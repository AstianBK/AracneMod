package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.OrbRenderState;
import com.astianbk.arachnemod.server.OrbEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.quad.QuadTransforms;
import org.joml.Matrix4f;

import java.util.Map;

public class OrbRenderer<T extends OrbEntity,S extends OrbRenderState> extends EntityRenderer<T,S> {
    public static final Map<OrbEntity.Type,Identifier> TEXTURES = Map.of(OrbEntity.Type.BLESSING,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing.png"),
            OrbEntity.Type.CANCEL,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_cancel.png"),
            OrbEntity.Type.QUEST,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest.png"),
            OrbEntity.Type.QUEST_GET,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest_get.png"),
            OrbEntity.Type.QUEST_KILL,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest_kill.png"),
            OrbEntity.Type.QUEST_REPUTATION,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/questorb_reputation.png"));

    public OrbRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        poseStack.translate(0,0,-1F);
        drawSlash(poseStack.last(), Minecraft.getInstance().renderBuffers().bufferSource(),0,2,state.ageInTicks);
        poseStack.popPose();
    }

    private void drawSlash(PoseStack.Pose pose, MultiBufferSource bufferSource, int light, float width,float ageInTicks) {
        Matrix4f poseMatrix = pose.pose();
        int frame = (int) (ageInTicks * 0.4f % 8.0F);

        float halfWidth = width * 0.5f;

        float frameHeight = 48F / 384F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;
        VertexConsumer consumer1 = bufferSource.getBuffer(RenderTypes.entityTranslucentEmissive(TEXTURES.get(OrbEntity.Type.CANCEL)));

        consumer1.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.eyes(TEXTURES.get(OrbEntity.Type.CANCEL)));

        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
    }
    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override
    public S createRenderState() {
        return (S) new OrbRenderState();
    }
}
