package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCampfireBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class EnchantedCampfireBlock extends CampfireBlock {
	private final Supplier<Block> originalBlock;

	public EnchantedCampfireBlock(boolean spawnParticles, int fireDamage, Supplier<Block> originalBlock, Properties properties) {
		super(spawnParticles, fireDamage, properties);
		this.originalBlock = originalBlock;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedCampfireBlockEntity(pos, state);
	}

	@Override
	public Item asItem() {
		return originalBlock.get().asItem();
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (level.isClientSide) {
			return state.getValue(LIT) ?
					createTickerHelper(blockEntityType, ModRegistry.ENCHANTED_CAMPFIRE_BLOCK_ENTITY.get(), EnchantedCampfireBlockEntity::particleTick) :
					null;
		} else {
			return state.getValue(LIT) ?
					createTickerHelper(blockEntityType, ModRegistry.ENCHANTED_CAMPFIRE_BLOCK_ENTITY.get(), EnchantedCampfireBlockEntity::cookTick) :
					createTickerHelper(blockEntityType, ModRegistry.ENCHANTED_CAMPFIRE_BLOCK_ENTITY.get(), EnchantedCampfireBlockEntity::cooldownTick);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (state.getValue(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
			float damage = (float) this.fireDamage;
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof IEnchantable enchantable) {
				if (enchantable.hasEnchantment(ModEnchantments.BOILING.get())) {
					int enchantmentLevel = enchantable.getEnchantmentLevel(ModEnchantments.BOILING.get());
					damage = damage * (enchantmentLevel + 1);
				}
			}
			entity.hurt(level.damageSources().inFire(), damage);
		}

	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		ItemStack originalStack = new ItemStack(originalBlock.get());
		if (level.getBlockEntity(pos) instanceof IEnchantable blockEntity && blockEntity.getEnchantmentsTag() != null) {
			originalStack.getOrCreateTag().put("Enchantments", blockEntity.getEnchantmentsTag());
		}
		return originalStack;
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
		float explosionResistance = super.getExplosionResistance(state, level, pos, explosion);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IEnchantable enchantable) {
			if (enchantable.hasEnchantment(Enchantments.BLAST_PROTECTION)) {
				int enchantmentLevel = enchantable.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
				explosionResistance *= ((enchantmentLevel + 1) * 30);
			}
		}
		return explosionResistance;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof EnchantedCampfireBlockEntity campfireBlockEntity) {
				if (!campfireBlockEntity.hasEnchantment(Enchantments.VANISHING_CURSE)) {
					Containers.dropContents(level, pos, campfireBlockEntity.getItems());
				}
			}

			if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
				level.removeBlockEntity(pos);
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		BlockEntity blockentity = params.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockentity instanceof IEnchantable enchantable) {
			if (enchantable.hasEnchantment(Enchantments.VANISHING_CURSE)) {
				return List.of();
			}
		}
		return super.getDrops(state, params);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource prandom) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IEnchantable enchantable) {
			if (!enchantable.hasEnchantment(ModEnchantments.CONCEALED.get())) {
				super.animateTick(state, level, pos, prandom);
			}
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IEnchantable enchantable) {
			enchantable.setEnchantments(stack.getEnchantmentTags());
		}
	}
}
