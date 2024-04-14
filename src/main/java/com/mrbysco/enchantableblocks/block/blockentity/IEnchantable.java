package com.mrbysco.enchantableblocks.block.blockentity;

import net.minecraft.nbt.ListTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

public interface IEnchantable {
	Map<Enchantment, Integer> getEnchantments();

	ListTag getEnchantmentsTag();

	void setEnchantments(ListTag enchantmentTags);

	void updateEnchantmentMap();

	boolean hasEnchantment(Enchantment enchantment);

	int getEnchantmentLevel(Enchantment enchantment);

	boolean hasEnchantment(TagKey<Enchantment> enchantmentTag);

	int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag);

	boolean hideGlint();
}
