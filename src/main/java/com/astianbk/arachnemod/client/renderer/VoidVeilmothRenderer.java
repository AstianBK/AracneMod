package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.render_state.VoidVeilmothRenderState;
import com.astianbk.arachnemod.client.model.VoidVeilmothModel;
import com.astianbk.arachnemod.server.entity.VoidVeilmothEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

public class VoidVeilmothRenderer<T extends VoidVeilmothEntity,R extends VoidVeilmothRenderState,M extends VoidVeilmothModel<R>> extends LivingEntityRenderer<T,R,M> {
    public VoidVeilmothRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidVeilmothModel<>(context.bakeLayer(VoidVeilmothModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new EyesLayer<R, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_veilmoth/void_veilmoth.png"));
            }
        });
    }


    @Override
    public R createRenderState() {
        return (R) new VoidVeilmothRenderState();
    }


    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_veilmoth/void_veilmoth.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle.copyFrom(entity.idle);
//        state.attack1.copyFrom(entity.attack1);
//        state.attack2.copyFrom(entity.attack2);
//        state.bite.copyFrom(entity.bite);
    }



    @Override
    public void submit(R state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
