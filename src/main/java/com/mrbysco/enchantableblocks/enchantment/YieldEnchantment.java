package com.mrbysco.enchantableblocks.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class YieldEnchantment extends AbstractBlockEnchantment {
	public YieldEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
		super(rarity, category, equipmentSlots);
	}

	public int getMinCost(int pEnchantmentLevel) {
		return 15 + (pEnchantmentLevel - 1) * 9;
	}

	public int getMaxCost(int pEnchantmentLevel) {
		return super.getMinCost(pEnchantmentLevel) + 50;
	}

	public int getMaxLevel() {
		return 3;
	}
}
