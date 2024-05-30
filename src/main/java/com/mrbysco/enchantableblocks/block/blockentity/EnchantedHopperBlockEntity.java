package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.mixin.HopperBlockEntityAccessor;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.TagHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.joml.Math;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.stream.IntStream;

public class EnchantedHopperBlockEntity extends HopperBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

	public EnchantedHopperBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModRegistry.ENCHANTED_HOPPER_BLOCK_ENTITY.get();
	}

	public static void pushItemsTick(Level level, BlockPos pos, BlockState state, EnchantedHopperBlockEntity blockEntity) {
		int speed = 1;
		if (blockEntity.hasEnchantment(ModEnchantments.SPEED.get())) {
			speed = blockEntity.getEnchantmentLevel(ModEnchantments.SPEED.get()) + 1;
		}
		blockEntity.cooldownTime -= speed;
		blockEntity.tickedGameTime = level.getGameTime();
		if (!((HopperBlockEntityAccessor) blockEntity).invokeIsOnCooldown()) {
			int count = 1;
			if (blockEntity.hasEnchantment(Enchantments.BLOCK_EFFICIENCY)) {
				int enchantmentLevel = Mth.clamp(blockEntity.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY), 0, 5);
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
		if (blockEntity != null && blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).isPresent()) {
			IItemHandler itemHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).orElse(null);
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
											hopper.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack))) {
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
		if (sourceContainer.hasEnchantment(Enchantments.BLOCK_EFFICIENCY)) {
			int enchantmentLevel = Mth.clamp(sourceContainer.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY), 0, 5);
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
		if (blockEntity != null && blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).isPresent()) {
			IItemHandler itemHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite()).orElse(null);
			if (!isFull(itemHandler)) {
				for (int i = 0; i < sourceContainer.getContainerSize(); ++i) {
					if (!sourceContainer.getItem(i).isEmpty()) {
						ItemStack originalSlotContents = sourceContainer.getItem(i).copy();
						ItemStack insertStack = sourceContainer.removeItem(i, Math.min(count, originalSlotContents.getCount()));
						ItemStack remainder = ItemHandlerHelper.insertItem(itemHandler, insertStack, false);
						if (remainder.isEmpty()) {
							return true;
						}

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
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	@Override
	public boolean hasEnchantment(Enchantment enchantment) {
		return this.enchantments.containsKey(enchantment);
	}

	@Override
	public int getEnchantmentLevel(Enchantment enchantment) {
		if (this.hasEnchantment(enchantment))
			return this.enchantments.get(enchantment);
		return -1;
	}

	@Override
	public boolean hasEnchantment(TagKey<Enchantment> enchantmentTag) {
		for (Enchantment enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(enchantment, enchantmentTag)) {
				return true;
			}
		}
		return this.enchantments.containsKey(enchantmentTag);
	}

	@Override
	public int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag) {
		for (Enchantment enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(enchantment, enchantmentTag)) {
				return this.enchantments.get(enchantment);
			}
		}
		return -1;
	}

	@Override
	public void setEnchantments(ListTag enchantmentTags) {
		this.enchantmentTag = enchantmentTags;
		this.updateEnchantmentMap();
	}

	@Override
	public ListTag getEnchantmentsTag() {
		return this.enchantmentTag;
	}

	@Override
	public void updateEnchantmentMap() {
		this.enchantments.clear();
		if (this.enchantmentTag != null) {
			EnchantmentHelper.deserializeEnchantments(this.enchantmentTag).forEach((enchantment, integer) -> {
				if (enchantment != null) {
					this.enchantments.put(enchantment, integer);
				}
			});
			this.hideGlint = this.hasEnchantment(ModEnchantments.GLINTLESS.get());
		}
	}

	@Override
	public boolean hideGlint() {
		return this.hideGlint;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Enchantments")) {
			this.enchantmentTag = tag.getList("Enchantments", Tag.TAG_COMPOUND);
			this.updateEnchantmentMap();
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.enchantmentTag != null)
			tag.put("Enchantments", enchantmentTag);
	}

	//Sync stuff
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		var tag = packet.getTag();
		if (tag != null) {
			handleUpdateTag(tag);

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
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag);
		return tag;
	}
}
