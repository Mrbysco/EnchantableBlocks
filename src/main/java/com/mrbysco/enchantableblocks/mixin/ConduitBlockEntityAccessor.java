package com.mrbysco.enchantableblocks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.UUID;

@Mixin(ConduitBlockEntity.class)
public interface ConduitBlockEntityAccessor {
	@Invoker("updateHunting")
	public static void invokeUpdateHunting(ConduitBlockEntity blockEntity, List<BlockPos> positions) {
		throw new AssertionError();
	}

	@Invoker("updateClientTarget")
	public static void invokeUpdateClientTarget(Level level, BlockPos pos, ConduitBlockEntity blockEntity) {
		throw new AssertionError();
	}

	@Invoker("updateShape")
	public static boolean invokeUpdateShape(Level level, BlockPos pos, List<BlockPos> positions) {
		throw new AssertionError();
	}

	@Invoker("findDestroyTarget")
	public static LivingEntity invokeFindDestroyTarget(Level pLevel, BlockPos pPos, UUID pTargetId) {
		throw new AssertionError();
	}

	@Invoker("getDestroyRangeAABB")
	public static AABB invokeGetDestroyRangeAABB(BlockPos pos) {
		throw new AssertionError();
	}
}
