package com.mrbysco.enchantableblocks.util;

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

	public static void addReplacement(BlockReplacement replacement) {
		replacementMap.put(replacement.originalBlock(), replacement);
	}

	public static void addReplacement(Block block, Supplier<? extends Block> replacement) {
		replacementMap.put(block, new BlockReplacement(block, replacement));
	}
}
