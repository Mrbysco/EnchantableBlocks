package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.block.blockentity.furnace.AbstractEnchantedFurnaceBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.furnace.EnchantedBlastFurnaceBlockEntity;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedBlastFurnaceBlock extends BlastFurnaceBlock {
	public EnchantedBlastFurnaceBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedBlastFurnaceBlockEntity(pos, state);
	}

	@Override
	public Item asItem() {
		return Items.BLAST_FURNACE;
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
			if (blockentity instanceof AbstractEnchantedFurnaceBlockEntity enchantedFurnaceBlockEntity) {
				if (level instanceof ServerLevel) {
					if (!enchantedFurnaceBlockEntity.hasEnchantment(Enchantments.VANISHING_CURSE)) {
						Containers.dropContents(level, pos, enchantedFurnaceBlockEntity);
						enchantedFurnaceBlockEntity.getRecipesToAwardAndPopExperience((ServerLevel) level, Vec3.atCenterOf(pos));
					}
				}

				level.updateNeighbourForOutputSignal(pos, this);
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
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack originalStack = new ItemStack(Blocks.BLAST_FURNACE);
		if (level.getBlockEntity(pos) instanceof IEnchantable blockEntity) {
			originalStack.getOrCreateTag().put("Enchantments", blockEntity.getEnchantmentsTag());
		}
		return originalStack;
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof AbstractEnchantedFurnaceBlockEntity) {
			player.openMenu((MenuProvider) blockentity);
			player.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
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

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createEnchantedFurnaceTicker(level, blockEntityType, ModRegistry.ENCHANTED_BLAST_FURNACE_BLOCK_ENTITY.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createEnchantedFurnaceTicker(Level level, BlockEntityType<T> serverType,
	                                                                                           BlockEntityType<? extends AbstractFurnaceBlockEntity> clientType) {
		return level.isClientSide ? null : createTickerHelper(serverType, clientType, AbstractEnchantedFurnaceBlockEntity::serverTick);
	}
}
