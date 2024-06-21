package com.mrbysco.enchantableblocks.mixin;

import com.mrbysco.enchantableblocks.block.blockentity.EnchantedDispenserBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileDispenseBehavior.class)
public abstract class AbstractProjectileDispenseBehaviorMixin extends DefaultDispenseItemBehavior {

	@Shadow
	@Final
	private ProjectileItem projectileItem;

	@Shadow
	@Final
	private ProjectileItem.DispenseConfig dispenseConfig;

	@Inject(method = "execute(Lnet/minecraft/core/dispenser/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
			at = @At("HEAD"), cancellable = true)
	public void enchantableblocks$execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (stack.is(ItemTags.ARROWS) && source.blockEntity() instanceof EnchantedDispenserBlockEntity enchantedDispenserBlock) {
			Level level = source.level();
			Direction direction = source.state().getValue(DispenserBlock.FACING);
			Position position = this.dispenseConfig.positionFunction().getDispensePosition(source, direction);
			ItemStack cloneStack = stack.copy();

			//Put the enchantments on the arrow if the dispenser has them
			enchantedDispenserBlock.getEnchantments().entrySet().forEach(entry -> {
				if (entry.getKey().value().canEnchant(cloneStack) && enchantedDispenserBlock.hasEnchantment(entry.getKey()))
					cloneStack.enchant(entry.getKey(), enchantedDispenserBlock.getEnchantmentLevel(entry.getKey()));
			});

			Projectile projectile = this.projectileItem.asProjectile(level, position, cloneStack, direction);
			this.projectileItem
					.shoot(
							projectile,
							(double) direction.getStepX(),
							(double) direction.getStepY(),
							(double) direction.getStepZ(),
							this.dispenseConfig.power(),
							this.dispenseConfig.uncertainty()
					);
			level.addFreshEntity(projectile);
			stack.shrink(1);
			cir.setReturnValue(stack);
		}
	}
}
