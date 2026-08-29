package com.astianbk.arachnemod.client.render_state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class ArachneLegRenderState extends LivingEntityRenderState {
    public AnimationState spawn = new AnimationState();
    public  boolean visible = false;
    public ArachneLegRenderState(){

    }
}
