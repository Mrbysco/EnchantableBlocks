package com.mrbysco.enchantableblocks.block.blockentity;

import com.mojang.serialization.Dynamic;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class EnchantedCampfireBlockEntity extends CampfireBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected ItemEnchantments enchantments = ItemEnchantments.EMPTY;

	public EnchantedCampfireBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public static void cookTick(Level level, BlockPos pos, BlockState state, EnchantedCampfireBlockEntity blockEntity) {
		boolean flag = false;

		for (int i = 0; i < blockEntity.items.size(); ++i) {
			ItemStack inputStack = blockEntity.items.get(i);
			if (!inputStack.isEmpty()) {
				flag = true;
				int cookSpeed = 1;
				Holder<Enchantment> boilingHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.BOILING);
				if (blockEntity.hasEnchantment(boilingHolder)) {
					int enchantmentLevel = blockEntity.getEnchantmentLevel(boilingHolder);
					cookSpeed += enchantmentLevel;
				}
				blockEntity.cookingProgress[i] += cookSpeed;
				if (blockEntity.cookingProgress[i] >= blockEntity.cookingTime[i]) {
					SingleRecipeInput container = new SingleRecipeInput(inputStack);
					ItemStack resultStack = blockEntity.quickCheck.getRecipeFor(container, level).map((recipe) -> {
						return recipe.value().assemble(container, level.registryAccess());
					}).orElse(inputStack);
					if (resultStack.isItemEnabled(level.enabledFeatures())) {
						Containers.dropItemStack(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), resultStack);
						blockEntity.items.set(i, ItemStack.EMPTY);
						level.sendBlockUpdated(pos, state, state, 3);
						level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
					}
				}
			}
		}

		if (flag) {
			setChanged(level, pos, state);
		}

	}

	public static void cooldownTick(Level level, BlockPos pos, BlockState state, EnchantedCampfireBlockEntity blockEntity) {
		boolean flag = false;

		for (int i = 0; i < blockEntity.items.size(); ++i) {
			if (blockEntity.cookingProgress[i] > 0) {
				flag = true;
				blockEntity.cookingProgress[i] = Mth.clamp(blockEntity.cookingProgress[i] - 2, 0, blockEntity.cookingTime[i]);
			}
		}

		if (flag) {
			setChanged(level, pos, state);
		}

	}

	public static void particleTick(Level level, BlockPos pos, BlockState state, EnchantedCampfireBlockEntity blockEntity) {
		RandomSource randomsource = level.random;
		if (randomsource.nextFloat() < 0.11F) {
			for (int i = 0; i < randomsource.nextInt(2) + 2; ++i) {
				CampfireBlock.makeParticles(level, pos, state.getValue(CampfireBlock.SIGNAL_FIRE), false);
			}
		}

		int facing = state.getValue(CampfireBlock.FACING).get2DDataValue();

		for (int j = 0; j < blockEntity.items.size(); ++j) {
			if (!blockEntity.items.get(j).isEmpty() && randomsource.nextFloat() < 0.2F) {
				Direction direction = Direction.from2DDataValue(Math.floorMod(j + facing, 4));
				double d0 = (double) pos.getX() + 0.5D - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
				double d1 = (double) pos.getY() + 0.5D;
				double d2 = (double) pos.getZ() + 0.5D - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);

				for (int k = 0; k < 4; ++k) {
					level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
				}
			}
		}

	}

	@Override
	public BlockEntityType<?> getType() {
		return ModRegistry.ENCHANTED_CAMPFIRE_BLOCK_ENTITY.get();
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
		if (tag.contains("Enchantments")) {
			ItemEnchantments.CODEC
					.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("Enchantments")))
					.resultOrPartial()
					.ifPresent(enchantments -> this.enchantments = enchantments);
			this.enchantments = ItemEnchantments.CODEC.parse(NbtOps.INSTANCE, tag.get("Enchantments")).result().orElse(null);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (this.enchantments != null) {
			ItemEnchantments.CODEC
					.encodeStart(NbtOps.INSTANCE, this.enchantments)
					.resultOrPartial()
					.ifPresent(enchantments -> tag.put("Enchantments", enchantments));
		}
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
