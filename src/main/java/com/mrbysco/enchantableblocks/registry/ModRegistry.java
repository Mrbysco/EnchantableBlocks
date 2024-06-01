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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.VanillaHopperItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EnchantableBlocks.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EnchantableBlocks.MOD_ID);

	public static final DeferredBlock<EnchantedFurnaceBlock> ENCHANTED_FURNACE = BLOCKS.register("enchanted_furnace", () -> new EnchantedFurnaceBlock(Block.Properties.ofFullCopy(Blocks.FURNACE)));
	public static final DeferredBlock<EnchantedBlastFurnaceBlock> ENCHANTED_BLAST_FURNACE = BLOCKS.register("enchanted_blast_furnace", () -> new EnchantedBlastFurnaceBlock(Block.Properties.ofFullCopy(Blocks.BLAST_FURNACE)));
	public static final DeferredBlock<EnchantedSmokerBlock> ENCHANTED_SMOKER = BLOCKS.register("enchanted_smoker", () -> new EnchantedSmokerBlock(Block.Properties.ofFullCopy(Blocks.SMOKER)));
	public static final DeferredBlock<EnchantedBeaconBlock> ENCHANTED_BEACON = BLOCKS.register("enchanted_beacon", () -> new EnchantedBeaconBlock(Block.Properties.ofFullCopy(Blocks.BEACON)));
	public static final DeferredBlock<EnchantedCampfireBlock> ENCHANTED_CAMPFIRE = BLOCKS.register("enchanted_campfire", () -> new EnchantedCampfireBlock(true, 1, () -> Blocks.CAMPFIRE, BlockBehaviour.Properties.ofFullCopy(Blocks.CAMPFIRE)));
	public static final DeferredBlock<EnchantedCampfireBlock> ENCHANTED_SOUL_CAMPFIRE = BLOCKS.register("enchanted_soul_campfire", () -> new EnchantedCampfireBlock(false, 2, () -> Blocks.SOUL_CAMPFIRE, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_CAMPFIRE)));
	public static final DeferredBlock<EnchantedMagmaBlock> ENCHANTED_MAGMA_BLOCK = BLOCKS.register("enchanted_magma_block", () -> new EnchantedMagmaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)));
	public static final DeferredBlock<EnchantedDispenserBlock> ENCHANTED_DISPENSER = BLOCKS.register("enchanted_dispenser", () -> new EnchantedDispenserBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DISPENSER)));
	public static final DeferredBlock<EnchantedRespawnAnchorBlock> ENCHANTED_RESPAWN_ANCHOR = BLOCKS.register("enchanted_respawn_anchor", () -> new EnchantedRespawnAnchorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RESPAWN_ANCHOR)));
	public static final DeferredBlock<EnchantedHopperBlock> ENCHANTED_HOPPER = BLOCKS.register("enchanted_hopper", () -> new EnchantedHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER)));
	public static final DeferredBlock<EnchantedEnchantmentTableBlock> ENCHANTED_ENCHANTING_TABLE = BLOCKS.register("enchanted_enchanting_table", () -> new EnchantedEnchantmentTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ENCHANTING_TABLE)));
	public static final DeferredBlock<EnchantedConduitBlock> ENCHANTED_CONDUIT = BLOCKS.register("enchanted_conduit", () -> new EnchantedConduitBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CONDUIT)));
	public static final DeferredBlock<EnchantedCraftingTableBlock> ENCHANTED_CRAFTING_TABLE = BLOCKS.register("enchanted_crafting_table", () -> new EnchantedCraftingTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)));
	public static final DeferredBlock<EnchantedBeehiveBlock> ENCHANTED_BEEHIVE = BLOCKS.register("enchanted_beehive", () -> new EnchantedBeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
	public static final DeferredBlock<EnchantedChestBlock> ENCHANTED_CHEST = BLOCKS.register("enchanted_chest", () -> new EnchantedChestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST)));
	public static final DeferredBlock<EnchantedTrappedChestBlock> ENCHANTED_TRAPPED_CHEST = BLOCKS.register("enchanted_trapped_chest", () -> new EnchantedTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TRAPPED_CHEST)));

	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_WHITE_BED = BLOCKS.register("enchanted_white_bed", () -> enchantedBlock(DyeColor.WHITE, () -> Blocks.WHITE_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_ORANGE_BED = BLOCKS.register("enchanted_orange_bed", () -> enchantedBlock(DyeColor.ORANGE, () -> Blocks.ORANGE_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_MAGENTA_BED = BLOCKS.register("enchanted_magenta_bed", () -> enchantedBlock(DyeColor.MAGENTA, () -> Blocks.MAGENTA_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_LIGHT_BLUE_BED = BLOCKS.register("enchanted_light_blue_bed", () -> enchantedBlock(DyeColor.LIGHT_BLUE, () -> Blocks.LIGHT_BLUE_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_YELLOW_BED = BLOCKS.register("enchanted_yellow_bed", () -> enchantedBlock(DyeColor.YELLOW, () -> Blocks.YELLOW_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_LIME_BED = BLOCKS.register("enchanted_lime_bed", () -> enchantedBlock(DyeColor.LIME, () -> Blocks.LIME_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_PINK_BED = BLOCKS.register("enchanted_pink_bed", () -> enchantedBlock(DyeColor.PINK, () -> Blocks.PINK_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_GRAY_BED = BLOCKS.register("enchanted_gray_bed", () -> enchantedBlock(DyeColor.GRAY, () -> Blocks.GRAY_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_LIGHT_GRAY_BED = BLOCKS.register("enchanted_light_gray_bed", () -> enchantedBlock(DyeColor.LIGHT_GRAY, () -> Blocks.LIGHT_GRAY_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_CYAN_BED = BLOCKS.register("enchanted_cyan_bed", () -> enchantedBlock(DyeColor.CYAN, () -> Blocks.CYAN_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_PURPLE_BED = BLOCKS.register("enchanted_purple_bed", () -> enchantedBlock(DyeColor.PURPLE, () -> Blocks.PURPLE_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_BLUE_BED = BLOCKS.register("enchanted_blue_bed", () -> enchantedBlock(DyeColor.BLUE, () -> Blocks.BLUE_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_BROWN_BED = BLOCKS.register("enchanted_brown_bed", () -> enchantedBlock(DyeColor.BROWN, () -> Blocks.BROWN_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_GREEN_BED = BLOCKS.register("enchanted_green_bed", () -> enchantedBlock(DyeColor.GREEN, () -> Blocks.GREEN_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_RED_BED = BLOCKS.register("enchanted_red_bed", () -> enchantedBlock(DyeColor.RED, () -> Blocks.RED_BED));
	public static final DeferredBlock<EnchantedBedBlock> ENCHANTED_BLACK_BED = BLOCKS.register("enchanted_black_bed", () -> enchantedBlock(DyeColor.BLACK, () -> Blocks.BLACK_BED));

	private static EnchantedBedBlock enchantedBlock(DyeColor color, Supplier<Block> originalBed) {
		return new EnchantedBedBlock(color, originalBed, BlockBehaviour.Properties.of().mapColor((state) -> {
			return state.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMapColor() : MapColor.WOOL;
		}).sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY));
	}

	public static final Supplier<BlockEntityType<EnchantedBlockEntity>> ENCHANTED_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_block", () ->
			BlockEntityType.Builder.of(EnchantedBlockEntity::new, ENCHANTED_MAGMA_BLOCK.get(), ENCHANTED_RESPAWN_ANCHOR.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedFurnaceBlockEntity>> ENCHANTED_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_furnace", () ->
			BlockEntityType.Builder.of(EnchantedFurnaceBlockEntity::new, ENCHANTED_FURNACE.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedBlastFurnaceBlockEntity>> ENCHANTED_BLAST_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_blast_furnace", () ->
			BlockEntityType.Builder.of(EnchantedBlastFurnaceBlockEntity::new, ENCHANTED_BLAST_FURNACE.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedSmokerBlockEntity>> ENCHANTED_SMOKER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_smoker", () ->
			BlockEntityType.Builder.of(EnchantedSmokerBlockEntity::new, ENCHANTED_SMOKER.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedBeaconBlockEntity>> ENCHANTED_BEACON_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_beacon", () ->
			BlockEntityType.Builder.of(EnchantedBeaconBlockEntity::new, ENCHANTED_BEACON.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedCampfireBlockEntity>> ENCHANTED_CAMPFIRE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_campfire", () ->
			BlockEntityType.Builder.of(EnchantedCampfireBlockEntity::new, ENCHANTED_CAMPFIRE.get(), ENCHANTED_SOUL_CAMPFIRE.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedDispenserBlockEntity>> ENCHANTED_DISPENSER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_dispenser", () ->
			BlockEntityType.Builder.of(EnchantedDispenserBlockEntity::new, ENCHANTED_DISPENSER.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedHopperBlockEntity>> ENCHANTED_HOPPER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_hopper", () ->
			BlockEntityType.Builder.of(EnchantedHopperBlockEntity::new, ENCHANTED_HOPPER.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedEnchantmentTableBlockEntity>> ENCHANTED_ENCHANTING_TABLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_enchantment_table", () ->
			BlockEntityType.Builder.of(EnchantedEnchantmentTableBlockEntity::new, ENCHANTED_ENCHANTING_TABLE.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedConduitBlockEntity>> ENCHANTED_CONDUIT_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_conduit", () ->
			BlockEntityType.Builder.of(EnchantedConduitBlockEntity::new, ENCHANTED_CONDUIT.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedCraftingTableBlockEntity>> ENCHANTED_CRAFTING_TABLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_crafting_table", () ->
			BlockEntityType.Builder.of(EnchantedCraftingTableBlockEntity::new, ENCHANTED_CRAFTING_TABLE.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedBeehiveBlockEntity>> ENCHANTED_BEEHIVE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_beehive", () ->
			BlockEntityType.Builder.of(EnchantedBeehiveBlockEntity::new, ENCHANTED_BEEHIVE.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedChestBlockEntity>> ENCHANTED_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_chest", () ->
			BlockEntityType.Builder.of(EnchantedChestBlockEntity::new, ENCHANTED_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedTrappedChestBlockEntity>> ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_trapped_chest", () ->
			BlockEntityType.Builder.of(EnchantedTrappedChestBlockEntity::new, ENCHANTED_TRAPPED_CHEST.get()).build(null));
	public static final Supplier<BlockEntityType<EnchantedBedBlockEntity>> ENCHANTED_BED_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("enchanted_bed", () ->
			BlockEntityType.Builder.of(EnchantedBedBlockEntity::new,
					ENCHANTED_WHITE_BED.get(), ENCHANTED_ORANGE_BED.get(), ENCHANTED_MAGENTA_BED.get(), ENCHANTED_LIGHT_BLUE_BED.get(),
					ENCHANTED_YELLOW_BED.get(), ENCHANTED_LIME_BED.get(), ENCHANTED_PINK_BED.get(), ENCHANTED_GRAY_BED.get(),
					ENCHANTED_LIGHT_GRAY_BED.get(), ENCHANTED_CYAN_BED.get(), ENCHANTED_PURPLE_BED.get(), ENCHANTED_BLUE_BED.get(),
					ENCHANTED_BROWN_BED.get(), ENCHANTED_GREEN_BED.get(), ENCHANTED_RED_BED.get(), ENCHANTED_BLACK_BED.get()
			).build(null));

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENCHANTED_CHEST_BLOCK_ENTITY.get(), EnchantedChestBlockEntity::getHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENCHANTED_TRAPPED_CHEST_BLOCK_ENTITY.get(), EnchantedTrappedChestBlockEntity::getHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ENCHANTED_HOPPER_BLOCK_ENTITY.get(), (hopper, side) -> {
			// Use custom hopper wrapper that respects cooldown
			return new VanillaHopperItemHandler(hopper);
		});

		var enchantedFurnaces = List.of(
				ENCHANTED_BLAST_FURNACE_BLOCK_ENTITY.get(),
				ENCHANTED_FURNACE_BLOCK_ENTITY.get(),
				ENCHANTED_SMOKER_BLOCK_ENTITY.get());
		for (var type : enchantedFurnaces) {
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (sidedContainer, side) -> {
				return side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side);
			});
		}
	}
}
