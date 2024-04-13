package com.mrbysco.enchantableblocks.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {
	@Invoker("updateBase")
	public static int invokeUpdateBase(Level level, int x, int y, int z) {
		throw new AssertionError();
	}
}
