package com.astianbk.arachnemod.client.render_state;

import com.astianbk.arachnemod.server.entity.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class VoidHopperRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public AnimationState emerge = new AnimationState();
    public AnimationState flee = new AnimationState();
    public AnimationState casting = new AnimationState();
    public VoidNeedleEntity.AttackPhase phase= VoidNeedleEntity.AttackPhase.CIRCLE;
    public VoidHopperRenderState(){

    }
}
