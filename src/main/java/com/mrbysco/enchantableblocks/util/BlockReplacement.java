package com.mrbysco.enchantableblocks.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Stores the original block, the replacement block and the enchantment tag the block supports
 *
 * @param originalBlock  The original block
 * @param replacement    The replacement block
 * @param enchantmentTag The enchantment tag the block supports
 */
public record BlockReplacement(Block originalBlock, Supplier<? extends Block> replacement,
                               TagKey<Enchantment> enchantmentTag) {

	/**
	 * Check if the enchantment is in the tag
	 *
	 * @param enchantment The enchantment to check
	 * @return If the enchantment is in the tag
	 */
	public boolean matchesTag(Enchantment enchantment) {
		return TagHelper.matchesTag(enchantment, enchantmentTag());
	}
}
