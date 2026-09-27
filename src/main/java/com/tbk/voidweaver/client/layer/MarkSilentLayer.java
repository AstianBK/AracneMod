package com.tbk.voidweaver.client.layer;

import com.tbk.voidweaver.server.cap.ArachneAttachment;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class MarkSilentLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public MarkSilentLayer(EntityRenderer<?> renderer) {
        super((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) renderer);
    }


    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        List<ArachneAttachment.Hex> hexes =
                ArachneAttachment.get(player)
                        .map(attachment -> attachment.hexes)
                        .orElse(null);

        if (hexes == null || hexes.isEmpty()) {
            return;
        }

        int k = 0;

        for (ArachneAttachment.Hex hex : hexes) {

            if (hex == null || hex.getLocation() == null) {
                k++;
                continue;
            }

            poseStack.pushPose();

            float sin = Mth.sin(
                    (ageInTicks + k * 120.0F) / 30.0F
            );

            float cos = Mth.cos(
                    (ageInTicks + k * 120.0F) / 30.0F
            );

            poseStack.translate(
                    -1.25F * sin,
                    0.0F,
                    1.25F * cos
            );

            poseStack.mulPose(
                    Axis.XP.rotationDegrees(-90.0F)
            );

            draw(
                    poseStack,
                    buffer,
                    packedLight,
                    1.0F,
                    ageInTicks,
                    hex.getLocation()
            );

            poseStack.popPose();

            k++;
        }
    }

    private void draw(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            float width,
            float ageInTicks,
            ResourceLocation location
    ) {

        int frame = (int) (ageInTicks * 0.4F % 4.0F);

        float halfWidth = width * 0.5F;

        float frameHeight = 1.0F;

        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        /*
         * Cutout
         */
        VertexConsumer cutout = buffer.getBuffer(
                RenderType.entityCutout(location)
        );

        drawQuad(
                poseStack,
                cutout,
                packedLight,
                halfWidth,
                v0,
                v1
        );

        /*
         * Emissive
         */
        VertexConsumer emissive = buffer.getBuffer(
                RenderType.entityTranslucentEmissive(location)
        );

        drawQuad(
                poseStack,
                emissive,
                packedLight,
                halfWidth,
                v0,
                v1
        );

        /*
         * Eyes
         */
        VertexConsumer eyes = buffer.getBuffer(
                RenderType.eyes(location)
        );

        drawQuad(
                poseStack,
                eyes,
                packedLight,
                halfWidth,
                v0,
                v1
        );
    }

    private void drawQuad(
            PoseStack poseStack,
            VertexConsumer consumer,
            int packedLight,
            float halfWidth,
            float v0,
            float v1
    ) {

        PoseStack.Pose pose = poseStack.last();

        consumer.addVertex(
                        pose,
                        -halfWidth,
                        -0.1F,
                        -halfWidth
                )
                .setColor(255, 255, 255, 255)
                .setUv(0.0F, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);

        consumer.addVertex(
                        pose,
                        halfWidth,
                        -0.1F,
                        -halfWidth
                )
                .setColor(255, 255, 255, 255)
                .setUv(1.0F, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);

        consumer.addVertex(
                        pose,
                        halfWidth,
                        -0.1F,
                        halfWidth
                )
                .setColor(255, 255, 255, 255)
                .setUv(1.0F, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);

        consumer.addVertex(
                        pose,
                        -halfWidth,
                        -0.1F,
                        halfWidth
                )
                .setColor(255, 255, 255, 255)
                .setUv(0.0F, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

}
