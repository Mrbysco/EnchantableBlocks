package com.mrbysco.enchantableblocks.handler;

import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSleepFinished(SleepFinishedTimeEvent event) {
		ServerLevel serverLevel = (ServerLevel) event.getLevel();
		serverLevel.players().forEach(player -> {
			if (player.isSleeping()) {
				BlockPos sleepingPos = player.blockPosition();
				BlockEntity blockEntity = serverLevel.getBlockEntity(sleepingPos);
				if (blockEntity instanceof IEnchantable enchantable) {
					if (enchantable.hasEnchantment(ModEnchantments.WELL_RESTED.get())) {
						int enchantmentLevel = enchantable.getEnchantmentLevel(ModEnchantments.WELL_RESTED.get());
						boolean visibleParticles = !enchantable.hasEnchantment(ModEnchantments.CONCEALED.get());
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
}
