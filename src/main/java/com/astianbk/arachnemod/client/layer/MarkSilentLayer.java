package com.astianbk.arachnemod.client.layer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.AracneModClient;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import org.joml.Matrix4f;

import java.util.List;

public class MarkSilentLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
    public MarkSilentLayer(RenderLayerParent<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, LivingEntityRenderState livingEntityRenderState, float v, float v1) {
        List<ArachneAttachment.Hex> hexes = livingEntityRenderState.getRenderData(AracneModClient.HEXS);
        if (hexes==null)return;
        int k = 0 ;
        for (ArachneAttachment.Hex hex : hexes){
            poseStack.pushPose();
            double sin = Mth.sin((livingEntityRenderState.ageInTicks+k*120.0F)/30.0F);
            double cos = Mth.cos((livingEntityRenderState.ageInTicks+k*120.0F)/30.0F);

            poseStack.translate(-1.25*sin,0,1.25*cos);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

            draw(poseStack.last(), Minecraft.getInstance().renderBuffers().bufferSource(),0,1,livingEntityRenderState.ageInTicks,hex.getLocation());
            poseStack.popPose();
            k++;
        }

    }
    private void draw(PoseStack.Pose pose, MultiBufferSource bufferSource, int light, float width, float ageInTicks, Identifier location) {
        Matrix4f poseMatrix = pose.pose();
        int frame = (int) (ageInTicks * 0.4f % 4.0F);

        float halfWidth = width * 0.5f;

        float frameHeight = 1.0F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        VertexConsumer consumer2 = bufferSource.getBuffer(RenderTypes.entityCutout(location));

        consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);

        VertexConsumer consumer1 = bufferSource.getBuffer(RenderTypes.entityTranslucentEmissive(location));

        consumer1.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.eyes(location));

        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);

    }

}
