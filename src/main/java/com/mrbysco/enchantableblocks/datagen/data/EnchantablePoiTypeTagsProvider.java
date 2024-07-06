package com.mrbysco.enchantableblocks.datagen.data;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModPoiTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantablePoiTypeTagsProvider extends PoiTypeTagsProvider {
	public EnchantablePoiTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(pOutput, pProvider, EnchantableBlocks.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(PoiTypeTags.BEE_HOME).add(ModPoiTypes.ENCHANTED_BEEHIVE.getKey());
	}
}
