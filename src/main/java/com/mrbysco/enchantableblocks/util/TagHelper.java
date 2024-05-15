package com.mrbysco.enchantableblocks.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public class TagHelper {
	public static boolean matchesTag(Enchantment enchantment, TagKey<Enchantment> enchantmentTag) {
		Optional<ResourceKey<Enchantment>> enchantmentKey = BuiltInRegistries.ENCHANTMENT.getResourceKey(enchantment);
		if (enchantmentKey.isEmpty()) return false;
		Optional<Holder.Reference<Enchantment>> enchantmentHolder = BuiltInRegistries.ENCHANTMENT.getHolder(enchantmentKey.get());
		return enchantmentHolder.filter(enchantmentReference -> matchesTag(enchantmentReference, enchantmentTag)).isPresent();
	}

	public static boolean matchesTag(Holder<Enchantment> enchantment, TagKey<Enchantment> enchantmentTag) {
		Optional<HolderSet.Named<Enchantment>> optionalTag = BuiltInRegistries.ENCHANTMENT.getTag(enchantmentTag);
		return optionalTag.map(holders -> holders.contains(enchantment)).orElse(false);
	}
}
