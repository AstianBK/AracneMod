package com.astianbk.arachnemod.client.layer;

import com.astianbk.arachnemod.AracneModClient;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
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

            draw(poseStack,poseStack.last(), submitNodeCollector,0,1,livingEntityRenderState.ageInTicks,hex.getLocation());
            poseStack.popPose();
            k++;
        }

    }
    private void draw(PoseStack poseStack,PoseStack.Pose pose, SubmitNodeCollector bufferSource, int light, float width, float ageInTicks, Identifier location) {
        int frame = (int) (ageInTicks * 0.4f % 4.0F);

        float halfWidth = width * 0.5f;

        float frameHeight = 1.0F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        bufferSource.submitCustomGeometry(poseStack,RenderTypes.entityCutout(location),(pose1,consumer2)->{
            Matrix4f poseMatrix = pose1.pose();
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        });


        bufferSource.submitCustomGeometry(poseStack,RenderTypes.entityTranslucentEmissive(location),(pose1,consumer2)->{
            Matrix4f poseMatrix = pose1.pose();
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        });

        bufferSource.submitCustomGeometry(poseStack,RenderTypes.eyes(location),(pose1,consumer2)->{
            Matrix4f poseMatrix = pose1.pose();
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        });
    }

}
