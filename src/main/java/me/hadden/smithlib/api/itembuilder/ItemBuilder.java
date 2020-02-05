package me.hadden.smithlib.api.itembuilder;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.enchantments.*;
import org.bukkit.*;
import org.bukkit.potion.*;
import org.bukkit.entity.*;

public class ItemBuilder
{
    @SuppressWarnings("deprecation")
	public static ItemStack crearNormal(final int n, final int n2, final int n3) {
        return new ItemStack(n, n2, (short)n3);
    }
    
    public static ItemStack crearItem(final int n, final int n2, final int n3, final String s) {
        @SuppressWarnings("deprecation")
		final ItemStack itemStack = new ItemStack(n, n2, (short)n3);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
	public static ItemStack crearItem1(final int n, final int n2, final int n3, final String s, final String... array) {
        @SuppressWarnings("deprecation")
		final ItemStack itemStack = new ItemStack(n, n2, (short)n3);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
        final ArrayList<String> lore = new ArrayList<String>();
        for (int length = array.length, i = 0; i < length; ++i) {
            lore.add(ChatColor.translateAlternateColorCodes('&', array[i]));
            itemMeta.setLore((List<String>)lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
	public static ItemStack crearItem2(final int n, final int n2, final int n3, final String displayName, final ArrayList<String> list) {
        @SuppressWarnings("deprecation")
		final ItemStack itemStack = new ItemStack(n, n2, (short)n3);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        final ArrayList<String> lore = new ArrayList<String>();
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', (String)iterator.next()));
            itemMeta.setLore((List<String>)lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
	public static ItemStack crearItem2(final int n, final int n2, final int n3, final String displayName, final String[] array) {
        @SuppressWarnings("deprecation")
		final ItemStack itemStack = new ItemStack(n, n2, (short)n3);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        final ArrayList<String> lore = new ArrayList<String>();
        for (int length = array.length, i = 0; i < length; ++i) {
            lore.add(ChatColor.translateAlternateColorCodes('&', array[i]));
            itemMeta.setLore((List<String>)lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
	public static ItemStack toKit(final String s) {
        final String[] split = s.split(",");
        final ArrayList<String> lore = new ArrayList<String>();
        @SuppressWarnings("deprecation")
		final ItemStack itemStack = new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
        for (int i = 1; i < split.length; ++i) {
            if (split[i].startsWith("lore:")) {
                final ItemMeta itemMeta = itemStack.getItemMeta();
                lore.add(ChatColor.translateAlternateColorCodes('&', split[i].replace("lore:", "")));
                itemMeta.setLore((List<String>)lore);
                itemStack.setItemMeta(itemMeta);
            }
            Enchantment[] values;
            for (int length = (values = Enchantment.values()).length, j = 0; j < length; ++j) {
                final Enchantment enchantment = values[j];
                if (split[i].toUpperCase().startsWith(enchantment.getName().toUpperCase())) {
                    itemStack.addUnsafeEnchantment(enchantment, Integer.parseInt(split[i].replace(String.valueOf(String.valueOf(enchantment.getName().toUpperCase())) + ":", "")));
                }
            }
            if (split[i].startsWith("name:")) {
                final ItemMeta itemMeta2 = itemStack.getItemMeta();
                itemMeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', split[i].replace("name:", "")));
                itemStack.setItemMeta(itemMeta2);
            }
        }
        return itemStack;
    }
    
    public static ItemStack parseItem(final List<String> list) {
        if (list.size() < 2) {
            return null;
        }
        ItemStack itemStack = null;
        try {
            if (list.get(0).contains(":")) {
                final Material material = Material.getMaterial(list.get(0).split(":")[0].toUpperCase());
                final int int1 = Integer.parseInt(list.get(1));
                if (int1 < 1) {
                    return null;
                }
                itemStack = new ItemStack(material, int1, (short)Integer.parseInt(list.get(0).split(":")[1].toUpperCase()));
            }
            else {
                itemStack = new ItemStack(Material.getMaterial(list.get(0).toUpperCase()), Integer.parseInt(list.get(1)));
            }
            if (list.size() > 2) {
                for (int i = 2; i < list.size(); ++i) {
                    if (list.get(i).split(":")[0].equalsIgnoreCase("name")) {
                        final ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(list.get(i).split(":")[1]);
                        itemStack.setItemMeta(itemMeta);
                    }
                    else {
                        itemStack.addUnsafeEnchantment(getEnchant(list.get(i).split(":")[0]), Integer.parseInt(list.get(i).split(":")[1]));
                    }
                }
            }
        }
        catch (Exception ex) {}
        return itemStack;
    }
    
    public static PotionEffect parseEffect(final List<String> list) {
        if (list.size() < 2) {
            return null;
        }
        PotionEffect potionEffect = null;
        try {
            final PotionEffectType potionType = getPotionType(list.get(0));
            int n;
            if (Integer.parseInt(list.get(1)) == -1) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = 20 * Integer.parseInt(list.get(1));
            }
            potionEffect = new PotionEffect(potionType, n, Integer.parseInt(list.get(2)));
        }
        catch (Exception ex) {}
        return potionEffect;
    }
    
    @SuppressWarnings("unused")
	private static PotionEffectType getPotionType(final String s) {
		final String lowerCase;
        final String s2;
        switch (s2 = (lowerCase = s.toLowerCase())) {
            case "healthboost": {
                return PotionEffectType.HEALTH_BOOST;
            }
            case "invisibility": {
                return PotionEffectType.INVISIBILITY;
            }
            case "absorption": {
                return PotionEffectType.ABSORPTION;
            }
            case "hunger": {
                return PotionEffectType.HUNGER;
            }
            case "slowness": {
                return PotionEffectType.SLOW;
            }
            case "nausea": {
                return PotionEffectType.CONFUSION;
            }
            case "poison": {
                return PotionEffectType.POISON;
            }
            case "nightvision": {
                return PotionEffectType.NIGHT_VISION;
            }
            case "wither": {
                return PotionEffectType.WITHER;
            }
            case "weakness": {
                return PotionEffectType.WEAKNESS;
            }
            case "waterbreathing": {
                return PotionEffectType.WATER_BREATHING;
            }
            case "saturation": {
                return PotionEffectType.SATURATION;
            }
            case "haste": {
                return PotionEffectType.FAST_DIGGING;
            }
            case "speed": {
                return PotionEffectType.SPEED;
            }
            case "blindness": {
                return PotionEffectType.BLINDNESS;
            }
            case "miningfatique": {
                return PotionEffectType.SLOW_DIGGING;
            }
            case "jumpboost": {
                return PotionEffectType.JUMP;
            }
            case "instantdamage": {
                return PotionEffectType.HARM;
            }
            case "instanthealth": {
                return PotionEffectType.HEAL;
            }
            case "regeneration": {
                return PotionEffectType.REGENERATION;
            }
            case "strength": {
                return PotionEffectType.INCREASE_DAMAGE;
            }
            case "fireresistance": {
                return PotionEffectType.FIRE_RESISTANCE;
            }
            case "resistance": {
                return PotionEffectType.DAMAGE_RESISTANCE;
            }
            default:
                break;
        }
        return null;
    }
    
    @SuppressWarnings("unused")
	private static Enchantment getEnchant(final String s) {
        final String lowerCase;
        final String s2;
        switch (s2 = (lowerCase = s.toLowerCase())) {
            case "depthstrider": {
                return Enchantment.DEPTH_STRIDER;
            }
            case "blastprotection": {
                return Enchantment.PROTECTION_EXPLOSIONS;
            }
            case "fireprotection": {
                return Enchantment.PROTECTION_FIRE;
            }
            case "aquaaffinity": {
                return Enchantment.WATER_WORKER;
            }
            case "protection": {
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            }
            case "sharpness": {
                return Enchantment.DAMAGE_ALL;
            }
            case "luckofthesea": {
                return Enchantment.LUCK;
            }
            case "thorns": {
                return Enchantment.THORNS;
            }
            case "fortune": {
                return Enchantment.LOOT_BONUS_BLOCKS;
            }
            case "fireaspect": {
                return Enchantment.FIRE_ASPECT;
            }
            case "luck": {
                return Enchantment.LUCK;
            }
            case "lure": {
                return Enchantment.LURE;
            }
            case "flame": {
                return Enchantment.ARROW_FIRE;
            }
            case "power": {
                return Enchantment.ARROW_DAMAGE;
            }
            case "punch": {
                return Enchantment.ARROW_KNOCKBACK;
            }
            case "smite": {
                return Enchantment.DAMAGE_UNDEAD;
            }
            case "infinity": {
                return Enchantment.ARROW_INFINITE;
            }
            case "projectileprotection": {
                return Enchantment.PROTECTION_PROJECTILE;
            }
            case "looting": {
                return Enchantment.LOOT_BONUS_MOBS;
            }
            case "featherfall": {
                return Enchantment.PROTECTION_FALL;
            }
            case "baneofarthropods": {
                return Enchantment.DAMAGE_ARTHROPODS;
            }
            case "respiration": {
                return Enchantment.OXYGEN;
            }
            case "efficiency": {
                return Enchantment.DIG_SPEED;
            }
            case "knockback": {
                return Enchantment.KNOCKBACK;
            }
            case "silktouch": {
                return Enchantment.SILK_TOUCH;
            }
            case "unbreaking": {
                return Enchantment.DURABILITY;
            }
            default:
                break;
        }
        return null;
    }
    
    public static boolean isEnchanted(final ItemStack itemStack) {
        return itemStack.containsEnchantment(Enchantment.ARROW_DAMAGE) || itemStack.containsEnchantment(Enchantment.ARROW_DAMAGE) || itemStack.containsEnchantment(Enchantment.ARROW_FIRE) || itemStack.containsEnchantment(Enchantment.ARROW_INFINITE) || itemStack.containsEnchantment(Enchantment.ARROW_KNOCKBACK) || itemStack.containsEnchantment(Enchantment.DAMAGE_ALL) || itemStack.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) || itemStack.containsEnchantment(Enchantment.DAMAGE_UNDEAD) || itemStack.containsEnchantment(Enchantment.DIG_SPEED) || itemStack.containsEnchantment(Enchantment.DURABILITY) || itemStack.containsEnchantment(Enchantment.FIRE_ASPECT) || itemStack.containsEnchantment(Enchantment.KNOCKBACK) || itemStack.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) || itemStack.containsEnchantment(Enchantment.LOOT_BONUS_MOBS) || itemStack.containsEnchantment(Enchantment.LUCK) || itemStack.containsEnchantment(Enchantment.LURE) || itemStack.containsEnchantment(Enchantment.OXYGEN) || itemStack.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL) || itemStack.containsEnchantment(Enchantment.PROTECTION_EXPLOSIONS) || itemStack.containsEnchantment(Enchantment.PROTECTION_FALL) || itemStack.containsEnchantment(Enchantment.PROTECTION_FIRE) || itemStack.containsEnchantment(Enchantment.PROTECTION_PROJECTILE) || itemStack.containsEnchantment(Enchantment.SILK_TOUCH) || itemStack.containsEnchantment(Enchantment.THORNS) || itemStack.containsEnchantment(Enchantment.WATER_WORKER);
    }
    
    public static void darItem(final Player p, final Material mat, final int quant, final int modo, final String nome, final int lugar) {
        final ItemStack item = new ItemStack(mat, quant, (short)modo);
        final ItemMeta itemm = item.getItemMeta();
        itemm.setDisplayName(nome);
        item.setItemMeta(itemm);
        p.getInventory().setItem(lugar, item);
    }
    
    public ItemStack setOwner(final Player jogador,final ItemStack item, final String owner,final String nome, final int lugar) {
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(nome);
        item.setItemMeta((ItemMeta)meta);
        jogador.getInventory().setItem(lugar, item);
        return item;
    }
}
