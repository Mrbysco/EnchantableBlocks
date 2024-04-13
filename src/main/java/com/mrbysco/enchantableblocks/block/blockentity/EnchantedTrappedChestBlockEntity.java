package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantedTrappedChestBlockEntity extends EnchantedChestBlockEntity  {
	public EnchantedTrappedChestBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventID, int eventParam) {
		super.signalOpenCount(level, pos, state, eventID, eventParam);
		if (eventID != eventParam) {
			Block block = state.getBlock();
			level.updateNeighborsAt(pos, block);
			level.updateNeighborsAt(pos.below(), block);
		}
	}
}
