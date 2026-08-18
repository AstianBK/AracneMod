package com.astianbk.arachnemod.client.layer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.AracneModClient;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.entity.OrbEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.joml.Matrix4f;

public class MarkSilentLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
    protected static final Identifier[] FRAMES_HEX = new Identifier[]{
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_0.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_1.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_2.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_3.png")
    };
    public MarkSilentLayer(RenderLayerParent<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, LivingEntityRenderState livingEntityRenderState, float v, float v1) {
        if (livingEntityRenderState.getRenderData(AracneModClient.EFFECT)!=null){
            MobEffectInstance instance = livingEntityRenderState.getRenderData(AracneModClient.EFFECT);
            if (instance.is(NRegistry.SILENT_HEX)){
                for (int k = 0 ; k < instance.getAmplifier()+1 ; k++){
                    poseStack.pushPose();
                    double sin = Mth.sin((livingEntityRenderState.ageInTicks+k*120.0F)/30.0F);
                    double cos = Mth.cos((livingEntityRenderState.ageInTicks+k*120.0F)/30.0F);


                    ItemStackRenderState renderState = new ItemStackRenderState();
                    poseStack.translate(-1.25*sin,0,1.25*cos);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));

                    drawSlash(poseStack.last(), Minecraft.getInstance().renderBuffers().bufferSource(),0,1,livingEntityRenderState.ageInTicks);
                    renderState.submit(poseStack,submitNodeCollector,i, OverlayTexture.NO_OVERLAY,livingEntityRenderState.outlineColor);
                    poseStack.popPose();
                }
            }

        }

    }
    private void drawSlash(PoseStack.Pose pose, MultiBufferSource bufferSource, int light, float width, float ageInTicks) {
        Matrix4f poseMatrix = pose.pose();
        int frame = (int) (ageInTicks * 0.4f % 4.0F);

        float halfWidth = width * 0.5f;

        float frameHeight = 1.0F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        VertexConsumer consumer2 = bufferSource.getBuffer(RenderTypes.entityCutout(FRAMES_HEX[frame]));

        consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer2.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer2.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer2.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);

        VertexConsumer consumer1 = bufferSource.getBuffer(RenderTypes.entityTranslucentEmissive(FRAMES_HEX[frame]));

        consumer1.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        consumer1.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0F,1F,0F);
        VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.eyes(FRAMES_HEX[frame]));

        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(0F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, -halfWidth).setColor(255,255,255,255).setUv(1F, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(1F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);
        consumer.addVertex(poseMatrix, -halfWidth, -0.1f, halfWidth).setColor(255,255,255,255).setUv(0F, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(10000000).setNormal(pose,0F,1F,0F);

    }

}
