package com.astianbk.arachnemod.client.render_state;

import com.astianbk.arachnemod.server.entity.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class EnterDimensionRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public AnimationState take = new AnimationState();
    public AnimationState spawn = new AnimationState();

    public EnterDimensionRenderState(){

    }
}
