package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModTags {
	public static final TagKey<Enchantment> FURNACE_APPLICABLE = modTag("applicable/furnace");
	public static final TagKey<Enchantment> BLAST_FURNACE_APPLICABLE = modTag("applicable/blast_furnace");
	public static final TagKey<Enchantment> SMOKER_APPLICABLE = modTag("applicable/smoker");
	public static final TagKey<Enchantment> BEACON_APPLICABLE = modTag("applicable/beacon");
	public static final TagKey<Enchantment> RESPAWN_ANCHOR_APPLICABLE = modTag("applicable/respawn_anchor");
	public static final TagKey<Enchantment> CAMPFIRE_APPLICABLE = modTag("applicable/campfire");
	public static final TagKey<Enchantment> SOUL_CAMPFIRE_APPLICABLE = modTag("applicable/soul_campfire");
	public static final TagKey<Enchantment> MAGMA_BLOCK_APPLICABLE = modTag("applicable/magma_block");
	public static final TagKey<Enchantment> DISPENSER_APPLICABLE = modTag("applicable/dispenser");
	public static final TagKey<Enchantment> HOPPER_APPLICABLE = modTag("applicable/hopper");
	public static final TagKey<Enchantment> ENCHANTMENT_TABLE_APPLICABLE = modTag("applicable/enchantment_table");
	public static final TagKey<Enchantment> CONDUIT_APPLICABLE = modTag("applicable/conduit");
	public static final TagKey<Enchantment> CRAFTING_TABLE_APPLICABLE = modTag("applicable/crafting_table");
	public static final TagKey<Enchantment> BEEHIVE_APPLICABLE = modTag("applicable/beehive");
	public static final TagKey<Enchantment> CHEST_APPLICABLE = modTag("applicable/chest");
	public static final TagKey<Enchantment> TRAPPED_CHEST_APPLICABLE = modTag("applicable/trapped_chest");

	public static final TagKey<Enchantment> BED_APPLICABLE = modTag("applicable/bed");
	public static final TagKey<Enchantment> WHITE_BED_APPLICABLE = modTag("applicable/white_bed");
	public static final TagKey<Enchantment> ORANGE_BED_APPLICABLE = modTag("applicable/orange_bed");
	public static final TagKey<Enchantment> MAGENTA_BED_APPLICABLE = modTag("applicable/magenta_bed");
	public static final TagKey<Enchantment> LIGHT_BLUE_BED_APPLICABLE = modTag("applicable/light_blue_bed");
	public static final TagKey<Enchantment> YELLOW_BED_APPLICABLE = modTag("applicable/yellow_bed");
	public static final TagKey<Enchantment> LIME_BED_APPLICABLE = modTag("applicable/lime_bed");
	public static final TagKey<Enchantment> PINK_BED_APPLICABLE = modTag("applicable/pink_bed");
	public static final TagKey<Enchantment> GRAY_BED_APPLICABLE = modTag("applicable/gray_bed");
	public static final TagKey<Enchantment> LIGHT_GRAY_BED_APPLICABLE = modTag("applicable/light_gray_bed");
	public static final TagKey<Enchantment> CYAN_BED_APPLICABLE = modTag("applicable/cyan_bed");
	public static final TagKey<Enchantment> PURPLE_BED_APPLICABLE = modTag("applicable/purple_bed");
	public static final TagKey<Enchantment> BLUE_BED_APPLICABLE = modTag("applicable/blue_bed");
	public static final TagKey<Enchantment> BROWN_BED_APPLICABLE = modTag("applicable/brown_bed");
	public static final TagKey<Enchantment> GREEN_BED_APPLICABLE = modTag("applicable/green_bed");
	public static final TagKey<Enchantment> RED_BED_APPLICABLE = modTag("applicable/red_bed");
	public static final TagKey<Enchantment> BLACK_BED_APPLICABLE = modTag("applicable/black_bed");

	public static class General {
		public static final TagKey<Enchantment> STORAGE_UPGRADE = modTag("storage_upgrade");

	}

	public static class Items {
		public static final TagKey<Item> YIELD_BLACKLIST = TagKey.create(Registries.ITEM, new ResourceLocation(EnchantableBlocks.MOD_ID, "yield_blacklist"));

	}

	private static TagKey<Enchantment> modTag(String path) {
		return TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(EnchantableBlocks.MOD_ID, path));
	}
}
