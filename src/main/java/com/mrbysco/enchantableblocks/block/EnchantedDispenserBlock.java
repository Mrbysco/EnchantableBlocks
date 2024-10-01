package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedDispenserBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedDispenserBlock extends DispenserBlock {
	public EnchantedDispenserBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedDispenserBlockEntity(pos, state);
	}

	@Override
	protected void dispenseFrom(ServerLevel serverLevel, BlockState state, BlockPos pos) {
		EnchantedDispenserBlockEntity dispenserBlockEntity = serverLevel.getBlockEntity(pos, ModRegistry.ENCHANTED_DISPENSER_BLOCK_ENTITY.get()).orElse(null);
		if (dispenserBlockEntity == null) {
			EnchantableBlocks.LOGGER.warn("Ignoring dispensing attempt for Enchanted Dispenser without matching block entity at {}", pos);
		} else {
			BlockSource blocksource = new BlockSource(serverLevel, pos, state, dispenserBlockEntity);
			int i = dispenserBlockEntity.getRandomSlot(serverLevel.random);
			if (i < 0) {
				serverLevel.levelEvent(1001, pos, 0);
				serverLevel.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(dispenserBlockEntity.getBlockState()));
			} else {
				ItemStack itemstack = dispenserBlockEntity.getItem(i);
				DispenseItemBehavior dispenseitembehavior = this.getDispenseMethod(serverLevel, itemstack);
				if (dispenseitembehavior != DispenseItemBehavior.NOOP) {
					if (dispenserBlockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(serverLevel, Enchantments.INFINITY)) && itemstack.is(ItemTags.ARROWS)) {
						dispenseitembehavior.dispense(blocksource, itemstack);
					} else {
						dispenserBlockEntity.setItem(i, dispenseitembehavior.dispense(blocksource, itemstack));
					}
				}
			}
		}
	}

	@Override
	public Item asItem() {
		return Items.DISPENSER;
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
			if (blockentity instanceof EnchantedDispenserBlockEntity dispenserBlockEntity) {
				if (!dispenserBlockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(blockentity, Enchantments.VANISHING_CURSE))) {
					Containers.dropContents(level, pos, dispenserBlockEntity);
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
