package me.hadden.smithlib.api.skin;

import java.util.Collection;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.HashSet;

import me.hadden.smithlib.SmithLib;
import me.hadden.smithlib.api.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class SkinAPI {

	public static HashMap<String, String> nicks = new HashMap<>();

	public static void changeSkin(Player p, String skin) {

		CraftPlayer cp = (CraftPlayer)p;

		GameProfile skingp = cp.getProfile();
		try {
			if(UUIDFetcher.getUUID(skin) == null) {
				skingp = GameProfilesFetcher.fetch(UUIDFetcher.getUUID("Steve"));
			} else {
				skingp = GameProfilesFetcher.fetch(UUIDFetcher.getUUID(skin));
			}
		}catch (Exception ex) {
		}

		Collection<Property> props = skingp.getProperties().get("textures");
		cp.getProfile().getProperties().removeAll("textures");

		cp.getProfile().getProperties().putAll("textures", props);
	}


	public static String getRealName(Player p) {
		return nicks.get(p.getUniqueId().toString());
	}

	public static void TirarFake(Player p) {
		changeSkin(p, getRealName(p));
		changeNick(p, getRealName(p));
		updateSkin(p);
		updateForOther(p);

		nicks.remove(p.getUniqueId().toString());
		p.setDisplayName(p.getName());
		p.setPlayerListName(p.getName());
	}

	public static void changeNick(Player p, String nick) {
		nicks.put(p.getUniqueId().toString(), p.getName());
		CraftPlayer cp = (CraftPlayer)p;

		try {
			Field pF = cp.getProfile().getClass().getDeclaredField("name");
			pF.setAccessible(true);
			pF.set(cp.getProfile(), nick);
		}catch (Exception ex) {

		}
	}

	@SuppressWarnings("deprecation")
	public static void updateSkin(Player p) {

		try
		{
			if (!p.isOnline()) {
				return;
			}
			CraftPlayer cp = (CraftPlayer)p;
			EntityPlayer ep = cp.getHandle();
			int entId = ep.getId();

			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep });

			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { entId });

			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);

			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { ep });

			PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.getWorld().worldProvider.getDimension(), 
					ep.getWorld().getDifficulty(), ep.getWorld().worldData.getType(), 
					EnumGamemode.getById(p.getGameMode().getValue()));

			PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0, 
					CraftItemStack.asNMSCopy(p.getItemInHand()));

			PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4, 
					CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));

			PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3, 
					CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));

			PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2, 
					CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));

			PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1, 
					CraftItemStack.asNMSCopy(p.getInventory().getBoots()));

			PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(p.getInventory().getHeldItemSlot());
			for (Player pOnline : ((CraftServer)Bukkit.getServer()).getOnlinePlayers())
			{
				final CraftPlayer craftOnline = (CraftPlayer)pOnline;
				PlayerConnection con = craftOnline.getHandle().playerConnection;
				if (pOnline.getName().equals(p.getName()))
				{
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
					con.sendPacket(respawn);
					con.sendPacket(slot);
					craftOnline.updateScaledHealth();
					craftOnline.getHandle().triggerHealthUpdate();
					craftOnline.updateInventory();
					Bukkit.getScheduler().runTask(SmithLib.getInstance(), new Runnable(){
						public void run()
						{
							craftOnline.getHandle().updateAbilities();
						}
					});
					PacketPlayOutPosition position = new PacketPlayOutPosition(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), new HashSet<>());
					con.sendPacket(position);
				}
				else if (pOnline.canSee(p))
				{
					con.sendPacket(removeEntity);
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
					con.sendPacket(addNamed);
					con.sendPacket(itemhand);
					con.sendPacket(helmet);
					con.sendPacket(chestplate);
					con.sendPacket(leggings);
					con.sendPacket(boots);

				}
			}
		}
		catch (Exception localException) {}
	}

	public static void updateForOther(Player p) {

		CraftPlayer cp = (CraftPlayer)p;
		EntityPlayer ep = cp.getHandle();
		int entId = ep.getId();

		PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep });

		PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { entId });

		PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);

		PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { ep });

		Bukkit.getOnlinePlayers().forEach(all -> {
			if(all != p) {
				CraftPlayer cpAll = (CraftPlayer)all;

				PlayerConnection con = cpAll.getHandle().playerConnection;
				con.sendPacket(removeEntity);
				con.sendPacket(removeInfo);
				con.sendPacket(addInfo);
				con.sendPacket(addNamed);
			}
		});
	}
}
