package com.mrbysco.enchantableblocks.block.blockentity;

import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.TagHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public class EnchantedCampfireBlockEntity extends CampfireBlockEntity implements IEnchantable {
	protected ListTag enchantmentTag = null;
	protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

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
				if (blockEntity.hasEnchantment(ModEnchantments.BOILING.get())) {
					int enchantmentLevel = blockEntity.getEnchantmentLevel(ModEnchantments.BOILING.get());
					cookSpeed += enchantmentLevel;
				}
				blockEntity.cookingProgress[i] += cookSpeed;
				if (blockEntity.cookingProgress[i] >= blockEntity.cookingTime[i]) {
					Container container = new SimpleContainer(inputStack);
					ItemStack resultStack = blockEntity.quickCheck.getRecipeFor(container, level).map((recipe) -> {
						return recipe.assemble(container, level.registryAccess());
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
		}
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
