package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.SealingCrystalModel;
import com.tbk.voidweaver.server.entity.SealingCrystalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SealingCrystalRenderer extends EntityRenderer<SealingCrystalEntity> {
    private static final ResourceLocation CRYSTAL_LOCATION = ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/sealing_crystal.png");
    private static final ResourceLocation GLOW_LOCATION = ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/sealing_crystal_glowing.png");

    private final SealingCrystalModel<SealingCrystalEntity> model;

    public SealingCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SealingCrystalModel<>(context.bakeLayer(SealingCrystalModel.LAYER_LOCATION));
    }

    @Override
    public void render(SealingCrystalEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
        poseStack.scale(2.0F, 2.0F, 2.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);

        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(this.model.renderType(CRYSTAL_LOCATION)),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        RenderType.entityTranslucentEmissive(CRYSTAL_LOCATION)
                ),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(
                        RenderType.eyes(CRYSTAL_LOCATION)
                ),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        super.render(
                p_entity,
                entityYaw,
                partialTick,
                poseStack,
                bufferSource,
                packedLight
        );
    }

    @Override
    public ResourceLocation getTextureLocation(SealingCrystalEntity sealingCrystalEntity) {
        return null;
    }

    public boolean shouldRender(SealingCrystalEntity entity, Frustum culler, double camX, double camY, double camZ) {
        return super.shouldRender(entity, culler, camX, camY, camZ) || entity.getBeamTarget() != null;
    }
}
