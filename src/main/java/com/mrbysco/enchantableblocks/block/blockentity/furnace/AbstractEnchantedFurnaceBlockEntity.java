package com.mrbysco.enchantableblocks.block.blockentity.furnace;

import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.mixin.AbstractFurnaceBlockEntityAccessor;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModTags;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import com.mrbysco.enchantableblocks.util.TagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractEnchantedFurnaceBlockEntity extends AbstractFurnaceBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected ItemEnchantments enchantments = ItemEnchantments.EMPTY;

	protected AbstractEnchantedFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
		super(blockEntityType, pos, state, recipeType);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractEnchantedFurnaceBlockEntity blockEntity) {
		AbstractFurnaceBlockEntityAccessor blockEntityAccessor = (AbstractFurnaceBlockEntityAccessor) blockEntity;
		final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck = blockEntityAccessor.getQuickCheck();
		boolean wasLit = blockEntity.isLit();
		boolean changed = false;
		boolean hasInput = !blockEntity.items.getFirst().isEmpty();
		boolean solar = blockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.SOLAR_RADIANCE));
		// If dimension has a fixed time (Level#isDay() always returns false there), check against the sky darkness directly (same as Level#isDay's impl)
		boolean solarRequirements = (level.isDay() || (level.dimensionType().hasFixedTime() && level.getSkyDarken() < 4)) && level.canSeeSky(pos.above());

		if (blockEntity.isLit()) {
			int speed = blockEntity.getSpeed();
			if (solar) {
				if (solarRequirements) {
					blockEntity.litTime = 200;
				} else {
					blockEntity.litTime -= speed;
				}
			} else {
				boolean preservation = blockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.PRESERVATION));
				if (preservation) {
					SingleRecipeInput singlerecipeinput = new SingleRecipeInput(blockEntity.getItem(0));
					RecipeHolder<?> recipe = hasInput ? quickCheck.getRecipeFor(singlerecipeinput, level).orElse(null) : null;
					if (recipe != null) blockEntity.litTime -= speed;

				} else {
					blockEntity.litTime -= speed;
				}
			}
		}

		ItemStack fuel = blockEntity.items.get(1);
		boolean hasFuel = !fuel.isEmpty();
		if (blockEntity.isLit() || ((solar && solarRequirements) || hasFuel) && hasInput) {
			SingleRecipeInput singlerecipeinput = new SingleRecipeInput(blockEntity.getItem(0));
			RecipeHolder<?> recipe = hasInput ? quickCheck.getRecipeFor(singlerecipeinput, level).orElse(null) : null;
			int i = blockEntity.getMaxStackSize();
			if (!blockEntity.isLit()) {
				if (solar && solarRequirements) {
					blockEntity.litTime = 200;
					blockEntity.litDuration = blockEntity.litTime;
				} else if (canBurn(level.registryAccess(), recipe, blockEntity.items, i, blockEntity)) {
					blockEntity.litTime = blockEntity.getBurnDuration(fuel, level);
					blockEntity.litDuration = blockEntity.litTime;
					if (blockEntity.isLit()) {
						changed = true;
						if (fuel.hasCraftingRemainingItem()) blockEntity.items.set(1, fuel.getCraftingRemainingItem());
						else if (hasFuel) {
							fuel.shrink(1);
							if (fuel.isEmpty()) {
								blockEntity.items.set(1, fuel.getCraftingRemainingItem());
							}
						}
					}
				}
			}

			if (blockEntity.isLit() && canBurn(level.registryAccess(), recipe, blockEntity.items, i, blockEntity)) {
				int speed = blockEntity.getSpeed();
				blockEntity.cookingProgress += speed;
				if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
					blockEntity.cookingProgress = 0;
					blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
					if (blockEntity.enchantedBurn(level.registryAccess(), recipe, blockEntity.items, i, blockEntity)) {
						blockEntity.setRecipeUsed(recipe);
					}

					changed = true;
				}
			} else {
				blockEntity.cookingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
			blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
		}

		if (wasLit != blockEntity.isLit()) {
			changed = true;
			state = state.setValue(AbstractFurnaceBlock.LIT, blockEntity.isLit());
			level.setBlock(pos, state, 3);
		}

		if (changed) {
			setChanged(level, pos, state);
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean canBurn(RegistryAccess pRegistryAccess, @Nullable RecipeHolder<?> pRecipe, NonNullList<ItemStack> pInventory, int pMaxStackSize, AbstractEnchantedFurnaceBlockEntity furnace) {
		if (!pInventory.get(0).isEmpty() && pRecipe != null) {
			ItemStack itemstack = ((RecipeHolder<? extends AbstractCookingRecipe>) pRecipe).value().assemble(new SingleRecipeInput(furnace.getItem(0)), pRegistryAccess);
			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = pInventory.get(2);
				if (itemstack1.isEmpty()) {
					return true;
				} else if (!ItemStack.isSameItemSameComponents(itemstack1, itemstack)) {
					return false;
				} else {
					return itemstack1.getCount() + itemstack.getCount() <= pMaxStackSize && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize() // Neo fix: make furnace respect stack sizes in furnace recipes
							? true : itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Neo fix: make furnace respect stack sizes in furnace recipes
				}
			}
		} else {
			return false;
		}
	}

	private int getSpeed() {
		int speed = 1;
		Holder<Enchantment> speedHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.SPEED);
		if (hasEnchantment(speedHolder)) {
			int enchantmentLevel = getEnchantmentLevel(speedHolder);
			//Adjust the speed based on the level of the enchantment
			speed += enchantmentLevel;
		}
		return speed;
	}

	@SuppressWarnings("unchecked")
	private boolean enchantedBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> inventory, int maxStackSize, AbstractEnchantedFurnaceBlockEntity furnace) {
		if (recipeHolder != null && canBurn(registryAccess, recipeHolder, inventory, maxStackSize, furnace)) {
			ItemStack inputStack = inventory.getFirst();
			ItemStack craftedStack = ((RecipeHolder<? extends AbstractCookingRecipe>) recipeHolder).value().assemble(new SingleRecipeInput(furnace.getItem(0)), registryAccess);
			Holder<Enchantment> yieldHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.YIELD);
			if (hasEnchantment(yieldHolder) && craftedStack.getCount() < craftedStack.getMaxStackSize() && !craftedStack.is(ModTags.Items.YIELD_BLACKLIST)) {
				int enchantmentLevel = getEnchantmentLevel(yieldHolder);
				//Adjust the craftedStack based on the level of the enchantment
				int count = 1 + enchantmentLevel;
				craftedStack.setCount(Mth.clamp(count, 1, craftedStack.getMaxStackSize()));
			}

			ItemStack resultStack = inventory.get(2);
			if (hasEnchantment(EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.EXPORTING)) && this.getBlockState() != null) {
				Direction direction = this.getBlockState().getValue(AbstractFurnaceBlock.FACING);
				BlockPos leftPos = this.getBlockPos().relative(direction.getClockWise());
				BlockPos rightPos = this.getBlockPos().relative(direction.getCounterClockWise());
				if (this.level.isLoaded(leftPos)) {
					BlockEntity blockEntity = this.level.getBlockEntity(leftPos);
					if (blockEntity != null) {
						IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, leftPos, direction.getCounterClockWise());
						if (itemHandler != null) {
							craftedStack = ItemHandlerHelper.insertItem(itemHandler, craftedStack, false);
						}
					}
				}
				if (this.level.isLoaded(rightPos)) {
					BlockEntity blockEntity = this.level.getBlockEntity(rightPos);
					if (blockEntity != null) {
						IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, rightPos, direction.getClockWise());
						if (itemHandler != null) {
							craftedStack = ItemHandlerHelper.insertItem(itemHandler, craftedStack, false);
						}
					}
				}
			}

			if (resultStack.isEmpty()) {
				inventory.set(2, craftedStack.copy());
			} else if (resultStack.is(craftedStack.getItem())) {
				resultStack.setCount(Mth.clamp(resultStack.getCount() + craftedStack.getCount(), 1, resultStack.getMaxStackSize()));
			}

			if (inputStack.is(Blocks.WET_SPONGE.asItem()) && !inventory.get(1).isEmpty() && inventory.get(1).is(Items.BUCKET)) {
				inventory.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			inputStack.shrink(1);
			return true;
		} else {
			return false;
		}
	}

	private static int getTotalCookTime(Level level, AbstractEnchantedFurnaceBlockEntity blockEntity) {
		AbstractFurnaceBlockEntityAccessor blockEntityAccessor = (AbstractFurnaceBlockEntityAccessor) blockEntity;
		SingleRecipeInput singlerecipeinput = new SingleRecipeInput(blockEntity.getItem(0));
		int cookTime = blockEntityAccessor.getQuickCheck().getRecipeFor(singlerecipeinput, level).map(recipeHolder -> recipeHolder.value().getCookingTime()).orElse(200);
		Holder<Enchantment> speedHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.SPEED);
		if (blockEntity.hasEnchantment(speedHolder)) {
			int enchantmentLevel = blockEntity.getEnchantmentLevel(speedHolder);
			//Adjust the cookTime based on the level of the enchantment
		}
		return cookTime;
	}

	protected int getBurnDuration(ItemStack fuel, @NotNull Level level) {
		int burnDuration = super.getBurnDuration(fuel);
		Holder<Enchantment> fuelEfficiencyHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.BLOCK_EFFICIENCY);
		if (burnDuration != 0 && this.hasEnchantment(fuelEfficiencyHolder)) {
			int enchantmentLevel = this.getEnchantmentLevel(fuelEfficiencyHolder);
			//Adjust the burnDuration based on the level of the enchantment
			burnDuration = Mth.ceil(burnDuration * (1F + (enchantmentLevel * 0.2F)));
		}
		return burnDuration;
	}

	private boolean isLit() {
		return this.litTime > 0;
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
		if (this.hasEnchantment(enchantment)) return this.enchantments.getLevel(enchantment);
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
}
