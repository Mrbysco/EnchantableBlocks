package com.mrbysco.enchantableblocks.menu;

import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.mixin.EnchantmentMenuAccessor;
import com.mrbysco.enchantableblocks.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class EnchantedEnchantmentMenu extends EnchantmentMenu {
	public EnchantedEnchantmentMenu(int containerID, Inventory inventory) {
		super(containerID, inventory);
	}

	public EnchantedEnchantmentMenu(int containerID, Inventory inventory, ContainerLevelAccess pAccess) {
		super(containerID, inventory, pAccess);
	}

	@Override
	public MenuType<?> getType() {
		return ModMenus.ENCHANTED_ENCHANTMENT.get();
	}

	@Override
	public void slotsChanged(Container pInventory) {
		if (pInventory == this.enchantSlots) {
			ItemStack itemstack = pInventory.getItem(0);
			if (!itemstack.isEmpty() && itemstack.isEnchantable()) {
				this.access.execute((level, pos) -> {
					float j = 0;
					float powerMultiplier = 1.0F;
					BlockEntity blockEntity = level.getBlockEntity(pos);
					if (blockEntity instanceof IEnchantable enchantable && enchantable.hasEnchantment(Enchantments.BLOCK_EFFICIENCY)) {
						powerMultiplier = 1.0F + (float) enchantable.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) * 0.5F;
					}

					for (BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
						if (EnchantmentTableBlock.isValidBookShelf(level, pos, blockpos)) {
							j += (level.getBlockState(pos.offset(blockpos)).getEnchantPowerBonus(level, pos.offset(blockpos)) * powerMultiplier);
						}
					}

					this.random.setSeed((long) this.enchantmentSeed.get());

					for (int k = 0; k < 3; ++k) {
						this.costs[k] = EnchantmentHelper.getEnchantmentCost(this.random, k, (int) j, itemstack);
						this.enchantClue[k] = -1;
						this.levelClue[k] = -1;
						if (this.costs[k] < k + 1) {
							this.costs[k] = 0;
						}
						this.costs[k] = net.minecraftforge.event.ForgeEventFactory.onEnchantmentLevelSet(level, pos, k, (int) j, itemstack, costs[k]);
					}

					for (int l = 0; l < 3; ++l) {
						if (this.costs[l] > 0) {
							List<EnchantmentInstance> list = ((EnchantmentMenuAccessor) this).invokeGetEnchantmentList(itemstack, l, this.costs[l]);
							if (list != null && !list.isEmpty()) {
								EnchantmentInstance enchantmentinstance = list.get(this.random.nextInt(list.size()));
								this.enchantClue[l] = BuiltInRegistries.ENCHANTMENT.getId(enchantmentinstance.enchantment);
								this.levelClue[l] = enchantmentinstance.level;
							}
						}
					}

					this.broadcastChanges();
				});
			} else {
				for (int i = 0; i < 3; ++i) {
					this.costs[i] = 0;
					this.enchantClue[i] = -1;
					this.levelClue[i] = -1;
				}
			}
		}
	}
}
