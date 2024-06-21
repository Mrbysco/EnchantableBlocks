package com.mrbysco.enchantableblocks.mixin;

import com.mrbysco.enchantableblocks.util.ReplacementUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
	@Shadow
	public abstract Block getBlock();

	@Shadow
	protected abstract boolean canPlace(BlockPlaceContext context, BlockState state);

	public BlockItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
	public void enchantableblocks$getPlacementState(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
		if (context.getItemInHand().isEnchanted()) {
			Block block = ReplacementUtil.getReplacement(this.getBlock());
			if (block != null) {
				BlockState blockstate = block.getStateForPlacement(context);
				BlockState state = blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
				cir.setReturnValue(state);
			}
		}
	}
}
