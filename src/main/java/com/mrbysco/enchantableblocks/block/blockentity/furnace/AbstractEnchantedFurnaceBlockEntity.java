package com.mrbysco.enchantableblocks.block.blockentity.furnace;

import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.mixin.AbstractFurnaceBlockEntityAccessor;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModTags;
import com.mrbysco.enchantableblocks.util.TagHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

import javax.annotation.Nullable;
import java.util.Map;

public abstract class AbstractEnchantedFurnaceBlockEntity extends AbstractFurnaceBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

	protected AbstractEnchantedFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state,
	                                              RecipeType<? extends AbstractCookingRecipe> recipeType) {
		super(blockEntityType, pos, state, recipeType);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractEnchantedFurnaceBlockEntity blockEntity) {
		AbstractFurnaceBlockEntityAccessor blockEntityAccessor = (AbstractFurnaceBlockEntityAccessor) blockEntity;
		final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck = blockEntityAccessor.getQuickCheck();
		boolean wasLit = blockEntity.isLit();
		boolean changed = false;
		boolean hasInput = !blockEntity.items.get(0).isEmpty();
		boolean solar = blockEntity.hasEnchantment(ModEnchantments.SOLAR_RADIANCE.get());
		boolean solarRequirements = level.isDay() && level.canSeeSky(pos.above());

		if (blockEntity.isLit()) {
			int speed = blockEntity.getSpeed();
			if (solar) {
				if (solarRequirements) {
					blockEntity.litTime = 200;
				} else {
					blockEntity.litTime -= speed;
				}
			} else {
				boolean preservation = blockEntity.hasEnchantment(ModEnchantments.PRESERVATION.get());
				if (preservation) {
					RecipeHolder<?> recipe = hasInput ? quickCheck.getRecipeFor(blockEntity, level).orElse(null) : null;
					if (recipe != null)
						blockEntity.litTime -= speed;

				} else {
					blockEntity.litTime -= speed;
				}
			}
		}

		ItemStack fuel = blockEntity.items.get(1);
		boolean hasFuel = !fuel.isEmpty();
		if (blockEntity.isLit() || ((solar && solarRequirements) || hasFuel) && hasInput) {
			RecipeHolder<?> recipe = hasInput ? quickCheck.getRecipeFor(blockEntity, level).orElse(null) : null;
			int i = blockEntity.getMaxStackSize();
			if (!blockEntity.isLit()) {
				if (solar && solarRequirements) {
					blockEntity.litTime = 200;
					blockEntity.litDuration = blockEntity.litTime;
				} else if (blockEntityAccessor.invokeCanBurn(level.registryAccess(), recipe, blockEntity.items, i)) {
					blockEntity.litTime = blockEntity.getBurnDuration(fuel);
					blockEntity.litDuration = blockEntity.litTime;
					if (blockEntity.isLit()) {
						changed = true;
						if (fuel.hasCraftingRemainingItem())
							blockEntity.items.set(1, fuel.getCraftingRemainingItem());
						else if (hasFuel) {
							fuel.shrink(1);
							if (fuel.isEmpty()) {
								blockEntity.items.set(1, fuel.getCraftingRemainingItem());
							}
						}
					}
				}
			}

			if (blockEntity.isLit() && blockEntityAccessor.invokeCanBurn(level.registryAccess(), recipe, blockEntity.items, i)) {
				int speed = blockEntity.getSpeed();
				blockEntity.cookingProgress += speed;
				if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
					blockEntity.cookingProgress = 0;
					blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
					if (blockEntity.enchantedBurn(level.registryAccess(), recipe, blockEntity.items, i)) {
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

	private int getSpeed() {
		int speed = 1;
		if (hasEnchantment(ModEnchantments.SPEED.get())) {
			int enchantmentLevel = getEnchantmentLevel(ModEnchantments.SPEED.get());
			//Adjust the speed based on the level of the enchantment
			speed += enchantmentLevel;
		}
		return speed;
	}

	@SuppressWarnings("unchecked")
	private boolean enchantedBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> inventory, int maxStackSize) {
		if (recipeHolder != null && ((AbstractFurnaceBlockEntityAccessor) this).invokeCanBurn(registryAccess, recipeHolder, inventory, maxStackSize)) {
			ItemStack inputStack = inventory.get(0);
			ItemStack craftedStack = ((RecipeHolder<net.minecraft.world.item.crafting.Recipe<WorldlyContainer>>) recipeHolder).value().assemble(this, registryAccess);
			if (hasEnchantment(ModEnchantments.YIELD.get()) && craftedStack.getCount() < craftedStack.getMaxStackSize() && !craftedStack.is(ModTags.Items.YIELD_BLACKLIST)) {
				int enchantmentLevel = getEnchantmentLevel(ModEnchantments.YIELD.get());
				//Adjust the craftedStack based on the level of the enchantment
				int count = 1 + enchantmentLevel;
				craftedStack.setCount(Mth.clamp(count, 1, craftedStack.getMaxStackSize()));
			}

			ItemStack resultStack = inventory.get(2);
			if (hasEnchantment(ModEnchantments.EXPORTING.get()) && this.getBlockState() != null) {
				Direction direction = this.getBlockState().getValue(AbstractFurnaceBlock.FACING);
				BlockPos leftPos = this.getBlockPos().relative(direction.getClockWise());
				BlockPos rightPos = this.getBlockPos().relative(direction.getCounterClockWise());
				if (this.level.isLoaded(leftPos)) {
					BlockEntity blockEntity = this.level.getBlockEntity(leftPos);
					if (blockEntity != null) {
						IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, leftPos, direction.getCounterClockWise());
						if (itemHandler != null) {
							resultStack = ItemHandlerHelper.insertItem(itemHandler, craftedStack, false);
						}
					}
				}
				if (this.level.isLoaded(rightPos)) {
					BlockEntity blockEntity = this.level.getBlockEntity(rightPos);
					if (blockEntity != null) {
						IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, rightPos, direction.getClockWise());
						if (itemHandler != null) {
							resultStack = ItemHandlerHelper.insertItem(itemHandler, craftedStack, false);
						}
					}
				}
			}

			if (resultStack.isEmpty()) {
				inventory.set(2, craftedStack.copy());
			} else if (resultStack.is(craftedStack.getItem())) {
				resultStack.grow(craftedStack.getCount());
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
		int cookTime = blockEntityAccessor.getQuickCheck().getRecipeFor(blockEntity, level)
				.map(recipeHolder -> recipeHolder.value().getCookingTime()).orElse(200);
		if (blockEntity.hasEnchantment(ModEnchantments.SPEED.get())) {
			int enchantmentLevel = blockEntity.getEnchantmentLevel(ModEnchantments.SPEED.get());
			//Adjust the cookTime based on the level of the enchantment
		}
		return cookTime;
	}

	@Override
	protected int getBurnDuration(ItemStack fuel) {
		int burnDuration = super.getBurnDuration(fuel);
		if (burnDuration != 0 && this.hasEnchantment(ModEnchantments.FUEL_EFFICIENCY.get())) {
			int enchantmentLevel = this.getEnchantmentLevel(ModEnchantments.FUEL_EFFICIENCY.get());
			//Adjust the burnDuration based on the level of the enchantment
			burnDuration = Mth.ceil(burnDuration * (1F + (enchantmentLevel * 0.2F)));
		}
		return burnDuration;
	}

	private boolean isLit() {
		return this.litTime > 0;
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
		if (packet.getTag() != null)
			load(packet.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag);
		return tag;
	}
}
