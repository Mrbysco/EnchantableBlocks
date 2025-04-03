package com.mrbysco.enchantableblocks.datagen.data;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModTags;
import com.mrbysco.enchantableblocks.registry.ModTags.General;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantableEnchantmentTagsProvider extends EnchantmentTagsProvider {

	public EnchantableEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
	                                          @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, EnchantableBlocks.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(EnchantmentTags.CURSE).add(
				ModEnchantments.INTENTIONAL_GAME_DESIGN
		);
		this.tag(EnchantmentTags.TREASURE).add(ModEnchantments.INTENTIONAL_GAME_DESIGN);
		this.tag(EnchantmentTags.NON_TREASURE).add(
				ModEnchantments.DAMAGE, ModEnchantments.SPEED,
				ModEnchantments.BLOCK_EFFICIENCY, ModEnchantments.YIELD,
				ModEnchantments.PRESERVATION, ModEnchantments.SOLAR_RADIANCE,
				ModEnchantments.EXPORTING, ModEnchantments.CONCEALED,
				ModEnchantments.GLINTLESS, ModEnchantments.RANGED,
				ModEnchantments.BOILING, ModEnchantments.WELL_RESTED,
				ModEnchantments.STORING, ModEnchantments.BLOCK_PROTECTION
		);

		this.tag(General.STORAGE_UPGRADE)
				.add(ModEnchantments.STORING);
	}
}
