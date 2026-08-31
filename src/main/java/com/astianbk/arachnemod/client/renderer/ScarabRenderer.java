package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.render_state.ScarabRenderState;
import com.astianbk.arachnemod.client.model.ScarabModel;
import com.astianbk.arachnemod.server.entity.ScarabEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class ScarabRenderer<T extends ScarabEntity,R extends ScarabRenderState,M extends ScarabModel<R>> extends MobRenderer<T,R,M> {
    public ScarabRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new ScarabModel<>(context.bakeLayer(ScarabModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new EyesLayer<R, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
            }
        });
    }


    @Override
    public R createRenderState() {
        return (R) new ScarabRenderState();
    }


    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_1.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle.copyFrom(entity.idle);
        state.attack1.copyFrom(entity.attack1);
        state.attack2.copyFrom(entity.attack2);
        state.bite.copyFrom(entity.bite);
        state.isAgressive = entity.isAggressive();
        Vec3 velocity = entity.getDeltaMovement();
        state.isMoving = velocity.x != 0.0D || velocity.z != 0.0D;
    }



    @Override
    public void submit(R state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
