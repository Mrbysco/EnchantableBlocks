package com.mrbysco.enchantableblocks.mixin;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface HopperBlockEntityAccessor {
	@Invoker("isOnCooldown")
	boolean invokeIsOnCooldown();

	@Invoker("inventoryFull")
	boolean invokeInventoryFull();
}
