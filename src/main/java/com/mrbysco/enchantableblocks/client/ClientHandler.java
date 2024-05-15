package com.mrbysco.enchantableblocks.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedBeaconRenderer;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedBedRenderer;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedBlockEntityRenderer;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedCampfireRenderer;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedChestRenderer;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedConduitRenderer;
import com.mrbysco.enchantableblocks.client.renderer.EnchantedEnchantTableRenderer;
import com.mrbysco.enchantableblocks.client.screen.EnchantedCraftingScreen;
import com.mrbysco.enchantableblocks.mixin.RenderBuffersAccessor;
import com.mrbysco.enchantableblocks.registry.ModMenus;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.SortedMap;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		MenuScreens.register(ModMenus.ENCHANTED_ENCHANTMENT.get(), EnchantmentScreen::new);
		MenuScreens.register(ModMenus.ENCHANTED_CRAFTING.get(), EnchantedCraftingScreen::new);
		MenuScreens.register(ModMenus.ENCHANTED_BEACON.get(), BeaconScreen::new);

		ItemBlockRenderTypes.setRenderLayer(ModRegistry.ENCHANTED_CAMPFIRE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModRegistry.ENCHANTED_SOUL_CAMPFIRE.get(), RenderType.cutout());
	}

	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		//Non-specific blocks
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_FURNACE_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_BLAST_FURNACE_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_SMOKER_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_DISPENSER_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_HOPPER_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_BEEHIVE_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_CRAFTING_TABLE_BLOCK_ENTITY.get(), EnchantedBlockEntityRenderer::new);

		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_BEACON_BLOCK_ENTITY.get(), EnchantedBeaconRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_CAMPFIRE_BLOCK_ENTITY.get(), EnchantedCampfireRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_BED_BLOCK_ENTITY.get(), EnchantedBedRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_ENCHANTING_TABLE_BLOCK_ENTITY.get(), EnchantedEnchantTableRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_CONDUIT_BLOCK_ENTITY.get(), EnchantedConduitRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_CHEST_BLOCK_ENTITY.get(), EnchantedChestRenderer::new);
		event.registerBlockEntityRenderer(ModRegistry.ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY.get(), EnchantedChestRenderer::new);

//		event.registerBlockEntityRenderer(BlockEntityType.BEEHIVE, EnchantedBlockEntityRenderer::new);
	}

	public static void loadComplete(FMLLoadCompleteEvent event) {
		// Use the below RegisterRenderBuffersEvent event in 1.20.4
		SortedMap<RenderType, BufferBuilder> layers = ((RenderBuffersAccessor) Minecraft.getInstance().renderBuffers()).getFixedBuffers();
		layers.put(CustomRenderType.GLINT, new BufferBuilder(CustomRenderType.GLINT.bufferSize()));
	}

	//	@SubscribeEvent
	//	public static void onRegisterRenderTypes(final RegisterRenderBuffersEvent event) {
	//		event.registerRenderBuffer(GLINT);
	//	}
}
