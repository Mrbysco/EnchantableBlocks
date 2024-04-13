package com.mrbysco.enchantableblocks.enchantment;

import com.mrbysco.enchantableblocks.util.ReplacementUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractBlockEnchantment extends Enchantment {
	protected AbstractBlockEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
		super(rarity, category, equipmentSlots);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return ReplacementUtil.isEnchantmentApplicable(this, stack) && super.canEnchant(stack);
	}
}
