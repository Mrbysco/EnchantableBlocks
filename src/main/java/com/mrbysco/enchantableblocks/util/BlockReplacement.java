package com.mrbysco.enchantableblocks.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record BlockReplacement(Supplier<? extends Block> replacement, TagKey<Enchantment> enchantmentTag) {

	public boolean matchesTag(Enchantment enchantment) {
		return TagHelper.matchesTag(enchantment, enchantmentTag);
	}
}
