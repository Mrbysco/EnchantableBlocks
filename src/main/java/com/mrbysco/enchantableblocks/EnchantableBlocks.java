package com.mrbysco.enchantableblocks;

import com.mojang.logging.LogUtils;
import com.mrbysco.enchantableblocks.client.ClientHandler;
import com.mrbysco.enchantableblocks.compat.top.TOPCompat;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModLootFunctions;
import com.mrbysco.enchantableblocks.registry.ModMenus;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.registry.ModTags;
import com.mrbysco.enchantableblocks.util.ReplacementUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(EnchantableBlocks.MOD_ID)
public class EnchantableBlocks {
	public static final String MOD_ID = "enchantableblocks";
	public static final Logger LOGGER = LogUtils.getLogger();

	public EnchantableBlocks() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModRegistry.BLOCKS.register(eventBus);
		ModRegistry.BLOCK_ENTITY_TYPES.register(eventBus);
		ModRegistry.ITEMS.register(eventBus);
		ModEnchantments.ENCHANTMENTS.register(eventBus);
		ModMenus.MENU_TYPES.register(eventBus);
		ModLootFunctions.LOOT_ITEM_FUNCTIONS.register(eventBus);

		eventBus.addListener(this::sendImc);

		MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::registerRenderers);
			eventBus.addListener(ClientHandler::loadComplete);
		});
	}

	public void sendImc(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded("theoneprobe")) {
			TOPCompat.register();
		}
	}

	private void onServerStarted(ServerStartedEvent event) {
		ReplacementUtil.addReplacement(Blocks.FURNACE, ModRegistry.ENCHANTED_FURNACE, ModTags.FURNACE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.BLAST_FURNACE, ModRegistry.ENCHANTED_BLAST_FURNACE, ModTags.BLAST_FURNACE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.SMOKER, ModRegistry.ENCHANTED_SMOKER, ModTags.SMOKER_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.BEACON, ModRegistry.ENCHANTED_BEACON, ModTags.BEACON_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.CAMPFIRE, ModRegistry.ENCHANTED_CAMPFIRE, ModTags.CAMPFIRE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.SOUL_CAMPFIRE, ModRegistry.ENCHANTED_SOUL_CAMPFIRE, ModTags.SOUL_CAMPFIRE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.MAGMA_BLOCK, ModRegistry.ENCHANTED_MAGMA_BLOCK, ModTags.MAGMA_BLOCK_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.DISPENSER, ModRegistry.ENCHANTED_DISPENSER, ModTags.DISPENSER_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.RESPAWN_ANCHOR, ModRegistry.ENCHANTED_RESPAWN_ANCHOR, ModTags.RESPAWN_ANCHOR_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.HOPPER, ModRegistry.ENCHANTED_HOPPER, ModTags.HOPPER_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.ENCHANTING_TABLE, ModRegistry.ENCHANTED_ENCHANTING_TABLE, ModTags.ENCHANTMENT_TABLE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.CONDUIT, ModRegistry.ENCHANTED_CONDUIT, ModTags.CONDUIT_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.CRAFTING_TABLE, ModRegistry.ENCHANTED_CRAFTING_TABLE, ModTags.CRAFTING_TABLE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.BEEHIVE, ModRegistry.ENCHANTED_BEEHIVE, ModTags.BEEHIVE_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.CHEST, ModRegistry.ENCHANTED_CHEST, ModTags.CHEST_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.TRAPPED_CHEST, ModRegistry.ENCHANTED_TRAPPED_CHEST, ModTags.TRAPPED_CHEST_APPLICABLE);

		ReplacementUtil.addReplacement(Blocks.WHITE_BED, ModRegistry.ENCHANTED_WHITE_BED, ModTags.WHITE_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.ORANGE_BED, ModRegistry.ENCHANTED_ORANGE_BED, ModTags.ORANGE_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.MAGENTA_BED, ModRegistry.ENCHANTED_MAGENTA_BED, ModTags.MAGENTA_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.LIGHT_BLUE_BED, ModRegistry.ENCHANTED_LIGHT_BLUE_BED, ModTags.LIGHT_BLUE_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.YELLOW_BED, ModRegistry.ENCHANTED_YELLOW_BED, ModTags.YELLOW_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.LIME_BED, ModRegistry.ENCHANTED_LIME_BED, ModTags.LIME_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.PINK_BED, ModRegistry.ENCHANTED_PINK_BED, ModTags.PINK_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.GRAY_BED, ModRegistry.ENCHANTED_GRAY_BED, ModTags.GRAY_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.LIGHT_GRAY_BED, ModRegistry.ENCHANTED_LIGHT_GRAY_BED, ModTags.LIGHT_GRAY_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.CYAN_BED, ModRegistry.ENCHANTED_CYAN_BED, ModTags.CYAN_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.PURPLE_BED, ModRegistry.ENCHANTED_PURPLE_BED, ModTags.PURPLE_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.BLUE_BED, ModRegistry.ENCHANTED_BLUE_BED, ModTags.BLUE_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.BROWN_BED, ModRegistry.ENCHANTED_BROWN_BED, ModTags.BROWN_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.GREEN_BED, ModRegistry.ENCHANTED_GREEN_BED, ModTags.GREEN_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.RED_BED, ModRegistry.ENCHANTED_RED_BED, ModTags.RED_BED_APPLICABLE);
		ReplacementUtil.addReplacement(Blocks.BLACK_BED, ModRegistry.ENCHANTED_BLACK_BED, ModTags.BLACK_BED_APPLICABLE);
	}
}
