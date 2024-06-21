package com.mrbysco.enchantableblocks.datagen.assets;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnchantableLanguageProvider extends LanguageProvider {
	public EnchantableLanguageProvider(PackOutput packOutput) {
		super(packOutput, EnchantableBlocks.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addBlock(ModRegistry.ENCHANTED_FURNACE, "Furnace");
		addBlock(ModRegistry.ENCHANTED_BLAST_FURNACE, "Blast Furnace");
		addBlock(ModRegistry.ENCHANTED_SMOKER, "Smoker");
		addBlock(ModRegistry.ENCHANTED_BEACON, "Beacon");
		addBlock(ModRegistry.ENCHANTED_CAMPFIRE, "Campfire");
		addBlock(ModRegistry.ENCHANTED_SOUL_CAMPFIRE, "Soul Campfire");
		addBlock(ModRegistry.ENCHANTED_MAGMA_BLOCK, "Magma Block");
		addBlock(ModRegistry.ENCHANTED_DISPENSER, "Dispenser");
		addBlock(ModRegistry.ENCHANTED_RESPAWN_ANCHOR, "Respawn Anchor");
		addBlock(ModRegistry.ENCHANTED_HOPPER, "Hopper");
		addBlock(ModRegistry.ENCHANTED_ENCHANTING_TABLE, "Enchanting Table");
		addBlock(ModRegistry.ENCHANTED_CONDUIT, "Conduit");
		addBlock(ModRegistry.ENCHANTED_CRAFTING_TABLE, "Crafting Table");
		addBlock(ModRegistry.ENCHANTED_BEEHIVE, "Beehive");
		addBlock(ModRegistry.ENCHANTED_CHEST, "Chest");
		addBlock(ModRegistry.ENCHANTED_TRAPPED_CHEST, "Trapped Chest");

		addBlock(ModRegistry.ENCHANTED_WHITE_BED, "White Bed");
		addBlock(ModRegistry.ENCHANTED_ORANGE_BED, "Orange Bed");
		addBlock(ModRegistry.ENCHANTED_MAGENTA_BED, "Magenta Bed");
		addBlock(ModRegistry.ENCHANTED_LIGHT_BLUE_BED, "Light Blue Bed");
		addBlock(ModRegistry.ENCHANTED_YELLOW_BED, "Yellow Bed");
		addBlock(ModRegistry.ENCHANTED_LIME_BED, "Lime Bed");
		addBlock(ModRegistry.ENCHANTED_PINK_BED, "Pink Bed");
		addBlock(ModRegistry.ENCHANTED_GRAY_BED, "Gray Bed");
		addBlock(ModRegistry.ENCHANTED_LIGHT_GRAY_BED, "Light Gray Bed");
		addBlock(ModRegistry.ENCHANTED_CYAN_BED, "Cyan Bed");
		addBlock(ModRegistry.ENCHANTED_PURPLE_BED, "Purple Bed");
		addBlock(ModRegistry.ENCHANTED_BLUE_BED, "Blue Bed");
		addBlock(ModRegistry.ENCHANTED_BROWN_BED, "Brown Bed");
		addBlock(ModRegistry.ENCHANTED_GREEN_BED, "Green Bed");
		addBlock(ModRegistry.ENCHANTED_RED_BED, "Red Bed");
		addBlock(ModRegistry.ENCHANTED_BLACK_BED, "Black Bed");

		addEnchantment(ModEnchantments.SPEED, "Speed");
		addEnchantmentDescription(ModEnchantments.SPEED, "Increases the speed at which functions are performed");

		addEnchantment(ModEnchantments.BLOCK_EFFICIENCY, "Block Efficiency");
		addEnchantmentDescription(ModEnchantments.BLOCK_EFFICIENCY, "Increases the efficiency of the block");

		addEnchantment(ModEnchantments.YIELD, "Yield");
		addEnchantmentDescription(ModEnchantments.YIELD, "Adds a chance of increased results");

		addEnchantment(ModEnchantments.PRESERVATION, "Preservation");
		addEnchantmentDescription(ModEnchantments.PRESERVATION, "Preserves the resources used in the block");

		addEnchantment(ModEnchantments.SOLAR_RADIANCE, "Solar Radiance");
		addEnchantmentDescription(ModEnchantments.SOLAR_RADIANCE, "Harnesses the power of the sun");

		addEnchantment(ModEnchantments.EXPORTING, "Exporting");
		addEnchantmentDescription(ModEnchantments.EXPORTING, "Auto exports results from the block");

		addEnchantment(ModEnchantments.CONCEALED, "Concealed");
		addEnchantmentDescription(ModEnchantments.CONCEALED, "Conceals external visuals produced by the block");

		addEnchantment(ModEnchantments.GLINTLESS, "Glintless");
		addEnchantmentDescription(ModEnchantments.GLINTLESS, "Removes the enchantment glint from the block");

		addEnchantment(ModEnchantments.RANGED, "Ranged");
		addEnchantmentDescription(ModEnchantments.RANGED, "Increases the range of the block");

		addEnchantment(ModEnchantments.INTENTIONAL_GAME_DESIGN, "Intentional Game Design");
		addEnchantmentDescription(ModEnchantments.INTENTIONAL_GAME_DESIGN, "The functionality of this enchantment is intentional");

		addEnchantment(ModEnchantments.BOILING, "Boiling");
		addEnchantmentDescription(ModEnchantments.BOILING, "Increases the temperature of the block");

		addEnchantment(ModEnchantments.WELL_RESTED, "Well Rested");
		addEnchantmentDescription(ModEnchantments.WELL_RESTED, "Gives the player a reward for sleeping in the bed");

		addEnchantment(ModEnchantments.STORING, "Storing");
		addEnchantmentDescription(ModEnchantments.STORING, "Increases the storage capacity of the block");

		addEnchantment(ModEnchantments.DAMAGE, "Damage");
		addEnchantmentDescription(ModEnchantments.DAMAGE, "Increases the damage dealt by the block");

		addEnchantment(ModEnchantments.BLOCK_PROTECTION, "Block Protection");
		addEnchantmentDescription(ModEnchantments.BLOCK_PROTECTION, "Protects the block from external damage");

		add("config.jade.plugin_enchantableblocks.enchantments", "Enchantments");
	}

	private void addEnchantment(ResourceKey<Enchantment> key, String name) {
		ResourceLocation location = key.location();
		add("enchantment." + location.getNamespace() + "." + location.getPath(), name);
	}

	private void addEnchantmentDescription(ResourceKey<Enchantment> key, String description) {
		ResourceLocation location = key.location();
		add("enchantment." + location.getNamespace() + "." + location.getPath() + ".desc", description);
	}
}