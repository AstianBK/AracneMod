package com.astianbk.aracnemod.client.renderer;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.client.ScarabRenderState;
import com.astianbk.aracnemod.client.model.ScarabModel;
import com.astianbk.aracnemod.server.ScarabEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class ScarabRenderer<T extends ScarabEntity,R extends ScarabRenderState,M extends ScarabModel<R>> extends LivingEntityRenderer<T,R,M> {
    public ScarabRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new ScarabModel<>(context.bakeLayer(ScarabModel.LAYER_LOCATION)), 1.0F);
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
    }



    @Override
    public void submit(R state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
