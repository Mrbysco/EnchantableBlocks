package com.mrbysco.enchantableblocks.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public interface EnchantmentMenuAccessor {
	@Invoker("getEnchantmentList")
	List<EnchantmentInstance> invokeGetEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int enchantSlot, int level);
}
