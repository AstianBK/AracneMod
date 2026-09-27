package com.tbk.voidweaver.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.EnterDimensionModel;

import com.tbk.voidweaver.server.entity.EnterDimensionEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EnterDimensionRenderer<T extends EnterDimensionEntity,M extends EnterDimensionModel<T>> extends EntityRenderer<T> {
    protected M model;
    public EnterDimensionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = (M) new EnterDimensionModel<>(context.bakeLayer(EnterDimensionModel.LAYER_LOCATION));

    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/war_spider/warspider.png");
    }


    @Override
    public void render(T p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        this.model.setupAnim(p_entity,0.0F,0.0F,partialTick+p_entity.tickCount,0.0F,0.0F);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
        poseStack.translate(0,-1.5F,0);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(p_entity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
