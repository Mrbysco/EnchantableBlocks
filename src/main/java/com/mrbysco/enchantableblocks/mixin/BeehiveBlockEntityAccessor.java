package com.mrbysco.enchantableblocks.mixin;

import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeehiveBlockEntity.class)
public interface BeehiveBlockEntityAccessor {
	@Invoker("hasSavedFlowerPos")
	boolean invokeHasSavedFlowerPos();
}
