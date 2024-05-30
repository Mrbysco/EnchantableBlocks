package com.mrbysco.enchantableblocks.datagen.data;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantableEnchantmentTagsProvider extends TagsProvider<Enchantment> {
	public EnchantableEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, Registries.ENCHANTMENT, completableFuture, EnchantableBlocks.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ModTags.FURNACE_APPLICABLE).add(
				ModEnchantments.SPEED.getKey(),
				ModEnchantments.FUEL_EFFICIENCY.getKey(),
				ModEnchantments.YIELD.getKey(),
				ModEnchantments.PRESERVATION.getKey(),
				ModEnchantments.SOLAR_RADIANCE.getKey(),
				ModEnchantments.EXPORTING.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.BLAST_FURNACE_APPLICABLE).add(
				ModEnchantments.SPEED.getKey(),
				ModEnchantments.FUEL_EFFICIENCY.getKey(),
				ModEnchantments.YIELD.getKey(),
				ModEnchantments.PRESERVATION.getKey(),
				ModEnchantments.SOLAR_RADIANCE.getKey(),
				ModEnchantments.EXPORTING.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.SMOKER_APPLICABLE).add(
				ModEnchantments.SPEED.getKey(),
				ModEnchantments.FUEL_EFFICIENCY.getKey(),
				ModEnchantments.YIELD.getKey(),
				ModEnchantments.PRESERVATION.getKey(),
				ModEnchantments.SOLAR_RADIANCE.getKey(),
				ModEnchantments.EXPORTING.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.BEACON_APPLICABLE).add(
				ModEnchantments.RANGED.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.BED_APPLICABLE).add(
				ModEnchantments.INTENTIONAL_GAME_DESIGN.getKey(),
				ModEnchantments.WELL_RESTED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.WHITE_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.ORANGE_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.MAGENTA_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.LIGHT_BLUE_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.YELLOW_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.LIME_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.PINK_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.GRAY_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.LIGHT_GRAY_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.CYAN_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.PURPLE_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.BLUE_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.BROWN_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.GREEN_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.RED_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.BLACK_BED_APPLICABLE).addTag(ModTags.BED_APPLICABLE);
		this.tag(ModTags.RESPAWN_ANCHOR_APPLICABLE).add(
				ModEnchantments.INTENTIONAL_GAME_DESIGN.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLOCK_EFFICIENCY).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.CAMPFIRE_APPLICABLE).add(
				ModEnchantments.BOILING.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.SOUL_CAMPFIRE_APPLICABLE).add(
				ModEnchantments.BOILING.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.MAGMA_BLOCK_APPLICABLE).add(
				ModEnchantments.BOILING.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.DISPENSER_APPLICABLE).add(
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.FLAMING_ARROWS).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.INFINITY_ARROWS).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.POWER_ARROWS).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.PUNCH_ARROWS).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.HOPPER_APPLICABLE).add(
				ModEnchantments.SPEED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLOCK_EFFICIENCY).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.ENCHANTMENT_TABLE_APPLICABLE).add(
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLOCK_EFFICIENCY).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.CONDUIT_APPLICABLE).add(
				ModEnchantments.RANGED.getKey(),
				ModEnchantments.CONCEALED.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.SHARPNESS).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.CRAFTING_TABLE_APPLICABLE).add(
				ModEnchantments.PRESERVATION.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.BEEHIVE_APPLICABLE).add(
				ModEnchantments.SPEED.getKey(),
				ModEnchantments.STORING.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.CHEST_APPLICABLE).add(
				ModEnchantments.STORING.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));
		this.tag(ModTags.TRAPPED_CHEST_APPLICABLE).add(
				ModEnchantments.STORING.getKey(),
				ModEnchantments.GLINTLESS.getKey(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).orElseThrow(),
				BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BLAST_PROTECTION).orElseThrow()
		).addOptional(new ResourceLocation("stickerframes", "foiled"));


		/*
		 * Non block specific tags
		 */

		this.tag(ModTags.General.STORAGE_UPGRADE).add(
				ModEnchantments.STORING.getKey()
		).addOptional(new ResourceLocation("cofh_core", "holding"));
	}
}