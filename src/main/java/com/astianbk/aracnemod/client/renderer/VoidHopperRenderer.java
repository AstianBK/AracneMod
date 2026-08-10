package com.astianbk.aracnemod.client.renderer;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.client.NeedleRenderState;
import com.astianbk.aracnemod.client.VoidHopperRenderState;
import com.astianbk.aracnemod.client.model.VoidHopperModel;
import com.astianbk.aracnemod.client.model.VoidNeedleModel;
import com.astianbk.aracnemod.server.VoidHopperEntity;
import com.astianbk.aracnemod.server.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;

public class VoidHopperRenderer<T extends VoidHopperEntity,R extends VoidHopperRenderState,M extends VoidHopperModel<R>> extends LivingEntityRenderer<T,R,M> {
    public VoidHopperRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidHopperModel<>(context.bakeLayer(VoidHopperModel.LAYER_LOCATION)),1.0F);
    }

    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_hopper/voidhopper.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle = entity.idle;
    }

    @Override
    public R createRenderState() {
        return (R) new VoidHopperRenderState();
    }
}
