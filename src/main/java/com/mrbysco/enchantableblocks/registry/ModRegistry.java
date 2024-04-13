package com.mrbysco.enchantableblocks.registry;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.block.EnchantedBeaconBlock;
import com.mrbysco.enchantableblocks.block.EnchantedBedBlock;
import com.mrbysco.enchantableblocks.block.EnchantedBeehiveBlock;
import com.mrbysco.enchantableblocks.block.EnchantedBlastFurnaceBlock;
import com.mrbysco.enchantableblocks.block.EnchantedCampfireBlock;
import com.mrbysco.enchantableblocks.block.EnchantedChestBlock;
import com.mrbysco.enchantableblocks.block.EnchantedConduitBlock;
import com.mrbysco.enchantableblocks.block.EnchantedCraftingTableBlock;
import com.mrbysco.enchantableblocks.block.EnchantedDispenserBlock;
import com.mrbysco.enchantableblocks.block.EnchantedEnchantmentTableBlock;
import com.mrbysco.enchantableblocks.block.EnchantedFurnaceBlock;
import com.mrbysco.enchantableblocks.block.EnchantedHopperBlock;
import com.mrbysco.enchantableblocks.block.EnchantedMagmaBlock;
import com.mrbysco.enchantableblocks.block.EnchantedRespawnAnchorBlock;
import com.mrbysco.enchantableblocks.block.EnchantedSmokerBlock;
import com.mrbysco.enchantableblocks.block.EnchantedTrappedChestBlock;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBeaconBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBedBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBeehiveBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCampfireBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedChestBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedConduitBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedCraftingTableBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedDispenserBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedEnchantmentTableBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedHopperBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.EnchantedTrappedChestBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.furnace.EnchantedBlastFurnaceBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.furnace.EnchantedFurnaceBlockEntity;
import com.mrbysco.enchantableblocks.block.blockentity.furnace.EnchantedSmokerBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnchantableBlocks.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnchantableBlocks.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EnchantableBlocks.MOD_ID);

	public static final RegistryObject<Block> ENCHANTED_FURNACE = BLOCKS.register("enchanted_furnace", () -> new EnchantedFurnaceBlock(Block.Properties.copy(Blocks.FURNACE)));
	public static final RegistryObject<Block> ENCHANTED_BLAST_FURNACE = BLOCKS.register("enchanted_blast_furnace", () -> new EnchantedBlastFurnaceBlock(Block.Properties.copy(Blocks.BLAST_FURNACE)));
	public static final RegistryObject<Block> ENCHANTED_SMOKER = BLOCKS.register("enchanted_smoker", () -> new EnchantedSmokerBlock(Block.Properties.copy(Blocks.SMOKER)));
	public static final RegistryObject<Block> ENCHANTED_BEACON = BLOCKS.register("enchanted_beacon", () -> new EnchantedBeaconBlock(Block.Properties.copy(Blocks.BEACON)));
	public static final RegistryObject<Block> ENCHANTED_CAMPFIRE = BLOCKS.register("enchanted_campfire", () -> new EnchantedCampfireBlock(true, 1, () -> Blocks.CAMPFIRE, BlockBehaviour.Properties.copy(Blocks.CAMPFIRE)));
	public static final RegistryObject<Block> ENCHANTED_SOUL_CAMPFIRE = BLOCKS.register("enchanted_soul_campfire", () -> new EnchantedCampfireBlock(false, 2, () -> Blocks.SOUL_CAMPFIRE, BlockBehaviour.Properties.copy(Blocks.SOUL_CAMPFIRE)));
	public static final RegistryObject<Block> ENCHANTED_MAGMA_BLOCK = BLOCKS.register("enchanted_magma_block", () -> new EnchantedMagmaBlock(BlockBehaviour.Properties.copy(Blocks.MAGMA_BLOCK)));
	public static final RegistryObject<Block> ENCHANTED_DISPENSER = BLOCKS.register("enchanted_dispenser", () -> new EnchantedDispenserBlock(BlockBehaviour.Properties.copy(Blocks.DISPENSER)));
	public static final RegistryObject<Block> ENCHANTED_RESPAWN_ANCHOR = BLOCKS.register("enchanted_respawn_anchor", () -> new EnchantedRespawnAnchorBlock(BlockBehaviour.Properties.copy(Blocks.RESPAWN_ANCHOR)));
	public static final RegistryObject<Block> ENCHANTED_HOPPER = BLOCKS.register("enchanted_hopper", () -> new EnchantedHopperBlock(BlockBehaviour.Properties.copy(Blocks.HOPPER)));
	public static final RegistryObject<Block> ENCHANTED_ENCHANTING_TABLE = BLOCKS.register("enchanted_enchanting_table", () -> new EnchantedEnchantmentTableBlock(BlockBehaviour.Properties.copy(Blocks.ENCHANTING_TABLE)));
	public static final RegistryObject<Block> ENCHANTED_CONDUIT = BLOCKS.register("enchanted_conduit", () -> new EnchantedConduitBlock(BlockBehaviour.Properties.copy(Blocks.CONDUIT)));
	public static final RegistryObject<Block> ENCHANTED_CRAFTING_TABLE = BLOCKS.register("enchanted_crafting_table", () -> new EnchantedCraftingTableBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
	public static final RegistryObject<Block> ENCHANTED_BEEHIVE = BLOCKS.register("enchanted_beehive", () -> new EnchantedBeehiveBlock(BlockBehaviour.Properties.copy(Blocks.BEEHIVE)));
	public static final RegistryObject<Block> ENCHANTED_CHEST = BLOCKS.register("enchanted_chest", () -> new EnchantedChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)));
	public static final RegistryObject<Block> ENCHANTED_TRAPPED_CHEST = BLOCKS.register("enchanted_trapped_chest", () -> new EnchantedTrappedChestBlock(BlockBehaviour.Properties.copy(Blocks.TRAPPED_CHEST)));

	public static final RegistryObject<Block> ENCHANTED_WHITE_BED = BLOCKS.register("enchanted_white_bed", () -> enchantedBlock(DyeColor.WHITE, () -> Blocks.WHITE_BED));
	public static final RegistryObject<Block> ENCHANTED_ORANGE_BED = BLOCKS.register("enchanted_orange_bed", () -> enchantedBlock(DyeColor.ORANGE, () -> Blocks.ORANGE_BED));
	public static final RegistryObject<Block> ENCHANTED_MAGENTA_BED = BLOCKS.register("enchanted_magenta_bed", () -> enchantedBlock(DyeColor.MAGENTA, () -> Blocks.MAGENTA_BED));
	public static final RegistryObject<Block> ENCHANTED_LIGHT_BLUE_BED = BLOCKS.register("enchanted_light_blue_bed", () -> enchantedBlock(DyeColor.LIGHT_BLUE, () -> Blocks.LIGHT_BLUE_BED));
	public static final RegistryObject<Block> ENCHANTED_YELLOW_BED = BLOCKS.register("enchanted_yellow_bed", () -> enchantedBlock(DyeColor.YELLOW, () -> Blocks.YELLOW_BED));
	public static final RegistryObject<Block> ENCHANTED_LIME_BED = BLOCKS.register("enchanted_lime_bed", () -> enchantedBlock(DyeColor.LIME, () -> Blocks.LIME_BED));
	public static final RegistryObject<Block> ENCHANTED_PINK_BED = BLOCKS.register("enchanted_pink_bed", () -> enchantedBlock(DyeColor.PINK, () -> Blocks.PINK_BED));
	public static final RegistryObject<Block> ENCHANTED_GRAY_BED = BLOCKS.register("enchanted_gray_bed", () -> enchantedBlock(DyeColor.GRAY, () -> Blocks.GRAY_BED));
	public static final RegistryObject<Block> ENCHANTED_LIGHT_GRAY_BED = BLOCKS.register("enchanted_light_gray_bed", () -> enchantedBlock(DyeColor.LIGHT_GRAY, () -> Blocks.LIGHT_GRAY_BED));
	public static final RegistryObject<Block> ENCHANTED_CYAN_BED = BLOCKS.register("enchanted_cyan_bed", () -> enchantedBlock(DyeColor.CYAN, () -> Blocks.CYAN_BED));
	public static final RegistryObject<Block> ENCHANTED_PURPLE_BED = BLOCKS.register("enchanted_purple_bed", () -> enchantedBlock(DyeColor.PURPLE, () -> Blocks.PURPLE_BED));
	public static final RegistryObject<Block> ENCHANTED_BLUE_BED = BLOCKS.register("enchanted_blue_bed", () -> enchantedBlock(DyeColor.BLUE, () -> Blocks.BLUE_BED));
	public static final RegistryObject<Block> ENCHANTED_BROWN_BED = BLOCKS.register("enchanted_brown_bed", () -> enchantedBlock(DyeColor.BROWN, () -> Blocks.BROWN_BED));
	public static final RegistryObject<Block> ENCHANTED_GREEN_BED = BLOCKS.register("enchanted_green_bed", () -> enchantedBlock(DyeColor.GREEN, () -> Blocks.GREEN_BED));
	public static final RegistryObject<Block> ENCHANTED_RED_BED = BLOCKS.register("enchanted_red_bed", () -> enchantedBlock(DyeColor.RED, () -> Blocks.RED_BED));
	public static final RegistryObject<Block> ENCHANTED_BLACK_BED = BLOCKS.register("enchanted_black_bed", () -> enchantedBlock(DyeColor.BLACK, () -> Blocks.BLACK_BED));

	private static EnchantedBedBlock enchantedBlock(DyeColor color, Supplier<Block> originalBed) {
		return new EnchantedBedBlock(color, originalBed, BlockBehaviour.Properties.of().mapColor((state) -> {
			return state.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMapColor() : MapColor.WOOL;
		}).sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY));
	}

	public static final RegistryObject<BlockEntityType<EnchantedBlockEntity>> ENCHANTED_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_block", () ->
			BlockEntityType.Builder.of(EnchantedBlockEntity::new, ENCHANTED_MAGMA_BLOCK.get(), ENCHANTED_RESPAWN_ANCHOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedFurnaceBlockEntity>> ENCHANTED_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_furnace", () ->
			BlockEntityType.Builder.of(EnchantedFurnaceBlockEntity::new, ENCHANTED_FURNACE.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedBlastFurnaceBlockEntity>> ENCHANTED_BLAST_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_blast_furnace", () ->
			BlockEntityType.Builder.of(EnchantedBlastFurnaceBlockEntity::new, ENCHANTED_BLAST_FURNACE.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedSmokerBlockEntity>> ENCHANTED_SMOKER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_smoker", () ->
			BlockEntityType.Builder.of(EnchantedSmokerBlockEntity::new, ENCHANTED_SMOKER.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedBeaconBlockEntity>> ENCHANTED_BEACON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_beacon", () ->
			BlockEntityType.Builder.of(EnchantedBeaconBlockEntity::new, ENCHANTED_BEACON.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedCampfireBlockEntity>> ENCHANTED_CAMPFIRE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_campfire", () ->
			BlockEntityType.Builder.of(EnchantedCampfireBlockEntity::new, ENCHANTED_CAMPFIRE.get(), ENCHANTED_SOUL_CAMPFIRE.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedDispenserBlockEntity>> ENCHANTED_DISPENSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_dispenser", () ->
			BlockEntityType.Builder.of(EnchantedDispenserBlockEntity::new, ENCHANTED_DISPENSER.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedHopperBlockEntity>> ENCHANTED_HOPPER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_hopper", () ->
			BlockEntityType.Builder.of(EnchantedHopperBlockEntity::new, ENCHANTED_HOPPER.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedEnchantmentTableBlockEntity>> ENCHANTED_ENCHANTING_TABLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_enchantment_table", () ->
			BlockEntityType.Builder.of(EnchantedEnchantmentTableBlockEntity::new, ENCHANTED_ENCHANTING_TABLE.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedConduitBlockEntity>> ENCHANTED_CONDUIT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_conduit", () ->
			BlockEntityType.Builder.of(EnchantedConduitBlockEntity::new, ENCHANTED_CONDUIT.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedCraftingTableBlockEntity>> ENCHANTED_CRAFTING_TABLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_crafting_table", () ->
			BlockEntityType.Builder.of(EnchantedCraftingTableBlockEntity::new, ENCHANTED_CRAFTING_TABLE.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedBeehiveBlockEntity>> ENCHANTED_BEEHIVE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_beehive", () ->
			BlockEntityType.Builder.of(EnchantedBeehiveBlockEntity::new, ENCHANTED_BEEHIVE.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedChestBlockEntity>> ENCHANTED_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_chest", () ->
			BlockEntityType.Builder.of(EnchantedChestBlockEntity::new, ENCHANTED_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedTrappedChestBlockEntity>> ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_trapped_chest", () ->
			BlockEntityType.Builder.of(EnchantedTrappedChestBlockEntity::new, ENCHANTED_TRAPPED_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnchantedBedBlockEntity>> ENCHANTED_BED_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_bed", () ->
			BlockEntityType.Builder.of(EnchantedBedBlockEntity::new,
					ENCHANTED_WHITE_BED.get(), ENCHANTED_ORANGE_BED.get(), ENCHANTED_MAGENTA_BED.get(), ENCHANTED_LIGHT_BLUE_BED.get(),
					ENCHANTED_YELLOW_BED.get(), ENCHANTED_LIME_BED.get(), ENCHANTED_PINK_BED.get(), ENCHANTED_GRAY_BED.get(),
					ENCHANTED_LIGHT_GRAY_BED.get(), ENCHANTED_CYAN_BED.get(), ENCHANTED_PURPLE_BED.get(), ENCHANTED_BLUE_BED.get(),
					ENCHANTED_BROWN_BED.get(), ENCHANTED_GREEN_BED.get(), ENCHANTED_RED_BED.get(), ENCHANTED_BLACK_BED.get()
			).build(null));
}
