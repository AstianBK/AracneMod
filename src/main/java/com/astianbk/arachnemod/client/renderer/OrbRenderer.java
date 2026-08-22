package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.render_state.OrbRenderState;
import com.astianbk.arachnemod.server.entity.OrbEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

import java.util.Map;

public class OrbRenderer<T extends OrbEntity,S extends OrbRenderState> extends EntityRenderer<T,S> {
    public static final Map<OrbEntity.Type,Identifier> TEXTURES = Map.of(OrbEntity.Type.BLESSING,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing.png"),
            OrbEntity.Type.CANCEL,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_cancel.png"),
            OrbEntity.Type.QUEST,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest.png"),
            OrbEntity.Type.QUEST_GET,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest_get.png"),
            OrbEntity.Type.QUEST_KILL,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest_kill.png"),
            OrbEntity.Type.QUEST_REPUTATION,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/questorb_reputation.png"));
    public static final Map<OrbEntity.Type,Identifier> GLOW = Map.of(OrbEntity.Type.BLESSING,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing.png"),
            OrbEntity.Type.CANCEL,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_cancel_glow.png"),
            OrbEntity.Type.QUEST,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest.png"),
            OrbEntity.Type.QUEST_GET,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest_get.png"),
            OrbEntity.Type.QUEST_KILL,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_quest_kill.png"),
            OrbEntity.Type.QUEST_REPUTATION,Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/questorb_reputation.png"));

    public OrbRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0,1,0F);
        poseStack.mulPose(camera.orientation);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

        drawSlash(poseStack,poseStack.last(),submitNodeCollector,0,2,state.ageInTicks,state.type);

        poseStack.popPose();
    }

    private void drawSlash(PoseStack poseStack,PoseStack.Pose pose, SubmitNodeCollector bufferSource, int light, float width,float ageInTicks,OrbEntity.Type type) {
        int frame = (int) (ageInTicks * 0.4f % 8.0F);

        float halfWidth = width * 0.5f;

        float frameHeight = 48F / 384F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        Identifier location = TEXTURES.get(type);

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
    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.type = entity.getOrbType();
    }

    @Override
    public S createRenderState() {
        return (S) new OrbRenderState();
    }
}
