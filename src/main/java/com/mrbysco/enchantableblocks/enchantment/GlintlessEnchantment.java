package com.mrbysco.enchantableblocks.enchantment;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GlintlessEnchantment extends AbstractBlockEnchantment {
	public GlintlessEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
		super(rarity, category, equipmentSlots);
	}

	public int getMinCost(int pEnchantmentLevel) {
		return 2;
	}

	public int getMaxCost(int pEnchantmentLevel) {
		return this.getMinCost(pEnchantmentLevel) + 30;
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		ResourceLocation location = BuiltInRegistries.ENCHANTMENT.getKey(other);
		if (location != null && location.equals(new ResourceLocation("stickerframes", "foiled"))) {
			return false;
		}
		return super.checkCompatibility(other);
	}
}
