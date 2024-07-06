package com.mrbysco.enchantableblocks;

import com.mojang.logging.LogUtils;
import com.mrbysco.enchantableblocks.client.ClientHandler;
import com.mrbysco.enchantableblocks.compat.top.TOPCompat;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModMenus;
import com.mrbysco.enchantableblocks.registry.ModPoiTypes;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.BlockReplacement;
import com.mrbysco.enchantableblocks.util.ReplacementUtil;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;

@Mod(EnchantableBlocks.MOD_ID)
public class EnchantableBlocks {
	public static final String MOD_ID = "enchantableblocks";
	public static final String REGISTER_REPLACEMENT = "register_replacement";
	public static final Logger LOGGER = LogUtils.getLogger();

	public EnchantableBlocks(IEventBus eventBus) {
		ModRegistry.BLOCKS.register(eventBus);
		ModRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		ModMenus.MENU_TYPES.register(eventBus);
		ModPoiTypes.POI_TYPES.register(eventBus);

		eventBus.addListener(ModRegistry::registerCapabilities);
		eventBus.addListener(this::sendImc);
		eventBus.addListener(this::receiveIMC);

		NeoForge.EVENT_BUS.addListener(this::onServerStarted);

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::registerMenuScreen);
			eventBus.addListener(ClientHandler::registerRenderers);
			eventBus.addListener(ClientHandler::onRegisterRenderTypes);
		}
	}

	public void sendImc(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded("theoneprobe")) {
			TOPCompat.register();
		}
	}

	public void receiveIMC(InterModProcessEvent event) {
		InterModComms.getMessages(MOD_ID, REGISTER_REPLACEMENT::equals)
				.filter(msg -> msg.messageSupplier().get() instanceof BlockReplacement).map(msg -> (BlockReplacement) msg.messageSupplier().get())
				.forEach(ReplacementUtil::addReplacement);
	}

	private void onServerStarted(ServerStartedEvent event) {
		ReplacementUtil.addReplacement(Blocks.FURNACE, ModRegistry.ENCHANTED_FURNACE);
		ReplacementUtil.addReplacement(Blocks.BLAST_FURNACE, ModRegistry.ENCHANTED_BLAST_FURNACE);
		ReplacementUtil.addReplacement(Blocks.SMOKER, ModRegistry.ENCHANTED_SMOKER);
		ReplacementUtil.addReplacement(Blocks.BEACON, ModRegistry.ENCHANTED_BEACON);
		ReplacementUtil.addReplacement(Blocks.CAMPFIRE, ModRegistry.ENCHANTED_CAMPFIRE);
		ReplacementUtil.addReplacement(Blocks.SOUL_CAMPFIRE, ModRegistry.ENCHANTED_SOUL_CAMPFIRE);
		ReplacementUtil.addReplacement(Blocks.MAGMA_BLOCK, ModRegistry.ENCHANTED_MAGMA_BLOCK);
		ReplacementUtil.addReplacement(Blocks.DISPENSER, ModRegistry.ENCHANTED_DISPENSER);
		ReplacementUtil.addReplacement(Blocks.RESPAWN_ANCHOR, ModRegistry.ENCHANTED_RESPAWN_ANCHOR);
		ReplacementUtil.addReplacement(Blocks.HOPPER, ModRegistry.ENCHANTED_HOPPER);
		ReplacementUtil.addReplacement(Blocks.ENCHANTING_TABLE, ModRegistry.ENCHANTED_ENCHANTING_TABLE);
		ReplacementUtil.addReplacement(Blocks.CONDUIT, ModRegistry.ENCHANTED_CONDUIT);
		ReplacementUtil.addReplacement(Blocks.CRAFTING_TABLE, ModRegistry.ENCHANTED_CRAFTING_TABLE);
		ReplacementUtil.addReplacement(Blocks.BEEHIVE, ModRegistry.ENCHANTED_BEEHIVE);
		ReplacementUtil.addReplacement(Blocks.CHEST, ModRegistry.ENCHANTED_CHEST);
		ReplacementUtil.addReplacement(Blocks.TRAPPED_CHEST, ModRegistry.ENCHANTED_TRAPPED_CHEST);

		ReplacementUtil.addReplacement(Blocks.WHITE_BED, ModRegistry.ENCHANTED_WHITE_BED);
		ReplacementUtil.addReplacement(Blocks.ORANGE_BED, ModRegistry.ENCHANTED_ORANGE_BED);
		ReplacementUtil.addReplacement(Blocks.MAGENTA_BED, ModRegistry.ENCHANTED_MAGENTA_BED);
		ReplacementUtil.addReplacement(Blocks.LIGHT_BLUE_BED, ModRegistry.ENCHANTED_LIGHT_BLUE_BED);
		ReplacementUtil.addReplacement(Blocks.YELLOW_BED, ModRegistry.ENCHANTED_YELLOW_BED);
		ReplacementUtil.addReplacement(Blocks.LIME_BED, ModRegistry.ENCHANTED_LIME_BED);
		ReplacementUtil.addReplacement(Blocks.PINK_BED, ModRegistry.ENCHANTED_PINK_BED);
		ReplacementUtil.addReplacement(Blocks.GRAY_BED, ModRegistry.ENCHANTED_GRAY_BED);
		ReplacementUtil.addReplacement(Blocks.LIGHT_GRAY_BED, ModRegistry.ENCHANTED_LIGHT_GRAY_BED);
		ReplacementUtil.addReplacement(Blocks.CYAN_BED, ModRegistry.ENCHANTED_CYAN_BED);
		ReplacementUtil.addReplacement(Blocks.PURPLE_BED, ModRegistry.ENCHANTED_PURPLE_BED);
		ReplacementUtil.addReplacement(Blocks.BLUE_BED, ModRegistry.ENCHANTED_BLUE_BED);
		ReplacementUtil.addReplacement(Blocks.BROWN_BED, ModRegistry.ENCHANTED_BROWN_BED);
		ReplacementUtil.addReplacement(Blocks.GREEN_BED, ModRegistry.ENCHANTED_GREEN_BED);
		ReplacementUtil.addReplacement(Blocks.RED_BED, ModRegistry.ENCHANTED_RED_BED);
		ReplacementUtil.addReplacement(Blocks.BLACK_BED, ModRegistry.ENCHANTED_BLACK_BED);
	}
}
