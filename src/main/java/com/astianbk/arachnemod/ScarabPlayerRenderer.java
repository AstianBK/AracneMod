package com.astianbk.arachnemod;

import com.astianbk.arachnemod.client.model.ScarabPlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class ScarabPlayerRenderer<T extends Player,R extends AvatarRenderState,M extends ScarabPlayerModel<R>> extends LivingEntityRenderer<T,R,M> {
    public ScarabPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new ScarabPlayerModel<>(context.bakeLayer(ScarabPlayerModel.LAYER_LOCATION)), 1.0F);
    }


    @Override
    public R createRenderState() {
        return (R) new AvatarRenderState();
    }


    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_1.png");
    }


    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
//        state.idle.copyFrom(entity.idle);
//        state.attack1.copyFrom(entity.attack1);
//        state.attack2.copyFrom(entity.attack2);
//        state.bite.copyFrom(entity.bite);
    }
}
