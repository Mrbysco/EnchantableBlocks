package com.mrbysco.enchantableblocks.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class IntentionalDesignEnchantment extends AbstractBlockEnchantment {
	public IntentionalDesignEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
		super(rarity, category, equipmentSlots);
	}

	public int getMinCost(int pEnchantmentLevel) {
		return 10;
	}

	public int getMaxCost(int pEnchantmentLevel) {
		return super.getMinCost(pEnchantmentLevel) + 40;
	}
}
