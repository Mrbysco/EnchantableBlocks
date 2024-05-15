package com.mrbysco.enchantableblocks.mixin;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedDispenserBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractProjectileDispenseBehavior.class)
public abstract class AbstractProjectileDispenseBehaviorMixin extends DefaultDispenseItemBehavior {
	@Shadow
	protected abstract Projectile getProjectile(Level level, Position position, ItemStack stack);

	@Shadow
	protected abstract float getPower();

	@Shadow
	protected abstract float getUncertainty();

	@Inject(method = "execute(Lnet/minecraft/core/dispenser/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
			at = @At("HEAD"), cancellable = true)
	public void enchantableblocks$execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (stack.is(ItemTags.ARROWS) && source.blockEntity() instanceof EnchantedDispenserBlockEntity enchantedDispenserBlock) {
			Level level = source.level();
			Position position = DispenserBlock.getDispensePosition(source);
			Direction direction = source.state().getValue(DispenserBlock.FACING);
			Projectile projectile = this.getProjectile(level, position, stack);
			if (projectile instanceof AbstractArrow abstractArrow) {
				if (enchantedDispenserBlock.hasEnchantment(Enchantments.POWER_ARROWS)) {
					int power = enchantedDispenserBlock.getEnchantmentLevel(Enchantments.POWER_ARROWS);
					abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double) power * 0.5D + 0.5D);
				}

				if (enchantedDispenserBlock.hasEnchantment(Enchantments.PUNCH_ARROWS)) {
					int punch = enchantedDispenserBlock.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
					abstractArrow.setKnockback(punch);
				}

				if (enchantedDispenserBlock.hasEnchantment(Enchantments.FLAMING_ARROWS)) {
					abstractArrow.setSecondsOnFire(100);
				}
			}

			projectile.shoot((double) direction.getStepX(), (double) ((float) direction.getStepY() + 0.1F), (double) direction.getStepZ(), this.getPower(), this.getUncertainty());
			level.addFreshEntity(projectile);
			stack.shrink(1);
			cir.setReturnValue(stack);
		}
	}
}
