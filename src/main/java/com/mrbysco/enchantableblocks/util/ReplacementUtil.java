package com.mrbysco.enchantableblocks.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ReplacementUtil {
	private static final Map<Block, BlockReplacement> replacementMap = new HashMap<>();

	public static Block getReplacement(Block block) {
		if (replacementMap.containsKey(block)) {
			return replacementMap.get(block).replacement().get();
		}
		return null;
	}

	public static boolean isEnchantable(Block block) {
		return replacementMap.containsKey(block);
	}

	public static boolean isEnchantmentApplicable(Enchantment enchantment, ItemStack stack) {
		if (stack.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (replacementMap.containsKey(block)) {
				return replacementMap.get(block).matchesTag(enchantment);
			}
		}

		return false;
	}

	public static void addReplacement(BlockReplacement replacement) {
		replacementMap.put(replacement.originalBlock(), replacement);
	}

	public static void addReplacement(Block block, Supplier<? extends Block> replacement, TagKey<Enchantment> enchantmentTag) {
		replacementMap.put(block, new BlockReplacement(block, replacement, enchantmentTag));
	}
}
