package me.hadden.smithlib.api.vault;

import me.hadden.smithlib.SmithLib;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Money {
	public static Economy economia = null;

	public static void register() {
		ConsoleCommandSender sc = SmithLib.getInstance().getServer().getConsoleSender();
		if (!setupEconomy()) {
			sc.sendMessage(ChatColor.RED + "Falta o Vault ou Iconomy");
			SmithLib.getInstance().getServer().getPluginManager().disablePlugin(SmithLib.getPlugin(SmithLib.class));
		}
	}

	public static boolean setupEconomy() {
		if (SmithLib.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		} else {
			RegisteredServiceProvider<Economy> rsp = SmithLib.getInstance().getServer().getServicesManager()
					.getRegistration(Economy.class);
			if (rsp == null) {
				return false;
			} else {
				economia = (Economy) rsp.getProvider();
				return economia != null;
			}
		}
	}

	public static Double get(Player p) {
		return economia.getBalance(p);
	}

	public static void add(Player p, Double valor) {
		economia.depositPlayer(p, valor);
	}

	public static void retirar(Player p, Double valor) {
		economia.withdrawPlayer(p, valor);
	}

	public static boolean contains(Player p, Double valor) {
		return economia.getBalance(p) >= valor;
	}
}
