package com.mrbysco.enchantableblocks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Axis;
import com.mrbysco.enchantableblocks.block.EnchantedChestBlock;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.client.CustomRenderType;
import com.mrbysco.enchantableblocks.registry.ModEnchantments;
import com.mrbysco.enchantableblocks.registry.ModRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Calendar;

public class EnchantedChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
	private static final String BOTTOM = "bottom";
	private static final String LID = "lid";
	private static final String LOCK = "lock";
	private final ModelPart lid;
	private final ModelPart bottom;
	private final ModelPart lock;
	private boolean xmasTextures;

	private boolean renderEnchantment = true;

	public EnchantedChestRenderer(BlockEntityRendererProvider.Context context) {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
			this.xmasTextures = true;
		}

		ModelPart modelpart = context.bakeLayer(ModelLayers.CHEST);
		this.bottom = modelpart.getChild("bottom");
		this.lid = modelpart.getChild("lid");
		this.lock = modelpart.getChild("lock");
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack,
	                   MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

		if (blockEntity.getLevel() == null) return;

		if (blockEntity instanceof IEnchantable enchantable) {
			if (enchantable.hasEnchantment(ModEnchantments.GLINTLESS.get())) {
				renderEnchantment = false;
				return;
			}
		}

		Level level = blockEntity.getLevel();
		boolean flag = level != null;
		BlockState state = flag ? blockEntity.getBlockState() : ModRegistry.ENCHANTED_CHEST.get().defaultBlockState()
				.setValue(EnchantedChestBlock.FACING, Direction.SOUTH);
		Block block = state.getBlock();
		if (block instanceof AbstractChestBlock<?>) {
			poseStack.pushPose();
			float yRot = state.getValue(EnchantedChestBlock.FACING).toYRot();
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
			poseStack.translate(-0.5F, -0.5F, -0.5F);
			DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> result = DoubleBlockCombiner.Combiner::acceptNone;

			float f1 = result.apply(ChestBlock.opennessCombiner(blockEntity)).get(partialTick);
			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			int i = result.apply(new BrightnessCombiner<>()).applyAsInt(packedLight);
			Material material = this.getMaterial(blockEntity);
			PoseStack.Pose pose = poseStack.last();
			VertexConsumer vertexconsumer = VertexMultiConsumer.create(
					new SheetedDecalTextureGenerator(bufferSource.getBuffer(CustomRenderType.GLINT), pose.pose(), pose.normal(), 0.0078125F),
					material.buffer(bufferSource, RenderType::entityCutout)
			);
			this.render(poseStack, vertexconsumer, this.lid, this.lock, this.bottom, f1, i, packedOverlay);

			poseStack.popPose();
		}
	}

	private void render(PoseStack poseStack, VertexConsumer consumer, ModelPart lidPart, ModelPart lockPart,
	                    ModelPart bottomPart, float pLidAngle, int packedLight, int pPackedOverlay) {
		lidPart.xRot = -(pLidAngle * ((float) Math.PI / 2F));
		lockPart.xRot = lidPart.xRot;
		lidPart.render(poseStack, consumer, packedLight, pPackedOverlay);
		lockPart.render(poseStack, consumer, packedLight, pPackedOverlay);
		bottomPart.render(poseStack, consumer, packedLight, pPackedOverlay);
	}

	protected Material getMaterial(T blockEntity) {
		if (this.xmasTextures) {
			return Sheets.CHEST_XMAS_LOCATION;
		} else {
			return blockEntity instanceof TrappedChestBlockEntity ? Sheets.CHEST_TRAP_LOCATION : Sheets.CHEST_LOCATION;
		}
	}
}
