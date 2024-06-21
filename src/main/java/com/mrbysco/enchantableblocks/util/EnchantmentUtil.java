package com.mrbysco.enchantableblocks.util;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentUtil {
	private static final Map<ResourceKey<Enchantment>, Holder<Enchantment>> enchantmentCache = new HashMap<>();

	public static Holder<Enchantment> getEnchantmentHolder(RegistryAccess registryAccess, ResourceKey<Enchantment> resourceKey) {
		if (enchantmentCache.containsKey(resourceKey)) {
			return enchantmentCache.get(resourceKey);
		}
		var enchantmentRegistry = registryAccess.registry(Registries.ENCHANTMENT);
		if (enchantmentRegistry.isPresent()) {
			var enchantment = enchantmentRegistry.get().getHolder(resourceKey);
			if (enchantment.isPresent()) {
				enchantmentCache.put(resourceKey, enchantment.get());
				return enchantment.get();
			}
		}
		return null;
	}

	public static Holder<Enchantment> getEnchantmentHolder(Entity entity, ResourceKey<Enchantment> resourceKey) {
		return getEnchantmentHolder(entity.registryAccess(), resourceKey);
	}

	public static Holder<Enchantment> getEnchantmentHolder(Level level, ResourceKey<Enchantment> resourceKey) {
		return getEnchantmentHolder(level.registryAccess(), resourceKey);
	}

	public static Holder<Enchantment> getEnchantmentHolder(BlockEntity blockEntity, ResourceKey<Enchantment> resourceKey) {
		return getEnchantmentHolder(blockEntity.getLevel().registryAccess(), resourceKey);
	}

	public static void clearCache() {
		enchantmentCache.clear();
	}
}
