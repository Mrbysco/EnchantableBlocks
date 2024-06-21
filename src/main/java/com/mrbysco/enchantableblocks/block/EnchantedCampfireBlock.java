package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCampfireBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
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
		if (state.getValue(LIT) && entity instanceof LivingEntity) {
			float damage = (float) this.fireDamage;
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof IEnchantable enchantable) {
				Holder<Enchantment> boilingHolder = EnchantmentUtil.getEnchantmentHolder(blockentity, ModEnchantments.BOILING);
				if (enchantable.hasEnchantment(boilingHolder)) {
					int enchantmentLevel = enchantable.getEnchantmentLevel(boilingHolder);
					damage = damage * (enchantmentLevel + 1);
				}
			}
			entity.hurt(level.damageSources().inFire(), damage);
		}

	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
		float explosionResistance = super.getExplosionResistance(state, level, pos, explosion);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IEnchantable enchantable) {
			Holder<Enchantment> blastHolder = EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.BLAST_PROTECTION);
			if (enchantable.hasEnchantment(blastHolder)) {
				int enchantmentLevel = enchantable.getEnchantmentLevel(blastHolder);
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
				if (!campfireBlockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
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
			if (enchantable.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
				return List.of();
			}
		}
		return super.getDrops(state, params);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource prandom) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IEnchantable enchantable) {
			if (!enchantable.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, ModEnchantments.CONCEALED))) {
				super.animateTick(state, level, pos, prandom);
			}
		}
	}


}
