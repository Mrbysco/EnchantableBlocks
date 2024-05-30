package com.mrbysco.enchantableblocks.datagen.data;

import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnchantableLootProvider extends LootTableProvider {
	public EnchantableLootProvider(PackOutput packOutput) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(EnchantableBlockLootSubProvider::new, LootContextParamSets.BLOCK)
		));
	}

	private static class EnchantableBlockLootSubProvider extends BlockLootSubProvider {

		protected EnchantableBlockLootSubProvider() {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		protected void generate() {
			this.add(ModRegistry.ENCHANTED_FURNACE.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.FURNACE));
			this.add(ModRegistry.ENCHANTED_BLAST_FURNACE.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.BLAST_FURNACE));
			this.add(ModRegistry.ENCHANTED_SMOKER.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.SMOKER));
			this.add(ModRegistry.ENCHANTED_BEACON.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.BEACON));
			this.add(ModRegistry.ENCHANTED_CAMPFIRE.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.CAMPFIRE));
			this.add(ModRegistry.ENCHANTED_SOUL_CAMPFIRE.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.SOUL_CAMPFIRE));
			this.add(ModRegistry.ENCHANTED_MAGMA_BLOCK.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.MAGMA_BLOCK));
			this.add(ModRegistry.ENCHANTED_DISPENSER.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.DISPENSER));
			this.add(ModRegistry.ENCHANTED_RESPAWN_ANCHOR.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.RESPAWN_ANCHOR));
			this.add(ModRegistry.ENCHANTED_HOPPER.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.HOPPER));
			this.add(ModRegistry.ENCHANTED_ENCHANTING_TABLE.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.ENCHANTING_TABLE));
			this.add(ModRegistry.ENCHANTED_CONDUIT.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.CONDUIT));
			this.add(ModRegistry.ENCHANTED_CRAFTING_TABLE.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.CRAFTING_TABLE));
			this.add(ModRegistry.ENCHANTED_BEEHIVE.get(), (block) -> createEnchantedBeeHiveDrop(block, Blocks.BEEHIVE));
			this.add(ModRegistry.ENCHANTED_CHEST.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.CHEST));
			this.add(ModRegistry.ENCHANTED_TRAPPED_CHEST.get(), (block) -> createEnchantableBlockEntityTable(block, Blocks.TRAPPED_CHEST));

			this.add(ModRegistry.ENCHANTED_WHITE_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.WHITE_BED));
			this.add(ModRegistry.ENCHANTED_ORANGE_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.ORANGE_BED));
			this.add(ModRegistry.ENCHANTED_MAGENTA_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.MAGENTA_BED));
			this.add(ModRegistry.ENCHANTED_LIGHT_BLUE_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.LIGHT_BLUE_BED));
			this.add(ModRegistry.ENCHANTED_YELLOW_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.YELLOW_BED));
			this.add(ModRegistry.ENCHANTED_LIME_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.LIME_BED));
			this.add(ModRegistry.ENCHANTED_PINK_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.PINK_BED));
			this.add(ModRegistry.ENCHANTED_GRAY_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.GRAY_BED));
			this.add(ModRegistry.ENCHANTED_LIGHT_GRAY_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.LIGHT_GRAY_BED));
			this.add(ModRegistry.ENCHANTED_CYAN_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.CYAN_BED));
			this.add(ModRegistry.ENCHANTED_PURPLE_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.PURPLE_BED));
			this.add(ModRegistry.ENCHANTED_BLUE_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.BLUE_BED));
			this.add(ModRegistry.ENCHANTED_BROWN_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.BROWN_BED));
			this.add(ModRegistry.ENCHANTED_GREEN_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.GREEN_BED));
			this.add(ModRegistry.ENCHANTED_RED_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.RED_BED));
			this.add(ModRegistry.ENCHANTED_BLACK_BED.get(), (block) -> createEnchantableBedBlockTable(block, Blocks.BLACK_BED));
		}

		protected static LootTable.Builder createEnchantedBeeHiveDrop(Block block, Block originalBlock) {
			return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(originalBlock).when(HAS_SILK_TOUCH)
							.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
									.copy("Bees", "BlockEntityTag.Bees"))
							.apply(CopyBlockState.copyState(block).copy(BeehiveBlock.HONEY_LEVEL))
							.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
							.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Enchantments", "Enchantments"))
							.otherwise(LootItem.lootTableItem(originalBlock))));
		}

		protected LootTable.Builder createEnchantableBedBlockTable(Block block, Block originalBlock) {
			return LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(originalBlock)
							.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
									.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BedBlock.PART, BedPart.HEAD))
							)
							.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
							.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Enchantments", "Enchantments"))
					)
			));
		}

		protected LootTable.Builder createEnchantableBlockEntityTable(Block block, Block originalBlock) {
			return LootTable.lootTable()
					.withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
									.add(LootItem.lootTableItem(originalBlock)
											.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
											.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Enchantments", "Enchantments"))
									)
							)
					);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return ModRegistry.BLOCKS.getEntries().stream().map((holder) -> (Block) holder.value())::iterator;
		}
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationContext) {
		map.forEach((location, lootTable) -> lootTable.validate(validationContext));
	}
}
