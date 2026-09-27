package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.render_state.OrbRenderState;
import com.tbk.voidweaver.server.entity.OrbEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.Map;

public class OrbRenderer<T extends OrbEntity,S extends OrbRenderState> extends EntityRenderer<T,S> {
    public static final Map<OrbEntity.Type,ResourceLocation> TEXTURES = Map.ofEntries(
            Map.entry(OrbEntity.Type.BLESSING, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing.png")),
            Map.entry(OrbEntity.Type.CANCEL, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_cancel.png")),
            Map.entry(OrbEntity.Type.QUEST, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_quest.png")),
            Map.entry(OrbEntity.Type.QUEST_GET, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_quest_get.png")),
            Map.entry(OrbEntity.Type.QUEST_KILL, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_quest_kill.png")),
            Map.entry(OrbEntity.Type.QUEST_REPUTATION, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/questorb_reputation.png")),
            Map.entry(OrbEntity.Type.ARACHNE_MOVE, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_0.png")),
            Map.entry(OrbEntity.Type.ARACHNE_ANTI_FALL, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_1.png")),
            Map.entry(OrbEntity.Type.ARACHNE_ALLIE, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_3.png")),
            Map.entry(OrbEntity.Type.ARACHNE_FANG, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_2.png")),
            Map.entry(OrbEntity.Type.ARACHNE_INFECTION, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_4.png")),
            Map.entry(OrbEntity.Type.ARACHNE_PROTECTION, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_5.png")),
            Map.entry(OrbEntity.Type.ARACHNE_FORM, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/orbs/orb_blessing_0.png"))    );
    public static final Map<OrbEntity.Type,ResourceLocation> OFF = Map.of(
            OrbEntity.Type.ARACHNE_MOVE,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_0_off.png"),
            OrbEntity.Type.ARACHNE_ANTI_FALL,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_1_off.png"),
            OrbEntity.Type.ARACHNE_ALLIE,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_3_off.png"),
            OrbEntity.Type.ARACHNE_FANG,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_2_off.png"),
            OrbEntity.Type.ARACHNE_INFECTION,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_4_off.png"),
            OrbEntity.Type.ARACHNE_PROTECTION,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_5_off.png"),
            OrbEntity.Type.ARACHNE_FORM,ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/orbs/orb_blessing_0.png")
    );

    public OrbRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    protected boolean shouldShowName(T entity, double distanceToCameraSq) {
        return true;
    }
    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state,poseStack,submitNodeCollector,camera);
        poseStack.pushPose();
        poseStack.translate(0,1,0F);
        poseStack.mulPose(camera.orientation);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

        drawSlash(state,poseStack,poseStack.last(),submitNodeCollector, 2,state.ageInTicks,state.type);

        poseStack.popPose();
    }

    private void drawSlash(S state,PoseStack poseStack, PoseStack.Pose pose, SubmitNodeCollector bufferSource, float width, float ageInTicks, OrbEntity.Type type) {
        int frame = (int) (ageInTicks * 0.4f % 8.0F);

        float halfWidth = width * 0.5f;

        float frameHeight = 48F / 384F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        ResourceLocation location = state.lock? OFF.get(type) : TEXTURES.get(type);

        bufferSource.submitCustomGeometry(poseStack,RenderTypes.entityCutout(location),(pose1,consumer2)->{
            Matrix4f poseMatrix = pose1.pose();
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
        });


        bufferSource.submitCustomGeometry(poseStack,RenderTypes.entityTranslucentEmissive(location),(pose1,consumer2)->{
            Matrix4f poseMatrix = pose1.pose();
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
        });

        bufferSource.submitCustomGeometry(poseStack,RenderTypes.eyes(location),(pose1,consumer2)->{
            Matrix4f poseMatrix = pose1.pose();
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
            consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0).setNormal(pose,0F,1F,0F);
        });
    }
    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.lock = entity.isLock();
        state.type = entity.getOrbType();
    }

    @Override
    public S createRenderState() {
        return (S) new OrbRenderState();
    }
}
