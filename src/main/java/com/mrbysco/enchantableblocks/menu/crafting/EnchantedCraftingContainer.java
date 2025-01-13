package com.mrbysco.enchantableblocks.menu.crafting;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EnchantedCraftingContainer extends TransientCraftingContainer {
	boolean pauseUpdates;
	final ItemStackHandler inventory;

	public EnchantedCraftingContainer(AbstractContainerMenu menu, ItemStackHandler itemHandler) {
		super(menu, 3, 3);
		this.inventory = itemHandler;
	}

	@Override
	public List<ItemStack> getItems() {
		NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
		for (int i = 0; i < inventory.getSlots(); i++) {
			stacks.set(i, inventory.getStackInSlot(i));
		}
		return List.copyOf(stacks);
	}

	@Override
	public ItemStack getItem(int slot) {
		validateSlot(slot);
		return this.inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		validateSlot(slot);
		ItemStack stack = inventory.extractItem(slot, amount, false);
		if (!stack.isEmpty())
			onCraftMatrixChanged();
		return stack;
	}

	@Override
	public void setItem(int slot, @NotNull ItemStack stack) {
		validateSlot(slot);
		inventory.setStackInSlot(slot, stack);
		onCraftMatrixChanged();
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		validateSlot(slot);
		ItemStack s = getItem(slot);
		if (s.isEmpty()) return ItemStack.EMPTY;
		onCraftMatrixChanged();
		setItem(slot, ItemStack.EMPTY);
		return s;
	}

	@Override
	public boolean isEmpty() {
		return IntStream.range(0, inventory.getSlots()).allMatch(i -> inventory.getStackInSlot(i).isEmpty());
	}

	@Override
	public void clearContent() {
		//Nope
	}

	/**
	 * Validates the slot to make sure it's within the bounds of the inventory
	 *
	 * @param slot The slot index to validate
	 */
	public void validateSlot(int slot) {
		if (slot >= 0 && slot < getContainerSize()) {
			return;
		}
		throw new IndexOutOfBoundsException("Someone attempted to an out of bound stack at slot " + slot);
	}

	public void onCraftMatrixChanged() {
		if (!pauseUpdates) {
			this.menu.slotsChanged(this);
		}
	}
}
