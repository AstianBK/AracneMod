package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.model.WebPartModel;
import com.astianbk.arachnemod.client.render_state.ScarabRenderState;
import com.astianbk.arachnemod.client.model.WebElevatorModel;
import com.astianbk.arachnemod.client.render_state.WebElevatorRenderState;
import com.astianbk.arachnemod.server.entity.WebElevatorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class WebElevatorRenderer<T extends WebElevatorEntity,R extends WebElevatorRenderState,M extends WebElevatorModel<R>> extends EntityRenderer<T,R> {
    protected M model;
    public WebElevatorRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = (M) new WebElevatorModel<>(context.bakeLayer(WebElevatorModel.LAYER_LOCATION));
//        this.addLayer(new EyesLayer<R, M>(this) {
//            @Override
//            public RenderType renderType() {
//                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
//            }
//        });
    }


    @Override
    public R createRenderState() {
        return (R) new WebElevatorRenderState();
    }


    @Override
    public boolean shouldRender(T entity, Frustum culler, double camX, double camY, double camZ) {
        return true;
    }

    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/web_elevator/web_elevator.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
//        state.idle.copyFrom(entity.idle);
//        state.attack1.copyFrom(entity.attack1);
//        state.attack2.copyFrom(entity.attack2);
//        state.bite.copyFrom(entity.bite);
    }



    @Override
    public void submit(R state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        this.model.setupAnim(state);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
        poseStack.translate(0,-1.5F,0);
        this.model.renderToBuffer(poseStack, Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.entityCutout(getTextureLocation(state))),state.lightCoords, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        for (int i = 0 ; i < 100 ; i ++){
            WebPartModel model1 = new WebPartModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(WebPartModel.LAYER_LOCATION));
            poseStack.pushPose();
            poseStack.translate(0,2+i,0);
            model1.renderToBuffer(poseStack,Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/web_elevator/web_elevator.png"))),state.lightCoords, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}
