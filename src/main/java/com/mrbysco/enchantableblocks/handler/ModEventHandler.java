package com.mrbysco.enchantableblocks.handler;

import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class ModEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSleepFinished(SleepFinishedTimeEvent event) {
		ServerLevel serverLevel = (ServerLevel) event.getLevel();
		serverLevel.players().forEach(player -> {
			if (player.isSleeping()) {
				BlockPos sleepingPos = player.blockPosition();
				BlockEntity blockEntity = serverLevel.getBlockEntity(sleepingPos);
				if (blockEntity instanceof IEnchantable enchantable) {
					Holder<Enchantment> restedHolder = EnchantmentUtil.getEnchantmentHolder(serverLevel, ModEnchantments.WELL_RESTED);
					if (enchantable.hasEnchantment(restedHolder)) {
						int enchantmentLevel = enchantable.getEnchantmentLevel(restedHolder);
						boolean visibleParticles = !enchantable.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(serverLevel, ModEnchantments.CONCEALED));
						switch (enchantmentLevel) {
							default:
								player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0, false, visibleParticles));
								break;
							case 2:
								player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 1, false, visibleParticles));
								player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 10, 0, false, visibleParticles));
								break;
							case 3:
								player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 2, false, visibleParticles));
								player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 0, false, visibleParticles));
								player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, visibleParticles));
								break;
						}
					}
				}
			}
		});
	}

	@SubscribeEvent
	public static void onTagsUpdated(OnDatapackSyncEvent event) {
		EnchantmentUtil.clearCache();
	}
}
