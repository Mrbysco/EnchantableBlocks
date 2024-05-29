package com.mrbysco.enchantableblocks.datagen;

import com.mrbysco.enchantableblocks.datagen.assets.EnchantableBlockstateProvider;
import com.mrbysco.enchantableblocks.datagen.assets.EnchantableLanguageProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableBlockTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableEnchantmentTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableItemTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
			BlockTagsProvider blockTagProvider;
			generator.addProvider(true, blockTagProvider = new EnchantableBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
			generator.addProvider(true, new EnchantableItemTagsProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new EnchantableLanguageProvider(packOutput));
			generator.addProvider(true, new EnchantableBlockstateProvider(packOutput, existingFileHelper));
			generator.addProvider(true, new EnchantableEnchantmentTagsProvider(packOutput, event.getLookupProvider(), existingFileHelper));
		}
	}
}
