package com.mrbysco.enchantableblocks.datagen.data;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantableItemTagsProvider extends ItemTagsProvider {
	public EnchantableItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTags, EnchantableBlocks.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ModTags.Items.YIELD_BLACKLIST);

		this.tag(ModTags.Items.SPEED_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER, Items.HOPPER,
				Items.BEEHIVE);
		this.tag(ModTags.Items.DAMAGE_ENCHANTABLE).add(Items.CONDUIT);
		this.tag(ModTags.Items.BLOCK_EFFICIENCY_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER, Items.HOPPER);
		this.tag(ModTags.Items.YIELD_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER);
		this.tag(ModTags.Items.PRESERVATION_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER,
				Items.ENCHANTING_TABLE);
		this.tag(ModTags.Items.SOLAR_RADIANCE_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER);
		this.tag(ModTags.Items.EXPORTING_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER);

		this.tag(ModTags.Items.RANGED_ENCHANTABLE).add(Items.BEACON, Items.CONDUIT);
		this.tag(ModTags.Items.INTENTIONAL_GAME_DESIGN_ENCHANTABLE).add(Items.WHITE_BED,
				Items.ORANGE_BED, Items.MAGENTA_BED, Items.LIGHT_BLUE_BED, Items.YELLOW_BED, Items.LIME_BED,
				Items.PINK_BED, Items.GRAY_BED, Items.LIGHT_GRAY_BED, Items.CYAN_BED, Items.PURPLE_BED, Items.BLUE_BED,
				Items.BROWN_BED, Items.GREEN_BED, Items.RED_BED, Items.BLACK_BED, Items.RESPAWN_ANCHOR);
		this.tag(ModTags.Items.WELL_RESTED_ENCHANTABLE).add(Items.WHITE_BED,
				Items.ORANGE_BED, Items.MAGENTA_BED, Items.LIGHT_BLUE_BED, Items.YELLOW_BED, Items.LIME_BED,
				Items.PINK_BED, Items.GRAY_BED, Items.LIGHT_GRAY_BED, Items.CYAN_BED, Items.PURPLE_BED, Items.BLUE_BED,
				Items.BROWN_BED, Items.GREEN_BED, Items.RED_BED, Items.BLACK_BED);
		this.tag(ModTags.Items.BOILING_ENCHANTABLE).add(Items.CAMPFIRE, Items.SOUL_CAMPFIRE, Items.MAGMA_BLOCK);
		this.tag(ModTags.Items.STORING_ENCHANTABLE).add(Items.BEEHIVE, Items.CHEST, Items.TRAPPED_CHEST);

		this.tag(ModTags.Items.CONCEALED_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER,
				Items.BEACON, Items.RESPAWN_ANCHOR, Items.CAMPFIRE, Items.SOUL_CAMPFIRE, Items.ENCHANTING_TABLE,
				Items.CONDUIT);
		this.tag(ModTags.Items.GLINTLESS_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER,
				Items.BEACON, Items.CAMPFIRE, Items.SOUL_CAMPFIRE, Items.MAGMA_BLOCK, Items.DISPENSER,
				Items.RESPAWN_ANCHOR, Items.HOPPER, Items.ENCHANTING_TABLE, Items.CONDUIT, Items.CRAFTING_TABLE,
				Items.BEEHIVE, Items.CHEST, Items.TRAPPED_CHEST, Items.WHITE_BED, Items.ORANGE_BED, Items.MAGENTA_BED,
				Items.LIGHT_BLUE_BED, Items.YELLOW_BED, Items.LIME_BED, Items.PINK_BED, Items.GRAY_BED,
				Items.LIGHT_GRAY_BED, Items.CYAN_BED, Items.PURPLE_BED, Items.BLUE_BED, Items.BROWN_BED,
				Items.GREEN_BED, Items.RED_BED, Items.BLACK_BED);
		this.tag(ModTags.Items.BLOCK_PROTECTION_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER,
				Items.BEACON, Items.CAMPFIRE, Items.SOUL_CAMPFIRE, Items.MAGMA_BLOCK, Items.DISPENSER,
				Items.RESPAWN_ANCHOR, Items.HOPPER, Items.ENCHANTING_TABLE, Items.CRAFTING_TABLE,
				Items.BEEHIVE, Items.CHEST, Items.TRAPPED_CHEST, Items.WHITE_BED, Items.ORANGE_BED, Items.MAGENTA_BED,
				Items.LIGHT_BLUE_BED, Items.YELLOW_BED, Items.LIME_BED, Items.PINK_BED, Items.GRAY_BED,
				Items.LIGHT_GRAY_BED, Items.CYAN_BED, Items.PURPLE_BED, Items.BLUE_BED, Items.BROWN_BED,
				Items.GREEN_BED, Items.RED_BED, Items.BLACK_BED);


		this.tag(ItemTags.VANISHING_ENCHANTABLE).add(Items.FURNACE, Items.BLAST_FURNACE, Items.SMOKER,
				Items.BEACON, Items.CAMPFIRE, Items.SOUL_CAMPFIRE, Items.MAGMA_BLOCK, Items.DISPENSER,
				Items.RESPAWN_ANCHOR, Items.HOPPER, Items.ENCHANTING_TABLE, Items.CONDUIT, Items.CRAFTING_TABLE,
				Items.BEEHIVE, Items.CHEST, Items.TRAPPED_CHEST, Items.WHITE_BED, Items.ORANGE_BED, Items.MAGENTA_BED,
				Items.LIGHT_BLUE_BED, Items.YELLOW_BED, Items.LIME_BED, Items.PINK_BED, Items.GRAY_BED,
				Items.LIGHT_GRAY_BED, Items.CYAN_BED, Items.PURPLE_BED, Items.BLUE_BED, Items.BROWN_BED,
				Items.GREEN_BED, Items.RED_BED, Items.BLACK_BED);
		this.tag(ItemTags.BOW_ENCHANTABLE).add(Items.DISPENSER);
	}
}