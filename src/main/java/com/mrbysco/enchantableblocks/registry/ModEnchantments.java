package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.enchantment.BoilingEnchantment;
import com.mrbysco.enchantableblocks.enchantment.ConcealedEnchantment;
import com.mrbysco.enchantableblocks.enchantment.ExportingEnchantment;
import com.mrbysco.enchantableblocks.enchantment.FuelEfficiencyEnchantment;
import com.mrbysco.enchantableblocks.enchantment.GlintlessEnchantment;
import com.mrbysco.enchantableblocks.enchantment.IntentionalDesignEnchantment;
import com.mrbysco.enchantableblocks.enchantment.PreservationEnchantment;
import com.mrbysco.enchantableblocks.enchantment.RangedEnchantment;
import com.mrbysco.enchantableblocks.enchantment.SmeltSpeedEnchantment;
import com.mrbysco.enchantableblocks.enchantment.SolarRadianceEnchantment;
import com.mrbysco.enchantableblocks.enchantment.StoringEnchantment;
import com.mrbysco.enchantableblocks.enchantment.WellRestedEnchantment;
import com.mrbysco.enchantableblocks.enchantment.YieldEnchantment;
import com.mrbysco.enchantableblocks.util.ReplacementUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.BedBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, EnchantableBlocks.MOD_ID);

	public static final EnchantmentCategory BLOCK_CATEGORY = EnchantmentCategory.create(EnchantableBlocks.MOD_ID + ":blocks", item -> {
		if (item instanceof BlockItem blockItem) {
			if (ReplacementUtil.getReplacement(blockItem.getBlock()) != null) {
				return true;
			}
		}
		return false;
	});

	public static final EnchantmentCategory BED_CATEGORY = EnchantmentCategory.create(EnchantableBlocks.MOD_ID + ":blocks", item -> {
		if (item instanceof BlockItem blockItem) {
			if (blockItem.getBlock() instanceof BedBlock && ReplacementUtil.getReplacement(blockItem.getBlock()) != null) {
				return true;
			}
		}
		return false;
	});

	public static final DeferredHolder<Enchantment, Enchantment> SPEED = ENCHANTMENTS.register("speed", () -> new SmeltSpeedEnchantment(Enchantment.Rarity.UNCOMMON, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> FUEL_EFFICIENCY = ENCHANTMENTS.register("fuel_efficiency", () -> new FuelEfficiencyEnchantment(Enchantment.Rarity.UNCOMMON, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> YIELD = ENCHANTMENTS.register("yield", () -> new YieldEnchantment(Enchantment.Rarity.COMMON, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> PRESERVATION = ENCHANTMENTS.register("preservation", () -> new PreservationEnchantment(Enchantment.Rarity.RARE, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> SOLAR_RADIANCE = ENCHANTMENTS.register("solar_radiance", () -> new SolarRadianceEnchantment(Enchantment.Rarity.VERY_RARE, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> EXPORTING = ENCHANTMENTS.register("exporting", () -> new ExportingEnchantment(Enchantment.Rarity.RARE, BED_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> CONCEALED = ENCHANTMENTS.register("concealed", () -> new ConcealedEnchantment(Enchantment.Rarity.COMMON, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> GLINTLESS = ENCHANTMENTS.register("glintless", () -> new GlintlessEnchantment(Enchantment.Rarity.COMMON, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> RANGED = ENCHANTMENTS.register("ranged", () -> new RangedEnchantment(Enchantment.Rarity.VERY_RARE, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> INTENTIONAL_GAME_DESIGN = ENCHANTMENTS.register("intentional_game_design", () -> new IntentionalDesignEnchantment(Enchantment.Rarity.RARE, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> BOILING = ENCHANTMENTS.register("boiling", () -> new BoilingEnchantment(Enchantment.Rarity.UNCOMMON, BLOCK_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> WELL_RESTED = ENCHANTMENTS.register("well_rested", () -> new WellRestedEnchantment(Enchantment.Rarity.RARE, BED_CATEGORY, EquipmentSlot.MAINHAND));
	public static final DeferredHolder<Enchantment, Enchantment> STORING = ENCHANTMENTS.register("storing", () -> new StoringEnchantment(Enchantment.Rarity.RARE, BED_CATEGORY, EquipmentSlot.MAINHAND));

}
