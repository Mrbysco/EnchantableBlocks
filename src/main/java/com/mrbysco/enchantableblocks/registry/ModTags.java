package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModTags {
	public static class General {
		public static final TagKey<Enchantment> STORAGE_UPGRADE = modTag("storage_upgrade");
	}

	public static class Items {
		public static final TagKey<Item> DAMAGE_ENCHANTABLE = modTag("damage_enchantable");
		public static final TagKey<Item> SPEED_ENCHANTABLE = modTag("speed_enchantable");
		public static final TagKey<Item> BLOCK_EFFICIENCY_ENCHANTABLE = modTag("block_efficiency_enchantable");
		public static final TagKey<Item> YIELD_ENCHANTABLE = modTag("yield_enchantable");
		public static final TagKey<Item> PRESERVATION_ENCHANTABLE = modTag("preservation_enchantable");
		public static final TagKey<Item> SOLAR_RADIANCE_ENCHANTABLE = modTag("solar_radiance_enchantable");
		public static final TagKey<Item> EXPORTING_ENCHANTABLE = modTag("exporting_enchantable");
		public static final TagKey<Item> CONCEALED_ENCHANTABLE = modTag("concealed_enchantable");
		public static final TagKey<Item> GLINTLESS_ENCHANTABLE = modTag("glintless_enchantable");
		public static final TagKey<Item> RANGED_ENCHANTABLE = modTag("ranged_enchantable");
		public static final TagKey<Item> INTENTIONAL_GAME_DESIGN_ENCHANTABLE = modTag("intentional_game_design_enchantable");
		public static final TagKey<Item> BOILING_ENCHANTABLE = modTag("boiling_enchantable");
		public static final TagKey<Item> WELL_RESTED_ENCHANTABLE = modTag("well_rested_enchantable");
		public static final TagKey<Item> BLOCK_PROTECTION_ENCHANTABLE = modTag("block_protection_enchantable");
		public static final TagKey<Item> STORING_ENCHANTABLE = modTag("storing_enchantable");

		public static final TagKey<Item> YIELD_BLACKLIST = modTag("yield_blacklist");

		private static TagKey<Item> modTag(String path) {
			return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(EnchantableBlocks.MOD_ID, path));
		}
	}

	private static TagKey<Enchantment> modTag(String path) {
		return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(EnchantableBlocks.MOD_ID, path));
	}
}
