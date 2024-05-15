package com.mrbysco.enchantableblocks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.client.CustomRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;

public class EnchantedBeaconRenderer extends BeaconRenderer {

	private static final RandomSource RANDOM = RandomSource.create();

	private final BlockRenderDispatcher blockRenderDispatcher;
	private boolean renderEnchantment = true;

	public EnchantedBeaconRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		this.blockRenderDispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(BeaconBlockEntity blockEntity, float partialTick, PoseStack poseStack,
	                   MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

		if (blockEntity.getLevel() == null) return;
		if (blockEntity instanceof IEnchantable enchantable) {
			renderEnchantment = !enchantable.hideGlint();
		}

		if (renderEnchantment) {
			poseStack.pushPose();
			PoseStack.Pose pose = poseStack.last();
			VertexConsumer consumer = new SheetedDecalTextureGenerator(bufferSource.getBuffer(CustomRenderType.GLINT), pose.pose(), pose.normal(), 0.0078125F);
			blockRenderDispatcher.renderBatched(
					blockEntity.getBlockState(),
					blockEntity.getBlockPos(),
					blockEntity.getLevel(),
					poseStack,
					consumer,
					true,
					RANDOM,
					ModelData.EMPTY,
					null);
			poseStack.popPose();
		}
	}
}
