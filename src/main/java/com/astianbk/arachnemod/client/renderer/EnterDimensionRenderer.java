package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.model.EnterDimensionModel;
import com.astianbk.arachnemod.client.model.EnterDimensionModel;
import com.astianbk.arachnemod.client.render_state.EnterDimensionRenderState;
import com.astianbk.arachnemod.client.render_state.WebElevatorRenderState;
import com.astianbk.arachnemod.server.entity.EnterDimensionEntity;
import com.astianbk.arachnemod.server.entity.WebElevatorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class EnterDimensionRenderer<T extends EnterDimensionEntity,R extends EnterDimensionRenderState,M extends EnterDimensionModel<R>> extends EntityRenderer<T,R> {
    protected M model;
    public EnterDimensionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = (M) new EnterDimensionModel<>(context.bakeLayer(EnterDimensionModel.LAYER_LOCATION));
//        this.addLayer(new EyesLayer<R, M>(this) {
//            @Override
//            public RenderType renderType() {
//                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
//            }
//        });
    }


    @Override
    public R createRenderState() {
        return (R) new EnterDimensionRenderState();
    }


    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/war_spider/warspider.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle.copyFrom(entity.idle);
        state.take.copyFrom(entity.take);
        state.spawn.copyFrom(entity.spawn);
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
        submitNodeCollector.submitModel(this.model,state,poseStack,RenderTypes.entityCutout(getTextureLocation(state)),state.lightCoords, OverlayTexture.NO_OVERLAY,state.outlineColor,null);
        poseStack.popPose();
    }
}
