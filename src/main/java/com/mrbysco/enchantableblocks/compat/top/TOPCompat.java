package com.mrbysco.enchantableblocks.compat.top;

import com.mrbysco.enchantableblocks.EnchantableBlocks;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.InterModComms;

import java.util.function.Consumer;
import java.util.function.Function;

public class TOPCompat {
	public static void register() {
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	public static final class GetTheOneProbe implements Function<ITheOneProbe, Void> {
		@Override
		public Void apply(ITheOneProbe input) {
			input.registerProvider(new EnchantedInfo());
			return null;
		}
	}

	public static final class EnchantedInfo implements IProbeInfoProvider {

		@Override
		public ResourceLocation getID() {
			return ResourceLocation.fromNamespaceAndPath(EnchantableBlocks.MOD_ID, "main");
		}

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState state, IProbeHitData data) {
			BlockEntity blockEntity = level.getBlockEntity(data.getPos());
			if (blockEntity instanceof IEnchantable enchantable) {
				Consumer<Component> consumer = probeInfo::mcText;
				enchantable.getEnchantments().addToTooltip(Item.TooltipContext.of(level), consumer, TooltipFlag.NORMAL);
			}
		}
	}
}