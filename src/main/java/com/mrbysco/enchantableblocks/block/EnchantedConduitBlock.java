package com.mrbysco.enchantableblocks.block;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedConduitBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ConduitBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedConduitBlock extends ConduitBlock {
	public EnchantedConduitBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedConduitBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModRegistry.ENCHANTED_CONDUIT_BLOCK_ENTITY.get(), level.isClientSide ?
				EnchantedConduitBlockEntity::clientTick :
				EnchantedConduitBlockEntity::serverTick);
	}

	@Override
	public Item asItem() {
		return Items.CONDUIT;
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
