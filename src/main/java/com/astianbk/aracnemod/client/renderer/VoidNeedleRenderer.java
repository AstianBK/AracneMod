package com.astianbk.aracnemod.client.renderer;

import com.astianbk.aracnemod.client.NeedleRenderState;
import com.astianbk.aracnemod.client.ScarabRenderState;
import com.astianbk.aracnemod.client.model.ScarabModel;
import com.astianbk.aracnemod.client.model.VoidNeedleModel;
import com.astianbk.aracnemod.server.ScarabEntity;
import com.astianbk.aracnemod.server.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;

public class VoidNeedleRenderer<T extends VoidNeedleEntity,R extends NeedleRenderState,M extends VoidNeedleModel<R>> extends LivingEntityRenderer<T,R,M> {
    public VoidNeedleRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidNeedleModel<>(context.bakeLayer(VoidNeedleModel.LAYER_LOCATION)),1.0F);
    }

    @Override
    public Identifier getTextureLocation(R r) {
        return null;
    }

    @Override
    public R createRenderState() {
        return null;
    }
}
