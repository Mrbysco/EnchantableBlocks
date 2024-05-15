package com.mrbysco.enchantableblocks.menu;

import com.mrbysco.enchantableblocks.registry.ModMenus;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

public class EnchantedBeaconMenu extends BeaconMenu {
	public EnchantedBeaconMenu(int id, Container container) {
		super(id, container);
	}

	public EnchantedBeaconMenu(int id, Container container, ContainerData beaconData, ContainerLevelAccess levelAccess) {
		super(id, container, beaconData, levelAccess);
	}

	@Override
	public MenuType<?> getType() {
		return ModMenus.ENCHANTED_BEACON.get();
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(this.access, pPlayer, ModRegistry.ENCHANTED_BEACON.get());
	}
}
