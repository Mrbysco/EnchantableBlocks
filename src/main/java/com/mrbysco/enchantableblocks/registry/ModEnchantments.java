package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, EnchantableBlocks.MOD_ID);

	public static final ResourceKey<Enchantment> DAMAGE = key("damage");
	public static final ResourceKey<Enchantment> SPEED = key("speed");
	public static final ResourceKey<Enchantment> BLOCK_EFFICIENCY = key("block_efficiency");
	public static final ResourceKey<Enchantment> YIELD = key("yield");
	public static final ResourceKey<Enchantment> PRESERVATION = key("preservation");
	public static final ResourceKey<Enchantment> SOLAR_RADIANCE = key("solar_radiance");
	public static final ResourceKey<Enchantment> EXPORTING = key("exporting");
	public static final ResourceKey<Enchantment> CONCEALED = key("concealed");
	public static final ResourceKey<Enchantment> GLINTLESS = key("glintless");
	public static final ResourceKey<Enchantment> RANGED = key("ranged");
	public static final ResourceKey<Enchantment> INTENTIONAL_GAME_DESIGN = key("intentional_game_design");
	public static final ResourceKey<Enchantment> BOILING = key("boiling");
	public static final ResourceKey<Enchantment> WELL_RESTED = key("well_rested");
	public static final ResourceKey<Enchantment> STORING = key("storing");
	public static final ResourceKey<Enchantment> BLOCK_PROTECTION = key("block_protection");

	private static ResourceKey<Enchantment> key(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(EnchantableBlocks.MOD_ID, path));
	}

	public static void bootstrap(BootstrapContext<Enchantment> context) {
		HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

		register(
				context,
				DAMAGE,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.DAMAGE_ENCHANTABLE),
								5,
								3,
								Enchantment.dynamicCost(5, 8),
								Enchantment.dynamicCost(55, 8),
								2,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				SPEED,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.SPEED_ENCHANTABLE),
								5,
								3,
								Enchantment.dynamicCost(5, 8),
								Enchantment.dynamicCost(55, 8),
								2,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				BLOCK_EFFICIENCY,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.BLOCK_EFFICIENCY_ENCHANTABLE),
								10,
								5,
								Enchantment.dynamicCost(1, 10),
								Enchantment.dynamicCost(51, 10),
								1,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				YIELD,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.YIELD_ENCHANTABLE),
								2,
								3,
								Enchantment.dynamicCost(15, 9),
								Enchantment.dynamicCost(65, 9),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				PRESERVATION,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.PRESERVATION_ENCHANTABLE),
								1,
								1,
								Enchantment.constantCost(15),
								Enchantment.constantCost(65),
								8,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				SOLAR_RADIANCE,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.SOLAR_RADIANCE_ENCHANTABLE),
								5,
								2,
								Enchantment.constantCost(30),
								Enchantment.dynamicCost(55, 20),
								2,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				EXPORTING,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.EXPORTING_ENCHANTABLE),
								2,
								1,
								Enchantment.constantCost(20),
								Enchantment.constantCost(55),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				CONCEALED,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.CONCEALED_ENCHANTABLE),
								2,
								1,
								Enchantment.constantCost(1),
								Enchantment.constantCost(41),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				GLINTLESS,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.GLINTLESS_ENCHANTABLE),
								2,
								3,
								Enchantment.dynamicCost(10, 10),
								Enchantment.dynamicCost(40, 10),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				RANGED,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.RANGED_ENCHANTABLE),
								2,
								2,
								Enchantment.dynamicCost(12, 20),
								Enchantment.dynamicCost(37, 20),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				INTENTIONAL_GAME_DESIGN,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.INTENTIONAL_GAME_DESIGN_ENCHANTABLE),
								2,
								2,
								Enchantment.constantCost(10),
								Enchantment.dynamicCost(10, 40),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				BOILING,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.BOILING_ENCHANTABLE),
								5,
								3,
								Enchantment.dynamicCost(5, 8),
								Enchantment.dynamicCost(55, 8),
								2,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				WELL_RESTED,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.WELL_RESTED_ENCHANTABLE),
								5,
								3,
								Enchantment.dynamicCost(5, 8),
								Enchantment.dynamicCost(55, 8),
								2,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				STORING,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.WELL_RESTED_ENCHANTABLE),
								10,
								5,
								Enchantment.dynamicCost(1, 10),
								Enchantment.dynamicCost(51, 10),
								1,
								EquipmentSlotGroup.ANY
						)
				)
		);

		register(
				context,
				BLOCK_PROTECTION,
				Enchantment.enchantment(
						Enchantment.definition(
								itemHolderGetter.getOrThrow(ModTags.Items.BLOCK_PROTECTION_ENCHANTABLE),
								2,
								4,
								Enchantment.dynamicCost(5, 8),
								Enchantment.dynamicCost(13, 8),
								4,
								EquipmentSlotGroup.ANY
						)
				)
		);
	}

	private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> resourceKey,
	                             Enchantment.Builder builder) {
		context.register(resourceKey, builder.build(resourceKey.location()));
	}
}
