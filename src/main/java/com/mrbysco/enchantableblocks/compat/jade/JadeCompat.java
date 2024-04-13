package com.mrbysco.enchantableblocks.compat.jade;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {
	@Override
	public void register(IWailaCommonRegistration registration) {
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		List<Class<? extends Block>> blockClasses = new ArrayList<>();
		for (RegistryObject<Block> registryObject : ModRegistry.BLOCKS.getEntries()) {
			Block block = registryObject.get();
			Class<? extends Block> blockClass = block.getClass();
			if (!blockClasses.contains(blockClass)) {
				blockClasses.add(blockClass);
			}
		}
		blockClasses.forEach(blockClass -> registration.registerBlockComponent(EnchantedBlockHandler.INSTANCE, blockClass));
	}

	public static class EnchantedBlockHandler implements IBlockComponentProvider {
		private static final ResourceLocation ENCHANTMENTS = new ResourceLocation(EnchantableBlocks.MOD_ID, "enchantments");

		public static final EnchantedBlockHandler INSTANCE = new EnchantedBlockHandler();

		@Override
		public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
			if (blockAccessor.getBlockEntity() instanceof IEnchantable enchantable) {
				enchantable.getEnchantments().forEach((enchantment, integer) -> iTooltip.add(enchantment.getFullname(integer)));
			}
		}

		@Override
		public ResourceLocation getUid() {
			return ENCHANTMENTS;
		}
	}
}
