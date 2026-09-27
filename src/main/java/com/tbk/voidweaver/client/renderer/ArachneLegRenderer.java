package com.tbk.voidweaver.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.ArachneLegModel;
import com.tbk.voidweaver.server.entity.ArachneLegEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ArachneLegRenderer<T extends ArachneLegEntity,M extends ArachneLegModel<T>> extends EntityRenderer<T> {
    protected M model;
    public ArachneLegRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = (M) new ArachneLegModel<>(context.bakeLayer(ArachneLegModel.LAYER_LOCATION));
//        this.addLayer(new EyesLayer<R, M>(this) {
//            @Override
//            public RenderType renderType() {
//                return RenderTypes.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
//            }
//        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/war_spider/warspider.png");
    }

    @Override
    public void render(T p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        if (p_entity.visible){
            // En 1.21.1 el modelo se anima directamente.
            this.model.setupAnim(p_entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

            poseStack.pushPose();

            poseStack.mulPose(
                    Axis.XP.rotationDegrees(180.0F)
            );

            poseStack.translate(
                    0.0F,
                    -1.5F,
                    0.0F
            );

            VertexConsumer vertexConsumer = bufferSource.getBuffer(
                    RenderType.entityCutout(
                            getTextureLocation(p_entity)
                    )
            );

            this.model.renderToBuffer(
                    poseStack,
                    vertexConsumer,
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );

            poseStack.popPose();
        }
    }

}
