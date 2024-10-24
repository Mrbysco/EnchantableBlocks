package com.mrbysco.enchantableblocks.block;

import com.mojang.serialization.MapCodec;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedChestBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedTrappedChestBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class EnchantedTrappedChestBlock extends EnchantedChestBlock {
	public static final MapCodec<EnchantedChestBlock> CODEC = simpleCodec(p_304364_ -> new EnchantedChestBlock(p_304364_, ModRegistry.ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY::get));

	public EnchantedTrappedChestBlock(Properties properties) {
		super(properties, ModRegistry.ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY::get);
	}

	@Override
	protected MapCodec<? extends AbstractChestBlock<EnchantedChestBlockEntity>> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedTrappedChestBlockEntity(pos, state);
	}

	@Override
	protected Stat<ResourceLocation> getOpenChestStat() {
		return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
	}

	@Override
	public Item asItem() {
		return Items.TRAPPED_CHEST;
	}

	@Override
	public boolean isSignalSource(BlockState pState) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return Mth.clamp(EnchantedChestBlockEntity.getOpenCount(blockAccess, pos), 0, 15);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return side == Direction.UP ? state.getSignal(blockAccess, pos, side) : 0;
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
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		BlockEntity blockentity = params.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockentity instanceof IEnchantable enchantable) {
			if (enchantable.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
				return List.of();
			}
		}
		return super.getDrops(state, params);
	}


}
