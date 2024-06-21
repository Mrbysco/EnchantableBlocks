package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedHopperBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedHopperBlock extends HopperBlock {
	public EnchantedHopperBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedHopperBlockEntity(pos, state);
	}

	@Override
	public Item asItem() {
		return Items.HOPPER;
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, ModRegistry.ENCHANTED_HOPPER_BLOCK_ENTITY.get(), EnchantedHopperBlockEntity::pushItemsTick);
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
			if (blockentity instanceof EnchantedHopperBlockEntity hopperBlockEntity) {
				if (!hopperBlockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
					Containers.dropContents(level, pos, hopperBlockEntity);
					level.updateNeighbourForOutputSignal(pos, this);
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


}
