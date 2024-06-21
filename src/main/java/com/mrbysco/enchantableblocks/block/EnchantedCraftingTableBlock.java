package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCraftingTableBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import com.mrbysco.enchantableblocks.util.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedCraftingTableBlock extends CraftingTableBlock implements EntityBlock {
	public EnchantedCraftingTableBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedCraftingTableBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState pState, Level level, BlockPos pos, Player player, BlockHitResult pHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof EnchantedCraftingTableBlockEntity) {
				player.openMenu((EnchantedCraftingTableBlockEntity) blockEntity, pos);
				player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
			}

			return InteractionResult.CONSUME;
		}
	}

	@Override
	public Item asItem() {
		return Items.CRAFTING_TABLE;
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
			if (blockentity instanceof EnchantedCraftingTableBlockEntity craftingTableBlockEntity) {
				if (level instanceof ServerLevel) {
					if (!craftingTableBlockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
						for (int i = 0; i < craftingTableBlockEntity.handler.getSlots(); ++i) {
							MiscHelper.spawnItemStack(level, pos.getX(), pos.getY(), pos.getZ(), craftingTableBlockEntity.handler.getStackInSlot(i));
						}
					}
				}
			}

			super.onRemove(state, level, pos, newState, isMoving);
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
