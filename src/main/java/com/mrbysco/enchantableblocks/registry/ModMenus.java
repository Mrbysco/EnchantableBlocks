package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.menu.EnchantedEnchantmentMenu;
import com.mrbysco.enchantableblocks.menu.crafting.EnchantedCraftingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EnchantableBlocks.MOD_ID);

	public static final RegistryObject<MenuType<EnchantedEnchantmentMenu>> ENCHANTED_ENCHANTMENT = MENU_TYPES.register("enchanted_enchantment", () ->
			IForgeMenuType.create((windowId, inv, data) -> new EnchantedEnchantmentMenu(windowId, inv)));
	public static final RegistryObject<MenuType<EnchantedCraftingMenu>> ENCHANTED_CRAFTING = MENU_TYPES.register("enchanted_crafting", () ->
			IForgeMenuType.create(EnchantedCraftingMenu::new));

	public static ChestMenu fourRows(int pContainerId, Inventory pPlayerInventory, Container pContainer) {
		return new ChestMenu(MenuType.GENERIC_9x4, pContainerId, pPlayerInventory, pContainer, 4);
	}

	public static ChestMenu fiveRows(int pContainerId, Inventory pPlayerInventory, Container pContainer) {
		return new ChestMenu(MenuType.GENERIC_9x5, pContainerId, pPlayerInventory, pContainer, 5);
	}
}
