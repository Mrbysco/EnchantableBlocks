package com.mrbysco.enchantableblocks.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface BeehiveBlockEntityAccessor {
	@Invoker("canBurn")
	boolean invokeCanBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize);

	@Accessor("quickCheck")
	RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> getQuickCheck();
}
