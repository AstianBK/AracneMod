package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.NeedleRenderState;
import com.astianbk.arachnemod.client.ScarabRenderState;
import com.astianbk.arachnemod.client.model.ScarabModel;
import com.astianbk.arachnemod.client.model.VoidNeedleModel;
import com.astianbk.arachnemod.server.ScarabEntity;
import com.astianbk.arachnemod.server.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;

public class VoidNeedleRenderer<T extends VoidNeedleEntity,R extends NeedleRenderState,M extends VoidNeedleModel<R>> extends LivingEntityRenderer<T,R,M> {
    public VoidNeedleRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidNeedleModel<>(context.bakeLayer(VoidNeedleModel.LAYER_LOCATION)),1.0F);
    }

    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_needle/voidneedle.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle = entity.idle;
        state.change = entity.change;

    }

    @Override
    public R createRenderState() {
        return (R) new NeedleRenderState();
    }
}
