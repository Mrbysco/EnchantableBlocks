package com.mrbysco.enchantableblocks.block.blockentity.furnace;

import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantedSmokerBlockEntity extends AbstractEnchantedFurnaceBlockEntity {

	public EnchantedSmokerBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_SMOKER_BLOCK_ENTITY.get(), pos, state, RecipeType.SMOKING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.smoker");
	}

	@Override
	protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
		return new SmokerMenu(pId, pPlayer, this, this.dataAccess);
	}
}
