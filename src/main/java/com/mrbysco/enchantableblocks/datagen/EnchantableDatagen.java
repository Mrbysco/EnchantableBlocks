package com.mrbysco.enchantableblocks.datagen;

import com.mrbysco.enchantableblocks.datagen.assets.EnchantableBlockstateProvider;
import com.mrbysco.enchantableblocks.datagen.assets.EnchantableLanguageProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableBlockTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableEnchantmentTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantableDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new EnchantableLootProvider(packOutput));
			generator.addProvider(true, new EnchantableBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new EnchantableLanguageProvider(packOutput));
			generator.addProvider(true, new EnchantableBlockstateProvider(packOutput, existingFileHelper));
			generator.addProvider(true, new EnchantableEnchantmentTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		}
	}
}
