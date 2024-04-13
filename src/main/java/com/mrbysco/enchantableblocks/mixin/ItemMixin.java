package com.mrbysco.enchantableblocks.mixin;

import com.mrbysco.enchantableblocks.util.ReplacementUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Inject(method = "isEnchantable(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	public void enchantableblocks$isEnchantable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof BlockItem blockItem && stack.getCount() == 1) {
			Block block = blockItem.getBlock();
			if (ReplacementUtil.isEnchantable(block)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(method = "getEnchantmentValue()I", at = @At("HEAD"), cancellable = true)
	public void enchantableblocks$getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
		Item item = (Item) (Object) this;
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (ReplacementUtil.isEnchantable(block)) {
				cir.setReturnValue(1);
			}
		}
	}


}
