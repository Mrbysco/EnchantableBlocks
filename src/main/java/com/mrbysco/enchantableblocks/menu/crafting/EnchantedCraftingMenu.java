package com.mrbysco.enchantableblocks.menu.crafting;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCraftingTableBlockEntity;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModMenus;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class EnchantedCraftingMenu extends RecipeBookMenu<CraftingContainer> {
	public final CraftingContainer craftMatrix;
	private final ResultContainer resultSlots = new ResultContainer();
	private final ContainerLevelAccess access;
	private final Player player;

	public EnchantedCraftingMenu(final int windowId, final Inventory playerInventory, @NotNull final FriendlyByteBuf data) {
		this(windowId, playerInventory, getBlockEntity(playerInventory, data));
	}

	private static EnchantedCraftingTableBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final BlockEntity entityAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());

		if (entityAtPos instanceof EnchantedCraftingTableBlockEntity blockEntity) {
			return blockEntity;
		}

		throw new IllegalStateException("Block entity is not correct! " + entityAtPos);
	}

	public EnchantedCraftingMenu(int id, Inventory playerInventory, EnchantedCraftingTableBlockEntity blockEntity) {
		super(ModMenus.ENCHANTED_CRAFTING.get(), id);
		this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
		this.player = playerInventory.player;

		if (blockEntity.hasEnchantment(ModEnchantments.PRESERVATION.get()))
			this.craftMatrix = new EnchantedCraftingContainer(this, blockEntity.handler);
		else
			this.craftMatrix = new TransientCraftingContainer(this, 3, 3);

		this.addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.resultSlots, 0, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
		}

		slotsChanged(this.craftMatrix);
	}

	protected static void slotChangedCraftingGrid(AbstractContainerMenu pMenu, Level pLevel, Player pPlayer, CraftingContainer pContainer, ResultContainer pResult) {
		if (!pLevel.isClientSide) {
			ServerPlayer serverplayer = (ServerPlayer) pPlayer;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<RecipeHolder<CraftingRecipe>> optional = pLevel.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, pContainer, pLevel);
			if (optional.isPresent()) {
				RecipeHolder<CraftingRecipe> craftingrecipe = optional.get();
				if (pResult.setRecipeUsed(pLevel, serverplayer, craftingrecipe)) {
					ItemStack itemstack1 = craftingrecipe.value().assemble(pContainer, pLevel.registryAccess());
					if (itemstack1.isItemEnabled(pLevel.enabledFeatures())) {
						itemstack = itemstack1;
					}
				}
			}

			pResult.setItem(0, itemstack);
			pMenu.setRemoteSlot(0, itemstack);
			serverplayer.connection.send(new ClientboundContainerSetSlotPacket(pMenu.containerId, pMenu.incrementStateId(), 0, itemstack));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void slotsChanged(Container pInventory) {
		this.access.execute((p_39386_, p_39387_) -> {
			slotChangedCraftingGrid(this, p_39386_, this.player, this.craftMatrix, this.resultSlots);
		});
	}

	public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
		this.craftMatrix.fillStackedContents(itemHelper);
	}

	public void clearCraftingContent() {
		this.craftMatrix.clearContent();
		this.resultSlots.clearContent();
	}

	public boolean recipeMatches(RecipeHolder<? extends Recipe<CraftingContainer>> recipe) {
		return recipe.value().matches(this.craftMatrix, this.player.level());
	}

	/**
	 * Called when the container is closed.
	 */
	public void removed(Player player) {
		super.removed(player);
//		this.access.execute((p_39371_, p_39372_) -> {
//			this.clearContainer(player, this.craftMatrix);
//		});
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemstack = slotStack.copy();
			if (index == 0) {
				this.access.execute((p_39378_, p_39379_) -> {
					slotStack.getItem().onCraftedBy(slotStack, p_39378_, player);
				});
				if (!this.moveItemStackTo(slotStack, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(slotStack, itemstack);
			} else if (index >= 10 && index < 46) {
				if (!this.moveItemStackTo(slotStack, 1, 10, false)) {
					if (index < 37) {
						if (!this.moveItemStackTo(slotStack, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(slotStack, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(slotStack, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (slotStack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, slotStack);
			if (index == 0) {
				player.drop(slotStack, false);
			}
		}

		return itemstack;
	}

	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
	}

	public int getResultSlotIndex() {
		return 0;
	}

	public int getGridWidth() {
		return this.craftMatrix.getWidth();
	}

	public int getGridHeight() {
		return this.craftMatrix.getHeight();
	}

	public int getSize() {
		return 10;
	}

	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	public boolean shouldMoveToInventory(int pSlotIndex) {
		return pSlotIndex != this.getResultSlotIndex();
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, ModRegistry.ENCHANTED_CRAFTING_TABLE.get());
	}
}
