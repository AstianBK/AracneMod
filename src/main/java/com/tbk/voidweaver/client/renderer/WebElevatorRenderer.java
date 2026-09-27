package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.WebPartModel;
import com.tbk.voidweaver.client.model.WebElevatorModel;
import com.tbk.voidweaver.server.entity.WebElevatorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class WebElevatorRenderer<T extends WebElevatorEntity,M extends WebElevatorModel<T>> extends EntityRenderer<T,M> {
    protected M model;
    public WebElevatorRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = (M) new WebElevatorModel<>(context.bakeLayer(WebElevatorModel.LAYER_LOCATION));
//        this.addLayer(new EyesLayer<R, M>(this) {
//            @Override
//            public RenderType renderType() {
//                return RenderTypes.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
//            }
//        });
    }




    @Override
    public boolean shouldRender(T entity, Frustum culler, double camX, double camY, double camZ) {
        return true;
    }

    public ResourceLocation getTextureLocation(T r) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/web_elevator/web_elevator.png");
    }




    @Override
    public void render(
            T p_entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        super.render(
                p_entity,
                entityYaw,
                partialTick,
                poseStack,
                bufferSource,
                packedLight
        );

        this.model.setupAnim(p_entity);

        poseStack.pushPose();

        poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
        poseStack.translate(0.0F, -1.5F, 0.0F);

        // Modelo normal
        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        RenderType.entityCutout(getTextureLocation(p_entity))
                ),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        // Emissive
        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        RenderType.entityTranslucentEmissive(
                                getTextureLocation(p_entity)
                        )
                ),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        // Eyes
        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        RenderType.eyes(getTextureLocation(p_entity))
                ),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        // Tela de araña/elevador
        ResourceLocation webTexture =
                ResourceLocation.fromNamespaceAndPath(
                        AracneMod.MODID,
                        "textures/entity/web_elevator/web_elevator.png"
                );

        for (int i = 0; i < 100; i++) {

            WebPartModel<WebElevatorEntity> model1 =
                    new WebPartModel<>(
                            Minecraft.getInstance()
                                    .getEntityModels()
                                    .bakeLayer(WebPartModel.LAYER_LOCATION)
                    );

            poseStack.pushPose();

            poseStack.translate(0.0F, 2.0F + i, 0.0F);

            // Modelo normal
            model1.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(
                            RenderType.entityCutout(webTexture)
                    ),
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );

            // Emissive
            model1.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(
                            RenderType.entityTranslucentEmissive(webTexture)
                    ),
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );

            // Eyes
            model1.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(
                            RenderType.eyes(webTexture)
                    ),
                    packedLight,
                    OverlayTexture.NO_OVERLAY
            );

            poseStack.popPose();
        }
    }
}
