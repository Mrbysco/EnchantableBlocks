package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnchantedChestBlockEntity extends AbstractEnchantedBlockEntity implements LidBlockEntity, Container, MenuProvider, Nameable, IEnchantable {
	@Nullable
	private Component name;

	public final ItemStackHandler handler = new ItemStackHandler(54) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public void setStackInSlot(int slot, @NotNull ItemStack stack) {
			if (!isWithinRange(slot)) return;
			super.setStackInSlot(slot, stack);
		}

		@Override
		public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (!isWithinRange(slot)) return ItemStack.EMPTY;
			return super.extractItem(slot, amount, simulate);
		}

		@Override
		public @NotNull ItemStack getStackInSlot(int slot) {
			if (!isWithinRange(slot)) return ItemStack.EMPTY;
			return super.getStackInSlot(slot);
		}

		@Override
		public int getSlots() {
			int storingLevel = getEnchantmentLevel(ModTags.General.STORAGE_UPGRADE);
			if (storingLevel < 1) {
				return 27;
			} else if (storingLevel == 1) {
				return 36;
			} else if (storingLevel == 2) {
				return 45;
			} else {
				return super.getSlots();
			}
		}
	};
	private final LazyOptional<IItemHandler> handlerHolder = LazyOptional.of(() -> handler);
	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			EnchantedChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
		}

		protected void onClose(Level level, BlockPos pos, BlockState state) {
			EnchantedChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
		}

		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int eventID, int eventParam) {
			EnchantedChestBlockEntity.this.signalOpenCount(level, pos, state, eventID, eventParam);
		}

		protected boolean isOwnContainer(Player player) {
			if (!(player.containerMenu instanceof ChestMenu)) {
				return false;
			} else {
				Container container = ((ChestMenu) player.containerMenu).getContainer();
				return container == EnchantedChestBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer) container).contains(EnchantedChestBlockEntity.this);
			}
		}
	};
	private final ChestLidController chestLidController = new ChestLidController();

	public EnchantedChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public EnchantedChestBlockEntity(BlockPos pos, BlockState state) {
		super(ModRegistry.ENCHANTED_CHEST_BLOCK_ENTITY.get(), pos, state);
	}

	public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, EnchantedChestBlockEntity blockEntity) {
		blockEntity.chestLidController.tickLid();
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("CustomName", 8)) {
			this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
		}

		this.handler.deserializeNBT(tag.getCompound("Items"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.name != null) {
			tag.putString("CustomName", Component.Serializer.toJson(this.name));
		}

		tag.put("Items", handler.serializeNBT());
	}

	public void setCustomName(Component pName) {
		this.name = pName;
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : Component.translatable("container.chest");
	}

	@Override
	public Component getDisplayName() {
		return getName();
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerID, Inventory playerInventory, Player player) {
		return null;
	}

	@Override
	public float getOpenNess(float partialTicks) {
		return this.chestLidController.getOpenness(partialTicks);
	}

	public static int getOpenCount(BlockGetter pLevel, BlockPos pPos) {
		BlockState blockstate = pLevel.getBlockState(pPos);
		if (blockstate.hasBlockEntity()) {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			if (blockentity instanceof EnchantedChestBlockEntity) {
				return ((EnchantedChestBlockEntity) blockentity).openersCounter.getOpenerCount();
			}
		}

		return 0;
	}

	private static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent soundEvent) {
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.5D;
		double d2 = (double) pos.getZ() + 0.5D;
		level.playSound((Player) null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}

	public void recheckOpen() {
		if (!this.remove) {
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventID, int eventParam) {
		Block block = state.getBlock();
		level.blockEvent(pos, block, 1, eventParam);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.chestLidController.shouldBeOpen(type > 0);
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	@Override
	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	@Override
	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	@Override
	public int getContainerSize() {
		return this.handler.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < this.handler.getSlots(); ++i) {
			if (!this.handler.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		if (!isWithinRange(slot)) return ItemStack.EMPTY;
		return this.handler.getStackInSlot(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = handler.extractItem(slot, amount, false);
		if (!isWithinRange(slot)) return ItemStack.EMPTY;
		if (!stack.isEmpty())
			setChanged();
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (!isWithinRange(slot)) return ItemStack.EMPTY;
		ItemStack stack = getItem(slot);
		if (stack.isEmpty()) return ItemStack.EMPTY;
		handler.setStackInSlot(slot, stack);
		return stack;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (!isWithinRange(slot)) return;
		handler.setStackInSlot(slot, stack);
	}

	private boolean isWithinRange(int slot) {
		int storingLevel = getEnchantmentLevel(ModEnchantments.STORING.get());
		if (storingLevel < 1) {
			return slot < 27;
		} else if (storingLevel == 1) {
			return slot < 36;
		} else if (storingLevel == 2) {
			return slot < 45;
		} else {
			return slot < 54;
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		for (int i = 0; i < this.handler.getSlots(); ++i) {
			if (!this.handler.getStackInSlot(i).isEmpty()) {
				this.handler.setStackInSlot(i, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (!this.remove && cap == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
			return this.handlerHolder.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.handlerHolder.invalidate();
	}
}
