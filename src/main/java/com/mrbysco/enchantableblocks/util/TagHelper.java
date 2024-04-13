package com.mrbysco.enchantableblocks.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class TagHelper {
	public static boolean matchesTag(Enchantment enchantment, TagKey<Enchantment> enchantmentTag) {
		ITagManager<Enchantment> tags = ForgeRegistries.ENCHANTMENTS.tags();
		if (tags != null) {
			return tags.getTag(enchantmentTag).contains(enchantment);
		}
		return false;
	}
}
