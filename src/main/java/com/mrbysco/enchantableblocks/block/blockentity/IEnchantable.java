package com.mrbysco.enchantableblocks.block.blockentity;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public interface IEnchantable {
	ItemEnchantments getEnchantments();

	void setEnchantments(ItemEnchantments enchantments);

	boolean hasEnchantment(Holder<Enchantment> enchantment);

	int getEnchantmentLevel(Holder<Enchantment> enchantment);

	boolean hasEnchantment(TagKey<Enchantment> enchantmentTag);

	int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag);

	boolean hideGlint();
}
