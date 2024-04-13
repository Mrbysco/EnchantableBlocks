package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.lootfunctions.CopyEnchantmentsFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModLootFunctions {
	public static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, EnchantableBlocks.MOD_ID);

	public static final RegistryObject<LootItemFunctionType> COPY_ENCHANTMENTS = LOOT_ITEM_FUNCTIONS.register("copy_enchantments", () ->
			new LootItemFunctionType(new CopyEnchantmentsFunction.Serializer()));

}
