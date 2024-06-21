package com.mrbysco.enchantableblocks.block.blockentity;

import com.mojang.serialization.Dynamic;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.TagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantedEnchantingTableBlockEntity extends EnchantingTableBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected ItemEnchantments enchantments = ItemEnchantments.EMPTY;

	public EnchantedEnchantingTableBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModRegistry.ENCHANTED_ENCHANTING_TABLE_BLOCK_ENTITY.get();
	}

	@Override
	public ItemEnchantments getEnchantments() {
		return enchantments;
	}

	@Override
	public boolean hasEnchantment(Holder<Enchantment> enchantment) {
		return this.enchantments.getLevel(enchantment) > 0;
	}

	@Override
	public int getEnchantmentLevel(Holder<Enchantment> enchantment) {
		if (this.hasEnchantment(enchantment))
			return this.enchantments.getLevel(enchantment);
		return -1;
	}

	@Override
	public boolean hasEnchantment(TagKey<Enchantment> enchantmentTag) {
		for (Holder<Enchantment> enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(this.level.registryAccess(), enchantment, enchantmentTag)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag) {
		for (Holder<Enchantment> enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(this.level.registryAccess(), enchantment, enchantmentTag)) {
				return this.enchantments.getLevel(enchantment);
			}
		}
		return -1;
	}

	@Override
	public void setEnchantments(ItemEnchantments enchantments) {
		this.enchantments = enchantments;
	}

	@Override
	public boolean hideGlint() {
		return this.hideGlint;
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("Enchantments")) {
			ItemEnchantments.CODEC
					.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("Enchantments")))
					.resultOrPartial()
					.ifPresent(enchantments -> this.enchantments = enchantments);
			this.enchantments = ItemEnchantments.CODEC.parse(NbtOps.INSTANCE, tag.get("Enchantments")).result().orElse(null);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (this.enchantments != null) {
			ItemEnchantments.CODEC
					.encodeStart(NbtOps.INSTANCE, this.enchantments)
					.resultOrPartial()
					.ifPresent(enchantments -> tag.put("Enchantments", enchantments));
		}
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);
		ItemEnchantments enchantments = componentInput.get(DataComponents.ENCHANTMENTS);
		if (enchantments != null) {
			this.enchantments = enchantments;
		}
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder pComponents) {
		super.collectImplicitComponents(pComponents);
		pComponents.set(DataComponents.ENCHANTMENTS, this.getEnchantments());
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);

	}

	//Sync stuff
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
		var tag = packet.getTag();
		if (tag != null) {
			handleUpdateTag(tag, registries);

			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 3);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (level != null) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 3);
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		loadAdditional(tag, registries);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag, this.level.registryAccess());
		return tag;
	}
}
