package com.mrbysco.enchantableblocks.block.blockentity;

import com.mojang.serialization.Dynamic;
import com.mrbysco.enchantableblocks.mixin.ConduitBlockEntityAccessor;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import com.mrbysco.enchantableblocks.util.EnchantmentUtil;
import com.mrbysco.enchantableblocks.util.TagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedConduitBlockEntity extends ConduitBlockEntity implements IEnchantable {
	protected boolean hideGlint = false;
	protected ListTag enchantmentTag = null;
	protected ItemEnchantments enchantments = ItemEnchantments.EMPTY;

	public EnchantedConduitBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, EnchantedConduitBlockEntity blockEntity) {
		++blockEntity.tickCount;
		long i = level.getGameTime();
		List<BlockPos> effectBlocks = blockEntity.effectBlocks;
		if (i % 40L == 0L) {
			boolean flag = ConduitBlockEntityAccessor.invokeUpdateShape(level, pos, effectBlocks);
			if (flag != blockEntity.isActive) {
				SoundEvent soundevent = flag ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE;
				level.playSound((Player) null, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
			}

			blockEntity.isActive = flag;
			ConduitBlockEntityAccessor.invokeUpdateHunting(blockEntity, effectBlocks);
			if (flag) {
				applyEffects(level, pos, effectBlocks, blockEntity);
				updateDestroyTarget(level, pos, state, effectBlocks, blockEntity);
			}
		}

		if (blockEntity.isActive()) {
			if (i % 80L == 0L) {
				level.playSound((Player) null, pos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
			}

			if (i > blockEntity.nextAmbientSoundActivation) {
				blockEntity.nextAmbientSoundActivation = i + 60L + (long) level.getRandom().nextInt(40);
				level.playSound((Player) null, pos, SoundEvents.CONDUIT_AMBIENT_SHORT, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	private static void applyEffects(Level level, BlockPos pos, List<BlockPos> positions, EnchantedConduitBlockEntity blockEntity) {
		boolean concealed = blockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.CONCEALED));
		int blockCount = positions.size();
		int range = blockCount / 7 * 16;
		Holder<Enchantment> rangedHolder = EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.RANGED);
		if (blockEntity.hasEnchantment(rangedHolder)) {
			int enchantmentLevel = blockEntity.getEnchantmentLevel(rangedHolder);
			//Adjust the range based on the level of the enchantment
			range *= (int) (1 + (enchantmentLevel * 0.5));
		}
		int xPos = pos.getX();
		int yPos = pos.getY();
		int zPos = pos.getZ();
		AABB aabb = (new AABB(
				(double) xPos, (double) yPos, (double) zPos, (double)
				(xPos + 1), (double) (yPos + 1), (double) (zPos + 1)))
				.inflate((double) range)
				.expandTowards(0.0D, (double) level.getHeight(), 0.0D);
		List<Player> players = level.getEntitiesOfClass(Player.class, aabb);
		if (!players.isEmpty()) {
			for (Player player : players) {
				if (pos.closerThan(player.blockPosition(), (double) range) && player.isInWaterOrRain()) {
					player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, !concealed));
				}
			}
		}
	}

	private static void updateDestroyTarget(Level level, BlockPos pos, BlockState state, List<BlockPos> positions, EnchantedConduitBlockEntity blockEntity) {
		LivingEntity livingentity = blockEntity.destroyTarget;
		int blockCount = positions.size();
		if (blockCount < 42) {
			blockEntity.destroyTarget = null;
		} else if (blockEntity.destroyTarget == null && blockEntity.destroyTargetUUID != null) {
			blockEntity.destroyTarget = ConduitBlockEntityAccessor.invokeFindDestroyTarget(level, pos, blockEntity.destroyTargetUUID);
			blockEntity.destroyTargetUUID = null;
		} else if (blockEntity.destroyTarget == null) {
			List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, ConduitBlockEntityAccessor.invokeGetDestroyRangeAABB(pos), (livingEntity) -> {
				return livingEntity instanceof Enemy && livingEntity.isInWaterOrRain();
			});
			if (!list.isEmpty()) {
				blockEntity.destroyTarget = list.get(level.random.nextInt(list.size()));
			}
		} else if (!blockEntity.destroyTarget.isAlive() || !pos.closerThan(blockEntity.destroyTarget.blockPosition(), 8.0D)) {
			blockEntity.destroyTarget = null;
		}

		if (blockEntity.destroyTarget != null) {
			level.playSound((Player) null, blockEntity.destroyTarget.getX(), blockEntity.destroyTarget.getY(), blockEntity.destroyTarget.getZ(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
			float damage = 4.0F;
			Holder<Enchantment> sharpnessHolder = EnchantmentUtil.getEnchantmentHolder(level, Enchantments.SHARPNESS);
			if (blockEntity.hasEnchantment(sharpnessHolder)) {
				int enchantmentLevel = blockEntity.getEnchantmentLevel(sharpnessHolder);
				damage += 1 + (float) Math.max(0, enchantmentLevel - 1) * 0.5F;
			}
			blockEntity.destroyTarget.hurt(level.damageSources().magic(), damage);
		}

		if (livingentity != blockEntity.destroyTarget) {
			level.sendBlockUpdated(pos, state, state, 2);
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, EnchantedConduitBlockEntity blockEntity) {
		++blockEntity.tickCount;
		long i = level.getGameTime();
		List<BlockPos> list = blockEntity.effectBlocks;
		if (i % 40L == 0L) {
			blockEntity.isActive = ConduitBlockEntityAccessor.invokeUpdateShape(level, pos, list);
			ConduitBlockEntityAccessor.invokeUpdateHunting(blockEntity, list);
		}

		ConduitBlockEntityAccessor.invokeUpdateClientTarget(level, pos, blockEntity);
		animationTick(blockEntity, level, pos, list, blockEntity.destroyTarget, blockEntity.tickCount);
		if (blockEntity.isActive()) {
			++blockEntity.activeRotation;
		}
	}

	private static void animationTick(EnchantedConduitBlockEntity blockEntity, Level level, BlockPos pos, List<BlockPos> positions, @Nullable Entity entity, int tickCount) {
		if (blockEntity.hasEnchantment(EnchantmentUtil.getEnchantmentHolder(level, ModEnchantments.CONCEALED)))
			return;

		RandomSource random = level.random;
		double d0 = (double) (Mth.sin((float) (tickCount + 35) * 0.1F) / 2.0F + 0.5F);
		d0 = (d0 * d0 + d0) * (double) 0.3F;
		Vec3 vec3 = new Vec3((double) pos.getX() + 0.5D, (double) pos.getY() + 1.5D + d0, (double) pos.getZ() + 0.5D);

		for (BlockPos blockpos : positions) {
			if (random.nextInt(50) == 0) {
				BlockPos subtractPos = blockpos.subtract(pos);
				float f = -0.5F + random.nextFloat() + (float) subtractPos.getX();
				float f1 = -2.0F + random.nextFloat() + (float) subtractPos.getY();
				float f2 = -0.5F + random.nextFloat() + (float) subtractPos.getZ();
				level.addParticle(ParticleTypes.NAUTILUS, vec3.x, vec3.y, vec3.z, (double) f, (double) f1, (double) f2);
			}
		}

		if (entity != null) {
			Vec3 eyePos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
			float f3 = (-0.5F + random.nextFloat()) * (3.0F + entity.getBbWidth());
			float f4 = -1.0F + random.nextFloat() * entity.getBbHeight();
			float f5 = (-0.5F + random.nextFloat()) * (3.0F + entity.getBbWidth());
			Vec3 vec32 = new Vec3((double) f3, (double) f4, (double) f5);
			level.addParticle(ParticleTypes.NAUTILUS, eyePos.x, eyePos.y, eyePos.z, vec32.x, vec32.y, vec32.z);
		}
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModRegistry.ENCHANTED_CONDUIT_BLOCK_ENTITY.get();
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
