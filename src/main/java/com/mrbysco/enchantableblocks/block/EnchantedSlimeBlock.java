//package com.mrbysco.enchantableblocks.block;
//
//import com.mrbysco.enchantableblocks.block.blockentity.EnchantedBlockEntity;
//import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
//import com.mrbysco.enchantableblocks.block.blockentity.furnace.AbstractEnchantedFurnaceBlockEntity;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.SlimeBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.storage.loot.LootParams;
//import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//
//public class EnchantedSlimeBlock extends SlimeBlock implements EntityBlock { TODO: Re-visit Enchanted Slime Block. As it can't be pushed by pistons, it's not a good candidate for enchanting.
//	public EnchantedSlimeBlock(Properties pProperties) {
//		super(pProperties);
//	}
//
//	@Override
//	public boolean isSlimeBlock(BlockState state) {
//		return false;
//	}
//
//	@Nullable
//	@Override
//	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//		return new EnchantedBlockEntity(pos, state);
//	}
//
//	@Override
//	public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
//	}
//
//	public void updateEntityAfterFallOn(BlockGetter getter, BlockPos pos, Entity entity) {
//		if (entity.isSuppressingBounce()) {
//			entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
//			entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
//		} else {
//			double bounceFactor = 1.0D;
//			BlockEntity blockEntity = getter.getBlockEntity(pos);
//			if (blockEntity instanceof IEnchantable enchantable) {
//				if (enchantable.hasEnchantment(Enchantments.FALL_PROTECTION)) {
//					int level = enchantable.getEnchantmentLevel(Enchantments.FALL_PROTECTION);
//					bounceFactor = 1.0D + 0.15D * (level + 1);
//				}
//			}
//			this.bounceUp(entity, bounceFactor);
//		}
//	}
//
//	private void bounceUp(Entity entity, double bounceFactor) {
//		Vec3 vec3 = entity.getDeltaMovement();
//		if (vec3.y < 0.0D) {
//			double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
//			entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
//		}
//	}
//
//	@Override
//	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
//		ItemStack furnaceStack = new ItemStack(Blocks.SLIME_BLOCK);
//		if (level.getBlockEntity(pos) instanceof AbstractEnchantedFurnaceBlockEntity blockEntity) {
//			furnaceStack.getOrCreateTag().put("Enchantments", blockEntity.getEnchantmentsTag());
//		}
//		return furnaceStack;
//	}
//
//	@Override
//	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
//		float explosionResistance = super.getExplosionResistance(state, level, pos, explosion);
//		BlockEntity blockentity = level.getBlockEntity(pos);
//		if (blockentity instanceof IEnchantable enchantable) {
//			if (enchantable.hasEnchantment(Enchantments.BLAST_PROTECTION)) {
//				int enchantmentLevel = enchantable.getEnchantmentLevel(Enchantments.BLAST_PROTECTION);
//				explosionResistance *= ((enchantmentLevel + 1) * 30);
//			}
//		}
//		return explosionResistance;
//	}
//
//	@Override
//	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
//		BlockEntity blockentity = params.getParameter(LootContextParams.BLOCK_ENTITY);
//		if (blockentity instanceof IEnchantable enchantable) {
//			if (enchantable.hasEnchantment(Enchantments.VANISHING_CURSE)) {
//				return List.of();
//			}
//		}
//		return super.getDrops(state, params);
//	}
//
//	@Override
//	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
//		super.setPlacedBy(level, pos, state, placer, stack);
//		BlockEntity blockentity = level.getBlockEntity(pos);
//		if (blockentity instanceof IEnchantable enchantable) {
//			enchantable.setEnchantments(stack.getEnchantmentTags());
//		}
//	}
//}
