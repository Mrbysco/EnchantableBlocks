package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantedBlockEntity extends AbstractEnchantedBlockEntity {
	public EnchantedBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_BLOCK_ENTITY.get(), pos, state);
	}
}
