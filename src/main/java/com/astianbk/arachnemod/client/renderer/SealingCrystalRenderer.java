package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.model.SealingCrystalModel;
import com.astianbk.arachnemod.client.render_state.SealingCrystalRenderState;
import com.astianbk.arachnemod.server.entity.SealingCrystalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SealingCrystalRenderer extends EntityRenderer<SealingCrystalEntity, SealingCrystalRenderState> {
    private static final Identifier CRYSTAL_LOCATION = Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/sealing_crystal.png");
    private static final Identifier GLOW_LOCATION = Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/sealing_crystal_glowing.png");

    private final SealingCrystalModel<SealingCrystalRenderState> model;

    public SealingCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SealingCrystalModel<>(context.bakeLayer(SealingCrystalModel.LAYER_LOCATION));
    }

    public void submit(SealingCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.scale(2.0F, 2.0F, 2.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        submitNodeCollector.submitModel(this.model, state, poseStack, CRYSTAL_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }


    @Override
    public SealingCrystalRenderState createRenderState() {
        return new SealingCrystalRenderState();
    }

    public void extractRenderState(SealingCrystalEntity entity,SealingCrystalRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle = entity.idle;
        state.ageInTicks = entity.time + partialTicks;
        state.showsBottom = entity.showsBottom();
        BlockPos beamTarget = entity.getBeamTarget();
        if (beamTarget != null) {
            state.beamOffset = Vec3.atCenterOf(beamTarget).subtract(entity.getPosition(partialTicks));
        } else {
            state.beamOffset = null;
        }
    }

    public boolean shouldRender(SealingCrystalEntity entity, Frustum culler, double camX, double camY, double camZ) {
        return super.shouldRender(entity, culler, camX, camY, camZ) || entity.getBeamTarget() != null;
    }
}
