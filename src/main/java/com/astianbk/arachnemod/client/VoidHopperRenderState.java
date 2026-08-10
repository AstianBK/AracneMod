package com.astianbk.arachnemod.client;

import com.astianbk.arachnemod.server.VoidNeedleEntity;
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
