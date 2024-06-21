package com.mrbysco.enchantableblocks.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public class TagHelper {
	public static boolean matchesTag(RegistryAccess registryAccess, Holder<Enchantment> enchantment,
	                                 TagKey<Enchantment> enchantmentTag) {
		var registry = registryAccess.registry(Registries.ENCHANTMENT);
		if (registry.isPresent()) {
			Optional<HolderSet.Named<Enchantment>> optionalTagSet = registry.get().getTag(enchantmentTag);
			var enchantmentKey = registry.get().getKey(enchantment.value());
			return enchantmentKey != null && optionalTagSet.map(enchantmentNamed -> enchantmentNamed.stream()
					.anyMatch(holders -> holders.is(enchantmentKey))).orElse(false);
		}
		return false;
	}
}
