package com.astianbk.aracnemod.client;

import com.astianbk.aracnemod.server.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class VoidHopperRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState change = new AnimationState();
    public AnimationState bite = new AnimationState();
    public VoidNeedleEntity.AttackPhase phase= VoidNeedleEntity.AttackPhase.CIRCLE;
    public VoidHopperRenderState(){

    }
}
