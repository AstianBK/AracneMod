package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.VoidHopperRenderState;
import com.astianbk.arachnemod.client.model.VoidHopperModel;
import com.astianbk.arachnemod.server.entity.VoidHopperEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class VoidHopperRenderer<T extends VoidHopperEntity,R extends VoidHopperRenderState,M extends VoidHopperModel<R>> extends LivingEntityRenderer<T,R,M> {
    public VoidHopperRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidHopperModel<>(context.bakeLayer(VoidHopperModel.LAYER_LOCATION)),1.0F);
        this.addLayer(new EyesLayer<R, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_hopper/voidhopper_eyes.png"));
            }
        });
    }

    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_hopper/voidhopper.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle = entity.idle;
        state.casting = entity.casting;
        state.flee = entity.flee;
        state.emerge = entity.emerge;
    }

    @Override
    public R createRenderState() {
        return (R) new VoidHopperRenderState();
    }
}
