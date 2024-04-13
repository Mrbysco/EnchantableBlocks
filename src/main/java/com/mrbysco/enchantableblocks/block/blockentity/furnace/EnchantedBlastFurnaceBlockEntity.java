package com.mrbysco.enchantableblocks.block.blockentity.furnace;

import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantedBlastFurnaceBlockEntity extends AbstractEnchantedFurnaceBlockEntity {

	public EnchantedBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_BLAST_FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.BLASTING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.blast_furnace");
	}

	@Override
	protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
		return new BlastFurnaceMenu(pId, pPlayer, this, this.dataAccess);
	}
}
