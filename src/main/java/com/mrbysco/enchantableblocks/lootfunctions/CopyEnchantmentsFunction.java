package com.mrbysco.enchantableblocks.lootfunctions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.registry.ModLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public class CopyEnchantmentsFunction extends LootItemConditionalFunction {

	CopyEnchantmentsFunction(LootItemCondition[] pConditions) {
		super(pConditions);
	}

	public LootItemFunctionType getType() {
		return ModLootFunctions.COPY_ENCHANTMENTS.get();
	}

	/**
	 * Get the parameters used by this object.
	 */
	public Set<LootContextParam<?>> getReferencedContextParams() {
		return ImmutableSet.of(LootContextParams.BLOCK_ENTITY);
	}

	/**
	 * Called to perform the actual action of this function, after conditions have been checked.
	 */
	public ItemStack run(ItemStack stack, LootContext context) {
		Object object = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
		if (object instanceof IEnchantable enchantable) {
			if (enchantable.getEnchantmentsTag() != null) {
				stack.getOrCreateTag().put("Enchantments", enchantable.getEnchantmentsTag());
			}
		}

		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> copyEnchantments() {
		return simpleBuilder(CopyEnchantmentsFunction::new);
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<CopyEnchantmentsFunction> {
		/**
		 * Serialize the {@link CopyNbtFunction} by putting its data into the JsonObject.
		 */
		public void serialize(JsonObject jsonObject, CopyEnchantmentsFunction function, JsonSerializationContext serializationContext) {
			super.serialize(jsonObject, function, serializationContext);
		}

		public CopyEnchantmentsFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
			return new CopyEnchantmentsFunction(conditions);
		}
	}
}
