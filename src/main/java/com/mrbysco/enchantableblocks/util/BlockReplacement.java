package com.mrbysco.enchantableblocks.util;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Stores the original block, the replacement block and the enchantment tag the block supports
 *
 * @param originalBlock The original block
 * @param replacement   The replacement block
 */
public record BlockReplacement(Block originalBlock, Supplier<? extends Block> replacement) {

}
