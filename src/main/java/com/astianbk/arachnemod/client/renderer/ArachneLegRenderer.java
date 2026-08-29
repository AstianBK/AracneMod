package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.model.ArachneLegModel;
import com.astianbk.arachnemod.client.model.EnterDimensionModel;
import com.astianbk.arachnemod.client.render_state.ArachneLegRenderState;
import com.astianbk.arachnemod.client.render_state.EnterDimensionRenderState;
import com.astianbk.arachnemod.server.entity.ArachneLegEntity;
import com.astianbk.arachnemod.server.entity.EnterDimensionEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ArachneLegRenderer<T extends ArachneLegEntity,R extends ArachneLegRenderState,M extends ArachneLegModel<R>> extends EntityRenderer<T,R> {
    protected M model;
    public ArachneLegRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = (M) new ArachneLegModel<>(context.bakeLayer(ArachneLegModel.LAYER_LOCATION));
//        this.addLayer(new EyesLayer<R, M>(this) {
//            @Override
//            public RenderType renderType() {
//                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
//            }
//        });
    }


    @Override
    public R createRenderState() {
        return (R) new ArachneLegRenderState();
    }


    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/war_spider/warspider.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.spawn.copyFrom(entity.spawn);
        state.visible = entity.visible;
        state.yRot = entity.getYRot();
//        state.attack1.copyFrom(entity.attack1);
//        state.attack2.copyFrom(entity.attack2);
//        state.bite.copyFrom(entity.bite);
    }



    @Override
    public void submit(R state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        if (state.visible){
            this.model.setupAnim(state);
            poseStack.pushPose();
            poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
            poseStack.translate(0,-1.5F,0);
            submitNodeCollector.submitModel(this.model,state,poseStack,RenderTypes.entityCutout(getTextureLocation(state)),state.lightCoords, OverlayTexture.NO_OVERLAY,state.outlineColor,null);
            poseStack.popPose();
        }

    }
}
