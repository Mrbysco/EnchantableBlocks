package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.menu.EnchantedBeaconMenu;
import com.mrbysco.enchantableblocks.menu.EnchantedEnchantmentMenu;
import com.mrbysco.enchantableblocks.menu.crafting.EnchantedCraftingMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, EnchantableBlocks.MOD_ID);

	public static final Supplier<MenuType<EnchantedEnchantmentMenu>> ENCHANTED_ENCHANTMENT = MENU_TYPES.register("enchanted_enchantment", () ->
			IMenuTypeExtension.create((windowId, inv, data) -> new EnchantedEnchantmentMenu(windowId, inv)));
	public static final Supplier<MenuType<EnchantedCraftingMenu>> ENCHANTED_CRAFTING = MENU_TYPES.register("enchanted_crafting", () ->
			IMenuTypeExtension.create(EnchantedCraftingMenu::new));
	public static final Supplier<MenuType<EnchantedBeaconMenu>> ENCHANTED_BEACON = MENU_TYPES.register("enchanted_beacon", () ->
			IMenuTypeExtension.create((windowId, inv, data) -> new EnchantedBeaconMenu(windowId, inv)));

	public static ChestMenu fourRows(int pContainerId, Inventory pPlayerInventory, Container pContainer) {
		return new ChestMenu(MenuType.GENERIC_9x4, pContainerId, pPlayerInventory, pContainer, 4);
	}

	public static ChestMenu fiveRows(int pContainerId, Inventory pPlayerInventory, Container pContainer) {
		return new ChestMenu(MenuType.GENERIC_9x5, pContainerId, pPlayerInventory, pContainer, 5);
	}
}
