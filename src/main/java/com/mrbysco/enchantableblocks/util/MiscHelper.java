package com.mrbysco.enchantableblocks.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MiscHelper {
	public static void spawnItemStack(Level level, double x, double y, double z, ItemStack stack) {
		double d0 = (double) EntityType.ITEM.getWidth();
		double d1 = 1.0D - d0;
		double d2 = d0 / 2.0D;
		double d3 = Math.floor(x) + level.random.nextDouble() * d1 + d2;
		double d4 = Math.floor(y) + level.random.nextDouble() * d1;
		double d5 = Math.floor(z) + level.random.nextDouble() * d1 + d2;

		while (!stack.isEmpty()) {
			ItemEntity itementity = new ItemEntity(level, d3, d4, d5, stack.split(level.random.nextInt(21) + 10));
			float f = 0.05F;
			itementity.setDeltaMovement(level.random.nextGaussian() * (double) 0.05F, level.random.nextGaussian() * (double) f + (double) 0.2F, level.random.nextGaussian() * (double) f);
			level.addFreshEntity(itementity);
		}
	}
}
