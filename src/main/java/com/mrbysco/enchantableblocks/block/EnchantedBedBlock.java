package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBedBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class EnchantedBedBlock extends BedBlock {
	private final Supplier<Block> originalBlock;

	public EnchantedBedBlock(DyeColor dyeColor, Supplier<Block> originalBlock, Properties properties) {
		super(dyeColor, properties);
		this.originalBlock = originalBlock;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedBedBlockEntity(pos, state);
	}

	@Override
	public Item asItem() {
		return originalBlock.get().asItem();
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult) {
		if (level.isClientSide) {
			return InteractionResult.CONSUME;
		} else {
			if (state.getValue(PART) != BedPart.HEAD) {
				pos = pos.relative(state.getValue(FACING));
				state = level.getBlockState(pos);
				if (!state.is(this)) {
					return InteractionResult.CONSUME;
				}
			}

			boolean explodes = false;
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof IEnchantable enchantable) {
				explodes = enchantable.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockEntity, ModEnchantments.INTENTIONAL_GAME_DESIGN));
			}
			if (!canSetSpawn(level) || explodes) {
				level.removeBlock(pos, false);
				BlockPos blockpos = pos.relative(state.getValue(FACING).getOpposite());
				if (level.getBlockState(blockpos).is(this)) {
					level.removeBlock(blockpos, false);
				}

				Vec3 vec3 = pos.getCenter();
				level.explode((Entity) null, level.damageSources().badRespawnPointExplosion(vec3), (ExplosionDamageCalculator) null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
				return InteractionResult.SUCCESS;
			} else if (state.getValue(OCCUPIED)) {
				if (!this.kickVillagerOutOfBed(level, pos)) {
					player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
				}

				return InteractionResult.SUCCESS;
			} else {
				player.startSleepInBed(pos).ifLeft((problem) -> {
					if (problem.getMessage() != null) {
						player.displayClientMessage(problem.getMessage(), true);
					}

				});
				return InteractionResult.SUCCESS;
			}
		}
	}

	private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
		List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
		if (villagers.isEmpty()) {
			return false;
		} else {
			villagers.getFirst().stopSleeping();
			return true;
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
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		BlockEntity blockentity = params.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockentity instanceof IEnchantable enchantable) {
			if (enchantable.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
				return List.of();
			}
		}
		return super.getDrops(state, params);
	}

//	@Override
//	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
//		super.setPlacedBy(level, pos, state, placer, stack);
//		BlockEntity blockentity = level.getBlockEntity(pos);
//		if (!level.isClientSide && blockentity instanceof IEnchantable enchantable) {
//			enchantable.setEnchantments(stack.getEnchantmentTags());
//		}
//
//		BlockPos blockpos = pos.relative(state.getValue(FACING));
//		BlockEntity blockentity2 = level.getBlockEntity(blockpos);
//		if (!level.isClientSide && blockentity2 instanceof IEnchantable enchantable) {
//			enchantable.setEnchantments(stack.getEnchantmentTags());
//		}
//	}
}
