package com.mrbysco.enchantableblocks.datagen.data;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class EnchantableBlockTagsProvider extends BlockTagsProvider {
	public EnchantableBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, EnchantableBlocks.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
				ModRegistry.ENCHANTED_CRAFTING_TABLE.get(),
				ModRegistry.ENCHANTED_BEEHIVE.get(),
				ModRegistry.ENCHANTED_CHEST.get(),
				ModRegistry.ENCHANTED_TRAPPED_CHEST.get()
		);
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				ModRegistry.ENCHANTED_FURNACE.get(),
				ModRegistry.ENCHANTED_BLAST_FURNACE.get(),
				ModRegistry.ENCHANTED_SMOKER.get(),
				ModRegistry.ENCHANTED_MAGMA_BLOCK.get(),
				ModRegistry.ENCHANTED_DISPENSER.get(),
				ModRegistry.ENCHANTED_RESPAWN_ANCHOR.get(),
				ModRegistry.ENCHANTED_HOPPER.get(),
				ModRegistry.ENCHANTED_ENCHANTING_TABLE.get(),
				ModRegistry.ENCHANTED_CONDUIT.get()
		);
		this.tag(BlockTags.BEEHIVES).add(ModRegistry.ENCHANTED_BEEHIVE.get());
		this.tag(BlockTags.BEDS).add(
				ModRegistry.ENCHANTED_WHITE_BED.get(), ModRegistry.ENCHANTED_ORANGE_BED.get(),
				ModRegistry.ENCHANTED_MAGENTA_BED.get(), ModRegistry.ENCHANTED_LIGHT_BLUE_BED.get(),
				ModRegistry.ENCHANTED_YELLOW_BED.get(), ModRegistry.ENCHANTED_LIME_BED.get(),
				ModRegistry.ENCHANTED_PINK_BED.get(), ModRegistry.ENCHANTED_GRAY_BED.get(),
				ModRegistry.ENCHANTED_LIGHT_GRAY_BED.get(), ModRegistry.ENCHANTED_CYAN_BED.get(),
				ModRegistry.ENCHANTED_PURPLE_BED.get(), ModRegistry.ENCHANTED_BLUE_BED.get(),
				ModRegistry.ENCHANTED_BROWN_BED.get(), ModRegistry.ENCHANTED_GREEN_BED.get(),
				ModRegistry.ENCHANTED_RED_BED.get(), ModRegistry.ENCHANTED_BLACK_BED.get()
		);

		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(ModRegistry.ENCHANTED_CHEST.get(), ModRegistry.ENCHANTED_TRAPPED_CHEST.get());
		this.tag(Tags.Blocks.CHESTS_TRAPPED).add(ModRegistry.ENCHANTED_TRAPPED_CHEST.get());
		this.tag(Tags.Blocks.CHESTS_WOODEN).add(ModRegistry.ENCHANTED_CHEST.get(), ModRegistry.ENCHANTED_TRAPPED_CHEST.get());

		this.tag(BlockTags.DRAGON_IMMUNE).add(ModRegistry.ENCHANTED_RESPAWN_ANCHOR.get());
		this.tag(BlockTags.HOGLIN_REPELLENTS).add(ModRegistry.ENCHANTED_RESPAWN_ANCHOR.get());
		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ModRegistry.ENCHANTED_RESPAWN_ANCHOR.get());

		this.tag(BlockTags.PIGLIN_REPELLENTS).add(ModRegistry.ENCHANTED_SOUL_CAMPFIRE.get());
		this.tag(BlockTags.CAMPFIRES).add(ModRegistry.ENCHANTED_CAMPFIRE.get(), ModRegistry.ENCHANTED_SOUL_CAMPFIRE.get());
		this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModRegistry.ENCHANTED_CAMPFIRE.get(), ModRegistry.ENCHANTED_SOUL_CAMPFIRE.get());

		this.tag(BlockTags.INFINIBURN_OVERWORLD).add(ModRegistry.ENCHANTED_MAGMA_BLOCK.get());
	}
}