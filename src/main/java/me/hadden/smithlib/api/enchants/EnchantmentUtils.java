package me.hadden.smithlib.api.enchants;

import java.util.Set;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentUtils {

	public static boolean canEnchantWithBook(final ItemStack enchantmentBook, final ItemStack itemToEnchant) {
		final EnchantmentStorageMeta meta = (EnchantmentStorageMeta)enchantmentBook.getItemMeta();
		final Set<Enchantment> keys = meta.getStoredEnchants().keySet();
		for (final Enchantment enchantment : keys) {
			if (enchantment.canEnchantItem(itemToEnchant)) {
				final ItemMeta itemMeta = itemToEnchant.getItemMeta();
				final Set<Enchantment> enchantsOnItem = itemMeta.getEnchants().keySet();
				boolean conflictFound = false;
				for (final Enchantment enchantOnItem : enchantsOnItem) {
					if (enchantment.conflictsWith(enchantOnItem)) {
						conflictFound = true;
					}
				}
				if (!conflictFound) {
					return true;
				}
				continue;
			}
		}
		return false;
	}

	public static void enchantWithBook(final ItemStack itemToEnchant, final ItemStack enchantmentBook) {
		final EnchantmentStorageMeta meta = (EnchantmentStorageMeta)enchantmentBook.getItemMeta();
		final Set<Enchantment> keys = meta.getStoredEnchants().keySet();
		for(final Enchantment enchantment : keys) {
			int level = meta.getStoredEnchantLevel(enchantment);
			itemToEnchant.addUnsafeEnchantment(enchantment, level);
		}
	}

	public static Double getEnchantCost(final ItemStack enchantmentBook) {
		Double cost = 0.0;
		final EnchantmentStorageMeta meta = (EnchantmentStorageMeta)enchantmentBook.getItemMeta();
		final Set<Enchantment> keys = meta.getStoredEnchants().keySet();
		for(final Enchantment enchantment : keys) {
			int level = meta.getStoredEnchantLevel(enchantment);
			if(level <= 2) {
				cost += level*5.5;
			}
			if(level <= 4) {
				cost += level*7.5;
			}
			if(level >= 5) {
				cost += level*11.5;
			}
		}
		return cost;
	}

}