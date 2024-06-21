package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedEnchantingTableBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.menu.EnchantedEnchantmentMenu;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedEnchantmentTableBlock extends EnchantingTableBlock {
	public EnchantedEnchantmentTableBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedEnchantingTableBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof EnchantedEnchantingTableBlockEntity tableBlockEntity) {
			Component component = tableBlockEntity.getDisplayName();
			return new SimpleMenuProvider((windowID, inventory, player) -> {
				return new EnchantedEnchantmentMenu(windowID, inventory, ContainerLevelAccess.create(level, pos));
			}, component);
		} else {
			return null;
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
	public Item asItem() {
		return Items.ENCHANTING_TABLE;
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


	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModRegistry.ENCHANTED_ENCHANTING_TABLE_BLOCK_ENTITY.get(),
				EnchantedEnchantingTableBlockEntity::bookAnimationTick) : null;
	}
}
