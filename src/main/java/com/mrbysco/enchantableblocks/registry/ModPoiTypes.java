package com.mrbysco.enchantableblocks.registry;

import com.google.common.collect.ImmutableSet;
import com.mrbysco.enchantableblocks.EnchantableBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPoiTypes {
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, EnchantableBlocks.MOD_ID);

	public static DeferredHolder<PoiType, PoiType> ENCHANTED_BEEHIVE = POI_TYPES.register("enchanted_beehive", () -> new PoiType(ImmutableSet.copyOf(ModRegistry.ENCHANTED_BEEHIVE.get().getStateDefinition().getPossibleStates()), 0, 1));
}
