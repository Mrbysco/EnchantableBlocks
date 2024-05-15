package com.mrbysco.enchantableblocks.block.blockentity;

import com.google.common.collect.Lists;
import com.mrbysco.enchantableblocks.menu.EnchantedBeaconMenu;
import com.mrbysco.enchantableblocks.mixin.BeaconBeamSectionAccessor;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.TagHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EnchantedBeaconBlockEntity extends BeaconBlockEntity implements IEnchantable {
	protected boolean hideGlint;
	protected ListTag enchantmentTag = null;
	protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

	public EnchantedBeaconBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModRegistry.ENCHANTED_BEACON_BLOCK_ENTITY.get();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, EnchantedBeaconBlockEntity blockEntity) {
		int xPos = pos.getX();
		int yPos = pos.getY();
		int zPos = pos.getZ();
		BlockPos blockpos;
		if (blockEntity.lastCheckY < yPos) {
			blockpos = pos;
			blockEntity.checkingBeamSections = Lists.newArrayList();
			blockEntity.lastCheckY = pos.getY() - 1;
		} else {
			blockpos = new BlockPos(xPos, blockEntity.lastCheckY + 1, zPos);
		}

		BeaconBlockEntity.BeaconBeamSection beamSection = blockEntity.checkingBeamSections.isEmpty() ? null : blockEntity.checkingBeamSections.get(blockEntity.checkingBeamSections.size() - 1);
		int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, xPos, zPos);

		for (int i1 = 0; i1 < 10 && blockpos.getY() <= height; ++i1) {
			BlockState blockstate = level.getBlockState(blockpos);
			float[] colorMultiplier = blockstate.getBeaconColorMultiplier(level, blockpos, pos);
			if (colorMultiplier != null) {
				if (blockEntity.checkingBeamSections.size() <= 1) {
					beamSection = new BeaconBlockEntity.BeaconBeamSection(colorMultiplier);
					blockEntity.checkingBeamSections.add(beamSection);
				} else if (beamSection != null) {
					if (Arrays.equals(colorMultiplier, beamSection.getColor())) {
						((BeaconBeamSectionAccessor) beamSection).invokeIncreaseHeight();
					} else {
						beamSection = new BeaconBlockEntity.BeaconBeamSection(new float[]{(beamSection.getColor()[0] + colorMultiplier[0]) / 2.0F, (beamSection.getColor()[1] + colorMultiplier[1]) / 2.0F, (beamSection.getColor()[2] + colorMultiplier[2]) / 2.0F});
						blockEntity.checkingBeamSections.add(beamSection);
					}
				}
			} else {
				if (beamSection == null || blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK)) {
					blockEntity.checkingBeamSections.clear();
					blockEntity.lastCheckY = height;
					break;
				}

				((BeaconBeamSectionAccessor) beamSection).invokeIncreaseHeight();
			}

			blockpos = blockpos.above();
			++blockEntity.lastCheckY;
		}

		int levels = blockEntity.levels;
		if (level.getGameTime() % 80L == 0L) {
			if (!blockEntity.beamSections.isEmpty()) {
				blockEntity.levels = updateBase(level, xPos, yPos, zPos);
			}

			if (blockEntity.levels > 0 && !blockEntity.beamSections.isEmpty()) {
				applyEffects(blockEntity, level, pos, blockEntity.levels, blockEntity.primaryPower, blockEntity.secondaryPower);
				playSound(level, pos, SoundEvents.BEACON_AMBIENT);
			}
		}

		if (blockEntity.lastCheckY >= height) {
			blockEntity.lastCheckY = level.getMinBuildHeight() - 1;
			boolean flag = levels > 0;
			blockEntity.beamSections = blockEntity.checkingBeamSections;
			if (!level.isClientSide) {
				boolean flag1 = blockEntity.levels > 0;
				if (!flag && flag1) {
					playSound(level, pos, SoundEvents.BEACON_ACTIVATE);

					for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, (new AABB((double) xPos, (double) yPos, (double) zPos, (double) xPos, (double) (yPos - 4), (double) zPos)).inflate(10.0D, 5.0D, 10.0D))) {
						CriteriaTriggers.CONSTRUCT_BEACON.trigger(serverplayer, blockEntity.levels);
					}
				} else if (flag && !flag1) {
					playSound(level, pos, SoundEvents.BEACON_DEACTIVATE);
				}
			}
		}
	}

	private static int updateBase(Level pLevel, int pX, int pY, int pZ) {
		int i = 0;

		for(int j = 1; j <= 4; i = j++) {
			int k = pY - j;
			if (k < pLevel.getMinBuildHeight()) {
				break;
			}

			boolean flag = true;

			for(int l = pX - j; l <= pX + j && flag; ++l) {
				for(int i1 = pZ - j; i1 <= pZ + j; ++i1) {
					if (!pLevel.getBlockState(new BlockPos(l, k, i1)).is(BlockTags.BEACON_BASE_BLOCKS)) {
						flag = false;
						break;
					}
				}
			}

			if (!flag) {
				break;
			}
		}

		return i;
	}

	private static void applyEffects(EnchantedBeaconBlockEntity blockEntity, Level level, BlockPos pos, int levels, @Nullable MobEffect primary, @Nullable MobEffect secondary) {
		if (!level.isClientSide && primary != null) {
			boolean visible = !blockEntity.hasEnchantment(ModEnchantments.CONCEALED.get());

			double range = levels * 10 + 10;
			if (blockEntity.hasEnchantment(ModEnchantments.RANGED.get())) {
				int enchantmentLevel = blockEntity.getEnchantmentLevel(ModEnchantments.RANGED.get());
				//Adjust the range based on the level of the enchantment
				range *= 1 + (enchantmentLevel * 0.5);
			}

			int amplifier = 0;
			if (levels >= 4 && primary == secondary) {
				amplifier = 1;
			}

			int duration = (9 + levels * 2) * 20;
			AABB aabb = (new AABB(pos)).inflate(range).expandTowards(0.0D, level.getHeight(), 0.0D);
			List<Player> players = level.getEntitiesOfClass(Player.class, aabb);

			for (Player player : players) {
				player.addEffect(new MobEffectInstance(primary, duration, amplifier, true, visible));
			}

			if (levels >= 4 && primary != secondary && secondary != null) {
				for (Player player : players) {
					player.addEffect(new MobEffectInstance(secondary, duration, 0, true, visible));
				}
			}

		}
	}

	@Override
	@Nullable
	public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		return BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName()) ?
				new EnchantedBeaconMenu(containerId, inventory, this.dataAccess, ContainerLevelAccess.create(this.level, this.getBlockPos())) : null;
	}

	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	@Override
	public boolean hasEnchantment(Enchantment enchantment) {
		return this.enchantments.containsKey(enchantment);
	}

	@Override
	public int getEnchantmentLevel(Enchantment enchantment) {
		if (this.hasEnchantment(enchantment))
			return this.enchantments.get(enchantment);
		return -1;
	}

	@Override
	public boolean hasEnchantment(TagKey<Enchantment> enchantmentTag) {
		for (Enchantment enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(enchantment, enchantmentTag)) {
				return true;
			}
		}
		return this.enchantments.containsKey(enchantmentTag);
	}

	@Override
	public int getEnchantmentLevel(TagKey<Enchantment> enchantmentTag) {
		for (Enchantment enchantment : this.enchantments.keySet()) {
			if (TagHelper.matchesTag(enchantment, enchantmentTag)) {
				return this.enchantments.get(enchantment);
			}
		}
		return -1;
	}

	@Override
	public void setEnchantments(ListTag enchantmentTags) {
		this.enchantmentTag = enchantmentTags;
		this.updateEnchantmentMap();
	}

	@Override
	public ListTag getEnchantmentsTag() {
		return this.enchantmentTag;
	}

	@Override
	public void updateEnchantmentMap() {
		this.enchantments.clear();
		if (this.enchantmentTag != null) {
			EnchantmentHelper.deserializeEnchantments(this.enchantmentTag).forEach((enchantment, integer) -> {
				if (enchantment != null) {
					this.enchantments.put(enchantment, integer);
				}
			});
			this.hideGlint = this.hasEnchantment(ModEnchantments.GLINTLESS.get());
		}
	}

	@Override
	public boolean hideGlint() {
		return this.hideGlint;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Enchantments")) {
			this.enchantmentTag = tag.getList("Enchantments", Tag.TAG_COMPOUND);
			this.updateEnchantmentMap();
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.enchantmentTag != null)
			tag.put("Enchantments", enchantmentTag);
	}

	//Sync stuff
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		if (packet.getTag() != null)
			load(packet.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag);
		return tag;
	}
}
