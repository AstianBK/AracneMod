package com.astianbk.aracnemod.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class NeedleRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState bite = new AnimationState();

    public NeedleRenderState(){

    }
}
