package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EnchantedRespawnAnchorBlock extends RespawnAnchorBlock implements EntityBlock {

	public EnchantedRespawnAnchorBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedBlockEntity(pos, state);
	}

	@Override
	public Item asItem() {
		return Items.RESPAWN_ANCHOR;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		ItemStack originalStack = new ItemStack(Blocks.RESPAWN_ANCHOR);
		if (level.getBlockEntity(pos) instanceof IEnchantable blockEntity && blockEntity.getEnchantmentsTag() != null) {
			originalStack.getOrCreateTag().put("Enchantments", blockEntity.getEnchantmentsTag());
		}
		return originalStack;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		boolean explodes = false;
		int efficiency = 1;
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof IEnchantable enchantable) {
			explodes = enchantable.hasEnchantment(ModEnchantments.INTENTIONAL_GAME_DESIGN.get());
			if (enchantable.hasEnchantment(Enchantments.BLOCK_EFFICIENCY)) {
				efficiency = enchantable.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
			}
		}

		if (hand == InteractionHand.MAIN_HAND && !isRespawnFuel(itemstack) && isRespawnFuel(player.getItemInHand(InteractionHand.OFF_HAND))) {
			return InteractionResult.PASS;
		} else if (isRespawnFuel(itemstack) && canBeCharged(state)) {
			charge(player, level, pos, state, efficiency);
			if (!player.getAbilities().instabuild) {
				itemstack.shrink(1);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		} else if (state.getValue(CHARGE) == 0) {
			return InteractionResult.PASS;
		} else if (!canSetSpawn(level) || explodes) {
			if (!level.isClientSide) {
				this.explode(state, level, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			if (!level.isClientSide) {
				ServerPlayer serverplayer = (ServerPlayer) player;
				if (serverplayer.getRespawnDimension() != level.dimension() || !pos.equals(serverplayer.getRespawnPosition())) {
					serverplayer.setRespawnPosition(level.dimension(), pos, 0.0F, false, true);
					level.playSound((Player) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResult.SUCCESS;
				}
			}

			return InteractionResult.CONSUME;
		}
	}

	public static void charge(@Nullable Entity entity, Level level, BlockPos pos, BlockState state, int efficiency) {
		int currentCharge = state.getValue(CHARGE);
		int newCharge = Mth.clamp(currentCharge + efficiency, 0, 4);
		BlockState blockstate = state.setValue(CHARGE, newCharge);
		level.setBlock(pos, blockstate, 3);
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
		level.playSound((Player) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	private static boolean isRespawnFuel(ItemStack stack) {
		return stack.is(Items.GLOWSTONE);
	}

	private static boolean canBeCharged(BlockState state) {
		return state.getValue(CHARGE) < 4;
	}

	private static boolean isWaterThatWouldFlow(BlockPos pos, Level level) {
		FluidState fluidstate = level.getFluidState(pos);
		if (!fluidstate.is(FluidTags.WATER)) {
			return false;
		} else if (fluidstate.isSource()) {
			return true;
		} else {
			float f = (float) fluidstate.getAmount();
			if (f < 2.0F) {
				return false;
			} else {
				FluidState fluidstate1 = level.getFluidState(pos.below());
				return !fluidstate1.is(FluidTags.WATER);
			}
		}
	}

	private void explode(BlockState state, Level level, final BlockPos pos) {
		level.removeBlock(pos, false);
		boolean flag = Direction.Plane.HORIZONTAL.stream().map(pos::relative).anyMatch((p_55854_) -> {
			return isWaterThatWouldFlow(p_55854_, level);
		});
		final boolean flag1 = flag || level.getFluidState(pos.above()).is(FluidTags.WATER);
		ExplosionDamageCalculator explosionDamageCalculator = new ExplosionDamageCalculator() {
			public Optional<Float> getBlockExplosionResistance(Explosion p_55904_, BlockGetter p_55905_, BlockPos p_55906_, BlockState p_55907_, FluidState p_55908_) {
				return p_55906_.equals(pos) && flag1 ? Optional.of(Blocks.WATER.getExplosionResistance()) : super.getBlockExplosionResistance(p_55904_, p_55905_, p_55906_, p_55907_, p_55908_);
			}
		};
		Vec3 vec3 = pos.getCenter();
		level.explode((Entity) null, level.damageSources().badRespawnPointExplosion(vec3), explosionDamageCalculator, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
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
