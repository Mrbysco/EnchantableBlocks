package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.menu.crafting.EnchantedCraftingMenu;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class EnchantedCraftingTableBlockEntity extends AbstractEnchantedBlockEntity implements MenuProvider {
	public final ItemStackHandler handler = new ItemStackHandler(9) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	public EnchantedCraftingTableBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_CRAFTING_TABLE_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.handler.deserializeNBT(registries, tag.getCompound("ItemHandler"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("ItemHandler", handler.serializeNBT(registries));
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.minecraft.crafting_table");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerID, Inventory playerInventory, Player player) {
		return new EnchantedCraftingMenu(containerID, playerInventory, this);
	}
}
