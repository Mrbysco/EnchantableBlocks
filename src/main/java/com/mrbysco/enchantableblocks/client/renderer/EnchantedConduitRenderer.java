package com.mrbysco.enchantableblocks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mrbysco.enchantableblocks.block.blockentity.IEnchantable;
import com.mrbysco.enchantableblocks.client.CustomRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ConduitRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EnchantedConduitRenderer extends ConduitRenderer {

	private boolean renderEnchantment = true;

	public EnchantedConduitRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(ConduitBlockEntity blockEntity, float partialTick, PoseStack poseStack,
	                   MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		if (blockEntity.getLevel() == null) return;
		if (blockEntity instanceof IEnchantable enchantable) {
			renderEnchantment = !enchantable.hideGlint();
		}

		float f = (float) blockEntity.tickCount + partialTick;
		if (!blockEntity.isActive()) {
			float rotation = blockEntity.getActiveRotation(0.0F);
			VertexConsumer shellConsumer;
			if (renderEnchantment) {
				PoseStack.Pose pose = poseStack.last();
				shellConsumer = VertexMultiConsumer.create(
						new SheetedDecalTextureGenerator(bufferSource.getBuffer(CustomRenderType.GLINT), pose, 0.0078125F),
						SHELL_TEXTURE.buffer(bufferSource, RenderType::entitySolid));
			} else {
				shellConsumer = SHELL_TEXTURE.buffer(bufferSource, RenderType::entitySolid);
			}
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.mulPose((new Quaternionf()).rotationY(rotation * ((float) Math.PI / 180F)));
			this.shell.render(poseStack, shellConsumer, packedLight, packedOverlay);
			poseStack.popPose();
		} else {
			float rotation = blockEntity.getActiveRotation(partialTick) * (180F / (float) Math.PI);
			float f2 = Mth.sin(f * 0.1F) / 2.0F + 0.5F;
			f2 = f2 * f2 + f2;
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.3F + f2 * 0.2F, 0.5F);
			Vector3f vector3f = (new Vector3f(0.5F, 1.0F, 0.5F)).normalize();
			poseStack.mulPose((new Quaternionf()).rotationAxis(rotation * ((float) Math.PI / 180F), vector3f));
			this.cage.render(poseStack, ACTIVE_SHELL_TEXTURE.buffer(bufferSource, RenderType::entityCutoutNoCull), packedLight, packedOverlay);
			poseStack.popPose();
			int i = blockEntity.tickCount / 66 % 3;
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.5F, 0.5F);
			if (i == 1) {
				poseStack.mulPose((new Quaternionf()).rotationX(((float) Math.PI / 2F)));
			} else if (i == 2) {
				poseStack.mulPose((new Quaternionf()).rotationZ(((float) Math.PI / 2F)));
			}

			VertexConsumer windConsumer;
			if (renderEnchantment) {
				PoseStack.Pose pose = poseStack.last();
				windConsumer = VertexMultiConsumer.create(new SheetedDecalTextureGenerator(bufferSource.getBuffer(CustomRenderType.GLINT), pose, 0.0078125F),
						(i == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(bufferSource, RenderType::entityCutoutNoCull));
			} else {
				windConsumer = (i == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(bufferSource, RenderType::entityCutoutNoCull);
			}
			this.wind.render(poseStack, windConsumer, packedLight, packedOverlay);
			poseStack.popPose();
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.5F, 0.5F);
			poseStack.scale(0.875F, 0.875F, 0.875F);
			poseStack.mulPose((new Quaternionf()).rotationXYZ((float) Math.PI, 0.0F, (float) Math.PI));
			this.wind.render(poseStack, windConsumer, packedLight, packedOverlay);
			poseStack.popPose();
			Camera camera = this.renderer.camera;
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.3F + f2 * 0.2F, 0.5F);
			poseStack.scale(0.5F, 0.5F, 0.5F);
			float negYRot = -camera.getYRot();
			poseStack.mulPose((new Quaternionf()).rotationYXZ(negYRot * ((float) Math.PI / 180F), camera.getXRot() * ((float) Math.PI / 180F), (float) Math.PI));
			poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
			this.eye.render(poseStack, (blockEntity.isHunting() ? OPEN_EYE_TEXTURE : CLOSED_EYE_TEXTURE).buffer(bufferSource, RenderType::entityCutoutNoCull), packedLight, packedOverlay);
			poseStack.popPose();
		}
	}
}
