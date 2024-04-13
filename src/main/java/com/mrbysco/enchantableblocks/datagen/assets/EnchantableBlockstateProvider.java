package com.mrbysco.enchantableblocks.datagen.assets;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class EnchantableBlockstateProvider extends BlockStateProvider {
	public EnchantableBlockstateProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, EnchantableBlocks.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		abstractFurnace(ModRegistry.ENCHANTED_FURNACE, "furnace");
		abstractFurnace(ModRegistry.ENCHANTED_BLAST_FURNACE, "blast_furnace");
		abstractFurnace(ModRegistry.ENCHANTED_SMOKER, "smoker");

		campfire(ModRegistry.ENCHANTED_CAMPFIRE, "campfire");
		campfire(ModRegistry.ENCHANTED_SOUL_CAMPFIRE, "soul_campfire");
		simpleBlock(ModRegistry.ENCHANTED_MAGMA_BLOCK.get(), models().getExistingFile(mcLoc("magma_block")));
		dispenser(ModRegistry.ENCHANTED_DISPENSER);
		respawnAnchor(ModRegistry.ENCHANTED_RESPAWN_ANCHOR);
		hopper(ModRegistry.ENCHANTED_HOPPER);
		simpleBlock(ModRegistry.ENCHANTED_CONDUIT.get(), models().getExistingFile(mcLoc("conduit")));
		simpleBlock(ModRegistry.ENCHANTED_CRAFTING_TABLE.get(), models().getExistingFile(mcLoc("crafting_table")));
		simpleBlock(ModRegistry.ENCHANTED_CHEST.get(), models().getExistingFile(mcLoc("chest")));
		simpleBlock(ModRegistry.ENCHANTED_TRAPPED_CHEST.get(), models().getExistingFile(mcLoc("chest")));
		beehive(ModRegistry.ENCHANTED_BEEHIVE);

		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_BEACON, "beacon");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_WHITE_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_ORANGE_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_MAGENTA_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_LIGHT_BLUE_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_YELLOW_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_LIME_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_PINK_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_GRAY_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_LIGHT_GRAY_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_CYAN_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_PURPLE_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_BLUE_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_BROWN_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_GREEN_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_RED_BED, "bed");
		simpleCutoutEnchantedBlock(ModRegistry.ENCHANTED_BLACK_BED, "bed");
	}

	private void simpleCutoutEnchantedBlock(RegistryObject<Block> enchantedBlock, String originalBlock) {
		simpleBlock(enchantedBlock.get(), models()
				.withExistingParent("enchantableblocks:block/" + enchantedBlock.getId().getPath(), "minecraft:block/" + originalBlock).renderType(new ResourceLocation("cutout")));
	}

	private void beehive(RegistryObject<Block> registryObject) {
		ModelFile beehive = models().getExistingFile(mcLoc("beehive"));
		ModelFile beehiveHoney = models().getExistingFile(mcLoc("beehive_honey"));
		horizontalBlock(registryObject.get(), (state -> state.getValue(BlockStateProperties.LEVEL_HONEY) == 5 ? beehiveHoney : beehive));
	}

	private void campfire(RegistryObject<Block> registryObject, String originalBlock) {
		ModelFile offModel = this.models().getExistingFile(mcLoc("campfire_off"));
		ModelFile model = this.models().getExistingFile(mcLoc(originalBlock));
		this.getVariantBuilder(registryObject.get())
				.partialState().with(CampfireBlock.FACING, Direction.EAST).with(CampfireBlock.LIT, false)
				.modelForState().modelFile(offModel).rotationY(270).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.EAST).with(CampfireBlock.LIT, true)
				.modelForState().modelFile(model).rotationY(270).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.NORTH).with(CampfireBlock.LIT, false)
				.modelForState().modelFile(offModel).rotationY(180).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.NORTH).with(CampfireBlock.LIT, true)
				.modelForState().modelFile(model).rotationY(180).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.SOUTH).with(CampfireBlock.LIT, false)
				.modelForState().modelFile(offModel).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.SOUTH).with(CampfireBlock.LIT, true)
				.modelForState().modelFile(model).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.WEST).with(CampfireBlock.LIT, false)
				.modelForState().modelFile(offModel).rotationY(90).addModel()
				.partialState().with(CampfireBlock.FACING, Direction.WEST).with(CampfireBlock.LIT, true)
				.modelForState().modelFile(model).rotationY(90).addModel();
	}

	private void abstractFurnace(RegistryObject<Block> block, String originalBlock) {
		ModelFile normal = this.models().getExistingFile(mcLoc(originalBlock));
		ModelFile lit = this.models().getExistingFile(mcLoc(originalBlock + "_on"));
		this.getVariantBuilder(block.get())
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.NORTH).with(AbstractFurnaceBlock.LIT, false)
				.modelForState().modelFile(normal).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.EAST).with(AbstractFurnaceBlock.LIT, false)
				.modelForState().modelFile(normal).rotationY(90).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH).with(AbstractFurnaceBlock.LIT, false)
				.modelForState().modelFile(normal).rotationY(180).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.WEST).with(AbstractFurnaceBlock.LIT, false)
				.modelForState().modelFile(normal).rotationY(270).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.NORTH).with(AbstractFurnaceBlock.LIT, true)
				.modelForState().modelFile(lit).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.EAST).with(AbstractFurnaceBlock.LIT, true)
				.modelForState().modelFile(lit).rotationY(90).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH).with(AbstractFurnaceBlock.LIT, true)
				.modelForState().modelFile(lit).rotationY(180).addModel()
				.partialState().with(AbstractFurnaceBlock.FACING, Direction.WEST).with(AbstractFurnaceBlock.LIT, true)
				.modelForState().modelFile(lit).rotationY(270).addModel();
	}

	private void dispenser(RegistryObject<Block> block) {
		ModelFile model = this.models().getExistingFile(mcLoc("dispenser"));
		ModelFile vertical = this.models().getExistingFile(mcLoc("dispenser_vertical"));
		this.getVariantBuilder(block.get())
				.partialState().with(DispenserBlock.FACING, Direction.DOWN)
				.modelForState().modelFile(vertical).rotationX(180).addModel()
				.partialState().with(DispenserBlock.FACING, Direction.EAST)
				.modelForState().modelFile(model).rotationY(90).addModel()
				.partialState().with(DispenserBlock.FACING, Direction.NORTH)
				.modelForState().modelFile(model).addModel()
				.partialState().with(DispenserBlock.FACING, Direction.SOUTH)
				.modelForState().modelFile(model).rotationY(180).addModel()
				.partialState().with(DispenserBlock.FACING, Direction.UP)
				.modelForState().modelFile(vertical).addModel()
				.partialState().with(DispenserBlock.FACING, Direction.WEST)
				.modelForState().modelFile(model).rotationY(270).addModel();
	}

	private void hopper(RegistryObject<Block> block) {
		ModelFile model = this.models().getExistingFile(mcLoc("hopper"));
		ModelFile side = this.models().getExistingFile(mcLoc("hopper_side"));
		this.getVariantBuilder(block.get())
				.partialState().with(HopperBlock.FACING, Direction.DOWN)
				.modelForState().modelFile(model).addModel()
				.partialState().with(HopperBlock.FACING, Direction.EAST)
				.modelForState().modelFile(side).rotationY(90).addModel()
				.partialState().with(HopperBlock.FACING, Direction.NORTH)
				.modelForState().modelFile(side).addModel()
				.partialState().with(HopperBlock.FACING, Direction.SOUTH)
				.modelForState().modelFile(side).rotationY(180).addModel()
				.partialState().with(HopperBlock.FACING, Direction.WEST)
				.modelForState().modelFile(side).rotationY(270).addModel();
	}

	private void respawnAnchor(RegistryObject<Block> block) {
		ModelFile charge0 = this.models().getExistingFile(mcLoc("respawn_anchor_0"));
		ModelFile charge1 = this.models().getExistingFile(mcLoc("respawn_anchor_1"));
		ModelFile charge2 = this.models().getExistingFile(mcLoc("respawn_anchor_2"));
		ModelFile charge3 = this.models().getExistingFile(mcLoc("respawn_anchor_3"));
		ModelFile charge4 = this.models().getExistingFile(mcLoc("respawn_anchor_4"));
		this.getVariantBuilder(block.get())
				.partialState().with(RespawnAnchorBlock.CHARGE, 0)
				.modelForState().modelFile(charge0).addModel()
				.partialState().with(RespawnAnchorBlock.CHARGE, 1)
				.modelForState().modelFile(charge1).addModel()
				.partialState().with(RespawnAnchorBlock.CHARGE, 2)
				.modelForState().modelFile(charge2).addModel()
				.partialState().with(RespawnAnchorBlock.CHARGE, 3)
				.modelForState().modelFile(charge3).addModel()
				.partialState().with(RespawnAnchorBlock.CHARGE, 4)
				.modelForState().modelFile(charge4).addModel();
	}

	private ResourceLocation suffix(ResourceLocation location, String suffix) {
		return new ResourceLocation(location.getNamespace(), location.getPath() + suffix);
	}
}
