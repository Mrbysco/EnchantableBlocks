package com.mrbysco.enchantableblocks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Axis;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.client.CustomRenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class EnchantedBedRenderer extends BedRenderer {

	private boolean renderEnchantment = true;

	public EnchantedBedRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(BedBlockEntity blockEntity, float partialTick, PoseStack poseStack,
	                   MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		if (blockEntity.getLevel() == null) return;
		if (blockEntity instanceof IEnchantable enchantable) {
			renderEnchantment = !enchantable.hideGlint();
		}

		Material material = Sheets.BED_TEXTURES[blockEntity.getColor().getId()];
		Level level = blockEntity.getLevel();
		if (level != null) {
			BlockState blockstate = blockEntity.getBlockState();
			DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighborcombineresult =
					DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType,
							BedBlock::getConnectedDirection, BedBlock.FACING, blockstate, level,
							blockEntity.getBlockPos(), (accessor, pos) -> false);
			int i = neighborcombineresult.apply(new BrightnessCombiner<>()).get(packedLight);
			this.renderPiece(poseStack, bufferSource, blockstate.getValue(BedBlock.PART) == BedPart.HEAD ?
					this.headRoot : this.footRoot, blockstate.getValue(BedBlock.FACING), material, i, packedOverlay, false);
		} else {
			this.renderPiece(poseStack, bufferSource, this.headRoot, Direction.SOUTH, material, packedLight, packedOverlay, false);
			this.renderPiece(poseStack, bufferSource, this.footRoot, Direction.SOUTH, material, packedLight, packedOverlay, true);
		}
	}

	private void renderPiece(PoseStack poseStack, MultiBufferSource bufferSource, ModelPart modelPart,
	                         Direction direction, Material material, int packedLight, int packedOverlay,
	                         boolean foot) {
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.5625F, foot ? -1.0F : 0.0F);
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		poseStack.translate(0.5F, 0.5F, 0.5F);
		poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
		poseStack.translate(-0.5F, -0.5F, -0.5F);
		VertexConsumer vertexconsumer;
		if (renderEnchantment) {
			PoseStack.Pose pose = poseStack.last();
			vertexconsumer = VertexMultiConsumer.create(
					new SheetedDecalTextureGenerator(bufferSource.getBuffer(CustomRenderType.GLINT), pose.pose(), pose.normal(), 0.0078125F),
					material.buffer(bufferSource, RenderType::entitySolid));
		} else {
			vertexconsumer = material.buffer(bufferSource, RenderType::entitySolid);
		}
		modelPart.render(poseStack, vertexconsumer, packedLight, packedOverlay);
		poseStack.popPose();
	}
}
