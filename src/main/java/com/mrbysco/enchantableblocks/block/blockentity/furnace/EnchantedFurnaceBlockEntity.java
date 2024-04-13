package com.mrbysco.enchantableblocks.block.blockentity.furnace;

import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantedFurnaceBlockEntity extends AbstractEnchantedFurnaceBlockEntity {

	public EnchantedFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_FURNACE_BLOCK_ENTITY.get(), pos, state, RecipeType.SMELTING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.furnace");
	}

	@Override
	protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
		return new FurnaceMenu(pId, pPlayer, this, this.dataAccess);
	}
}
