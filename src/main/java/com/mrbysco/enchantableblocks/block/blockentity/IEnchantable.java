package com.mrbysco.enchantableblocks.block.blockentity;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public interface IEnchantable {
	ItemEnchantments getEnchantments();

	void setEnchantments(ItemEnchantments enchantments);

	boolean hasEnchantment(Holder<Enchantment> enchantment);

	int getEnchantmentLevel(Holder<Enchantment> enchantment);

	boolean hasEnchantment(TagKey<Enchantment> enchantmentTag);

	int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag);

	boolean hideGlint();

	default void saveEnchantments(CompoundTag tag, HolderLookup.Provider provider) {
		if (getEnchantments() != null) {
			RegistryOps<Tag> registryops = provider.createSerializationContext(NbtOps.INSTANCE);
			ItemEnchantments.CODEC
					.encodeStart(registryops, getEnchantments())
					.resultOrPartial()
					.ifPresent(enchantments -> tag.put("Enchantments", enchantments));
		}
	}

	default void loadEnchantments(CompoundTag tag, HolderLookup.Provider provider) {
		if (tag.contains("Enchantments")) {
			RegistryOps<Tag> registryops = provider.createSerializationContext(NbtOps.INSTANCE);
			ItemEnchantments.CODEC
					.parse(new Dynamic<>(registryops, tag.get("Enchantments")))
					.resultOrPartial()
					.ifPresent(this::setEnchantments);
		}
	}
}
