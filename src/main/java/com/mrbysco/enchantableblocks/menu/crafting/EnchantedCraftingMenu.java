package com.mrbysco.enchantableblocks.menu.crafting;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCraftingTableBlockEntity;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModMenus;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
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
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class EnchantedCraftingMenu extends RecipeBookMenu<CraftingInput, CraftingRecipe> {
	public final CraftingContainer craftSlots;
	private final ResultContainer resultSlots = new ResultContainer();
	private final ContainerLevelAccess access;
	private final Player player;
	private boolean placingRecipe;

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

		if (blockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockEntity.getLevel(), ModEnchantments.PRESERVATION)))
			this.craftSlots = new EnchantedCraftingContainer(this, blockEntity.handler);
		else
			this.craftSlots = new TransientCraftingContainer(this, 3, 3);

		this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
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

		slotsChanged(this.craftSlots);
	}

	protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player,
	                                              CraftingContainer craftSlots, ResultContainer resultSlots,
	                                              @Nullable RecipeHolder<CraftingRecipe> recipe) {
		if (!level.isClientSide) {
			CraftingInput craftinginput = craftSlots.asCraftInput();
			ServerPlayer serverplayer = (ServerPlayer)player;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer()
					.getRecipeManager()
					.getRecipeFor(RecipeType.CRAFTING, craftinginput, level, recipe);
			if (optional.isPresent()) {
				RecipeHolder<CraftingRecipe> recipeholder = optional.get();
				CraftingRecipe craftingrecipe = recipeholder.value();
				if (resultSlots.setRecipeUsed(level, serverplayer, recipeholder)) {
					ItemStack itemstack1 = craftingrecipe.assemble(craftinginput, level.registryAccess());
					if (itemstack1.isItemEnabled(level.enabledFeatures())) {
						itemstack = itemstack1;
					}
				}
			}

			resultSlots.setItem(0, itemstack);
			menu.setRemoteSlot(0, itemstack);
			serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void slotsChanged(Container pInventory) {
		if (!this.placingRecipe) {
			this.access.execute((level, pos) -> slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots, null));
		}
	}

	@Override
	public void beginPlacingRecipe() {
		this.placingRecipe = true;
	}

	@Override
	public void finishPlacingRecipe(RecipeHolder<CraftingRecipe> recipe) {
		this.placingRecipe = false;
		this.access.execute((p_344361_, p_344362_) -> slotChangedCraftingGrid(this, p_344361_, this.player, this.craftSlots, this.resultSlots, recipe));
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
		this.craftSlots.fillStackedContents(itemHelper);
	}

	@Override
	public void clearCraftingContent() {
		this.craftSlots.clearContent();
		this.resultSlots.clearContent();
	}

	@Override
	public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
		return recipe.value().matches(this.craftSlots.asCraftInput(), this.player.level());
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player player) {
		super.removed(player);
		if (!(this.craftSlots instanceof EnchantedCraftingContainer)) {
			this.access.execute((level, pos) -> {
				this.clearContainer(player, this.craftSlots);
			});
		}
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

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public int getResultSlotIndex() {
		return 0;
	}

	@Override
	public int getGridWidth() {
		return this.craftSlots.getWidth();
	}

	@Override
	public int getGridHeight() {
		return this.craftSlots.getHeight();
	}

	@Override
	public int getSize() {
		return 10;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	@Override
	public boolean shouldMoveToInventory(int pSlotIndex) {
		return pSlotIndex != this.getResultSlotIndex();
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(this.access, player, ModRegistry.ENCHANTED_CRAFTING_TABLE.get());
	}
}
