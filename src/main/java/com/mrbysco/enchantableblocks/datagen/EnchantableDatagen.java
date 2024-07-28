package com.mrbysco.enchantableblocks.datagen;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.datagen.assets.EnchantableBlockstateProvider;
import com.mrbysco.enchantableblocks.datagen.assets.EnchantableLanguageProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableBlockTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableEnchantmentTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableItemTagsProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantableLootProvider;
import com.mrbysco.enchantableblocks.datagen.data.EnchantablePoiTypeTagsProvider;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class EnchantableDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = CompletableFuture.supplyAsync(() -> EnchantableDatagen.getProvider().full());

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
					packOutput, CompletableFuture.supplyAsync(EnchantableDatagen::getProvider), Set.of(EnchantableBlocks.MOD_ID)));

			generator.addProvider(true, new EnchantableLootProvider(packOutput, lookupProvider));
			BlockTagsProvider blockTagProvider;
			generator.addProvider(true, blockTagProvider = new EnchantableBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
			generator.addProvider(true, new EnchantableItemTagsProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
			generator.addProvider(true, new EnchantablePoiTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
			generator.addProvider(true, new EnchantableEnchantmentTagsProvider(packOutput, lookupProvider, existingFileHelper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new EnchantableLanguageProvider(packOutput));
			generator.addProvider(true, new EnchantableBlockstateProvider(packOutput, existingFileHelper));
		}
	}

	private static RegistrySetBuilder.PatchedRegistries getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}
}
