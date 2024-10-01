package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.mixin.HopperBlockEntityAccessor;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import com.mrbysco.enchantableblocks.util.TagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.IntStream;

public class EnchantedHopperBlockEntity extends HopperBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected ItemEnchantments enchantments = ItemEnchantments.EMPTY;

	public EnchantedHopperBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModRegistry.ENCHANTED_HOPPER_BLOCK_ENTITY.get();
	}

	public static void pushItemsTick(Level level, BlockPos pos, BlockState state, EnchantedHopperBlockEntity blockEntity) {
		int speed = 1;
		Holder<Enchantment> speedHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.SPEED);
		if (blockEntity.hasEnchantment(speedHolder)) {
			speed = blockEntity.getEnchantmentLevel(speedHolder) + 1;
		}
		blockEntity.cooldownTime -= speed;
		blockEntity.tickedGameTime = level.getGameTime();
		if (!((HopperBlockEntityAccessor) blockEntity).invokeIsOnCooldown()) {
			int count = 1;
			Holder<Enchantment> efficiencyHolder = EnchantmentUtil.getEnchantmentHolder(level, Enchantments.EFFICIENCY);
			if (blockEntity.hasEnchantment(efficiencyHolder)) {
				int enchantmentLevel = Mth.clamp(blockEntity.getEnchantmentLevel(efficiencyHolder), 0, 5);
				count = switch (enchantmentLevel) {
					case 1 -> 4;
					case 2 -> 8;
					case 3 -> 16;
					case 4 -> 32;
					case 5 -> 64;
					default -> count;
				};
			}
			blockEntity.setCooldown(0);
			int finalCount = count;
			tryMoveItems(level, pos, state, blockEntity, () -> suckInItems(level, blockEntity, finalCount));
		}
	}

	public static boolean suckInItems(Level level, EnchantedHopperBlockEntity hopper, int count) {
		Container container = getSourceContainer(level, hopper);
		BlockEntity blockEntity = level.getBlockEntity(hopper.getBlockPos().relative(Direction.UP));
		if (blockEntity != null) {
			IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, hopper.getBlockPos().relative(Direction.UP), Direction.DOWN);
			if (itemHandler != null) {
				for (int i = 0; i < itemHandler.getSlots(); i++) {
					ItemStack slotStack = itemHandler.getStackInSlot(i);
					int actualCount = Math.clamp(count, 1, slotStack.getCount());
					ItemStack extractItem = itemHandler.extractItem(i, actualCount, true);
					if (!extractItem.isEmpty()) {
						for (int j = 0; j < hopper.getContainerSize(); j++) {
							ItemStack destStack = hopper.getItem(j);
							if (hopper.canPlaceItem(j, extractItem) && (destStack.isEmpty() ||
									destStack.getCount() < destStack.getMaxStackSize() && destStack.getCount() <
											hopper.getMaxStackSize() && ItemStack.isSameItemSameComponents(extractItem, destStack))) {
								actualCount = Math.min(slotStack.getCount(), Math.min(count, destStack.getMaxStackSize() - destStack.getCount()));
								extractItem = itemHandler.extractItem(i, actualCount, false);

								if (destStack.isEmpty())
									hopper.setItem(j, extractItem);
								else {
									destStack.grow(actualCount);
									hopper.setItem(j, destStack);
								}
								hopper.setChanged();
								return true;
							}
						}
					}
				}

				return false;
			} else {
				for (ItemEntity itementity : getItemsAtAndAbove(level, hopper)) {
					if (addItem(hopper, itementity)) {
						return true;
					}
				}
			}
		} else {
			if (container != null) {
				Direction direction = Direction.DOWN;
				return isEmptyContainer(container, direction) ? false : getSlots(container, direction).anyMatch((p_59363_) -> {
					return tryTakeInItemFromSlot(hopper, container, p_59363_, direction, count);
				});
			} else {
				for (ItemEntity itementity : getItemsAtAndAbove(level, hopper)) {
					if (addItem(hopper, itementity)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Nullable
	private static Container getSourceContainer(Level pLevel, EnchantedHopperBlockEntity pHopper) {
		return getContainerAt(pLevel, pHopper.getLevelX(), pHopper.getLevelY() + 1.0D, pHopper.getLevelZ());
	}

	private static Container getContainerAt(Level pLevel, double pX, double pY, double pZ) {
		Container container = null;
		BlockPos blockpos = BlockPos.containing(pX, pY, pZ);
		BlockState blockstate = pLevel.getBlockState(blockpos);
		Block block = blockstate.getBlock();
		if (block instanceof WorldlyContainerHolder) {
			container = ((WorldlyContainerHolder) block).getContainer(blockstate, pLevel, blockpos);
		} else if (blockstate.hasBlockEntity()) {
			BlockEntity blockentity = pLevel.getBlockEntity(blockpos);
			if (blockentity instanceof Container) {
				container = (Container) blockentity;
				if (container instanceof ChestBlockEntity && block instanceof ChestBlock) {
					container = ChestBlock.getContainer((ChestBlock) block, blockstate, pLevel, blockpos, true);
				}
			}
		}

		if (container == null) {
			List<Entity> list = pLevel.getEntities((Entity) null, new AABB(pX - 0.5D, pY - 0.5D, pZ - 0.5D, pX + 0.5D, pY + 0.5D, pZ + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
			if (!list.isEmpty()) {
				container = (Container) list.get(pLevel.random.nextInt(list.size()));
			}
		}

		return container;
	}

	private static boolean isEmptyContainer(Container pContainer, Direction pDirection) {
		return getSlots(pContainer, pDirection).allMatch((slot) -> pContainer.getItem(slot).isEmpty());
	}

	private static boolean tryTakeInItemFromSlot(EnchantedHopperBlockEntity pHopper, Container pContainer, int slot, Direction pDirection,
	                                             int count) {
		ItemStack itemstack = pContainer.getItem(slot);
		if (!itemstack.isEmpty() && canTakeItemFromContainer(pHopper, pContainer, itemstack, slot, pDirection)) {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = addItem(pContainer, pHopper, pContainer.removeItem(slot, Math.min(count, itemstack1.getCount())), (Direction) null);
			if (itemstack2.isEmpty()) {
				pContainer.setChanged();
				return true;
			}

			pContainer.setItem(slot, itemstack1);
		}

		return false;
	}

	private static boolean canTakeItemFromContainer(Container pSource, Container pDestination, ItemStack pStack, int pSlot, Direction pDirection) {
		if (!pDestination.canTakeItem(pSource, pSlot, pStack)) {
			return false;
		} else {
			if (pDestination instanceof WorldlyContainer worldlycontainer) {
				if (!worldlycontainer.canTakeItemThroughFace(pSlot, pStack, pDirection)) {
					return false;
				}
			}

			return true;
		}
	}

	private static boolean tryMoveItems(Level level, BlockPos pos, BlockState state, EnchantedHopperBlockEntity blockEntity, BooleanSupplier validator) {
		if (!level.isClientSide) {
			if (!((HopperBlockEntityAccessor) blockEntity).invokeIsOnCooldown() && state.getValue(HopperBlock.ENABLED)) {
				boolean flag = false;
				if (!blockEntity.isEmpty()) {
					flag = ejectItems(level, pos, state, blockEntity);
				}

				if (!((HopperBlockEntityAccessor) blockEntity).invokeInventoryFull()) {
					flag |= validator.getAsBoolean();
				}

				if (flag) {
					blockEntity.setCooldown(8);
					setChanged(level, pos, state);
					return true;
				}
			}

		}
		return false;
	}

	private static boolean ejectItems(Level level, BlockPos pos, BlockState state, EnchantedHopperBlockEntity sourceContainer) {
		int count = 1;
		Holder<Enchantment> efficiencyHolder = EnchantmentUtil.getEnchantmentHolder(level, Enchantments.EFFICIENCY);
		if (sourceContainer.hasEnchantment(efficiencyHolder)) {
			int enchantmentLevel = Mth.clamp(sourceContainer.getEnchantmentLevel(efficiencyHolder), 0, 5);
			count = switch (enchantmentLevel) {
				case 1 -> 4;
				case 2 -> 8;
				case 3 -> 16;
				case 4 -> 32;
				case 5 -> 64;
				default -> count;
			};
		}
		Direction direction = state.getValue(HopperBlock.FACING);
		BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
		IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction), direction.getOpposite());
		if (blockEntity != null && itemHandler != null) {
			if (!isFull(itemHandler)) {
				for (int i = 0; i < sourceContainer.getContainerSize(); ++i) {
					if (!sourceContainer.getItem(i).isEmpty()) {
						ItemStack originalSlotContents = sourceContainer.getItem(i).copy();
						ItemStack insertStack = sourceContainer.removeItem(i, Math.min(count, originalSlotContents.getCount()));
						ItemStack remainder = ItemHandlerHelper.insertItem(itemHandler, insertStack, false);
						if (remainder.isEmpty()) {
							return true;
						}
						int extractedAmount = insertStack.getCount() - remainder.getCount();
						originalSlotContents.shrink(extractedAmount);

						sourceContainer.setItem(i, originalSlotContents);
					}
				}
			}
			return false;
		} else {
			Container container = getAttachedContainer(level, pos, state);
			if (container != null) {
				if (!isFullContainer(container, direction)) {
					for (int i = 0; i < sourceContainer.getContainerSize(); ++i) {
						if (!sourceContainer.getItem(i).isEmpty()) {
							ItemStack stackCopy = sourceContainer.getItem(i).copy();
							ItemStack addStack = addItem(sourceContainer, container,
									sourceContainer.removeItem(i, Math.min(count, stackCopy.getCount())), direction);
							if (addStack.isEmpty()) {
								container.setChanged();
								return true;
							}

							sourceContainer.setItem(i, stackCopy);
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean isFull(IItemHandler itemHandler) {
		for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
			ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
			if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot)) {
				return false;
			}
		}
		return true;
	}

	@Nullable
	private static Container getAttachedContainer(Level pLevel, BlockPos pPos, BlockState pState) {
		Direction direction = pState.getValue(HopperBlock.FACING);
		return getContainerAt(pLevel, pPos.relative(direction));
	}

	private static boolean isFullContainer(Container container, Direction direction) {
		return getSlots(container, direction).allMatch((p_59379_) -> {
			ItemStack itemstack = container.getItem(p_59379_);
			return itemstack.getCount() >= itemstack.getMaxStackSize();
		});
	}

	private static IntStream getSlots(Container pContainer, Direction pDirection) {
		return pContainer instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer) pContainer).getSlotsForFace(pDirection)) : IntStream.range(0, pContainer.getContainerSize());
	}

	private static IntStream getSlots(IItemHandler itemHandler) {
		return IntStream.range(0, itemHandler.getSlots());
	}

	@Override
	public ItemEnchantments getEnchantments() {
		return enchantments;
	}

	@Override
	public boolean hasEnchantment(Holder<Enchantment> enchantment) {
		return this.enchantments.getLevel(enchantment) > 0;
	}

	@Override
	public int getEnchantmentLevel(Holder<Enchantment> enchantment) {
		if (this.hasEnchantment(enchantment))
			return this.enchantments.getLevel(enchantment);
		return -1;
	}

	@Override
	public boolean hasEnchantment(TagKey<Enchantment> enchantmentTag) {
		for (Holder<Enchantment> enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(this.level.registryAccess(), enchantment, enchantmentTag)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag) {
		for (Holder<Enchantment> enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(this.level.registryAccess(), enchantment, enchantmentTag)) {
				return this.enchantments.getLevel(enchantment);
			}
		}
		return -1;
	}

	@Override
	public void setEnchantments(ItemEnchantments enchantments) {
		this.enchantments = enchantments;
	}

	@Override
	public boolean hideGlint() {
		return this.hideGlint;
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.loadEnchantments(tag, registries);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		this.saveEnchantments(tag, registries);
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);
		ItemEnchantments enchantments = componentInput.get(DataComponents.ENCHANTMENTS);
		if (enchantments != null) {
			this.enchantments = enchantments;
		}
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
		super.collectImplicitComponents(pComponents);
		pComponents.set(DataComponents.ENCHANTMENTS, this.getEnchantments());
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);

	}

	//Sync stuff
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
		var tag = packet.getTag();
		if (tag != null) {
			handleUpdateTag(tag, registries);

			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 3);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (level != null) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 3);
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		loadAdditional(tag, registries);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag, this.level.registryAccess());
		return tag;
	}

	@Override
	public boolean isValidBlockState(BlockState state) {
		return this.getType().isValid(state);
	}
}
