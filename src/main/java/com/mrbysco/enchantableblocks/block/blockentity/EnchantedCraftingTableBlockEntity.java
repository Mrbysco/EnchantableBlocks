package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.menu.crafting.EnchantedCraftingMenu;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
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
	public void load(CompoundTag tag) {
		super.load(tag);
		this.handler.deserializeNBT(tag.getCompound("ItemHandler"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("ItemHandler", handler.serializeNBT());
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
