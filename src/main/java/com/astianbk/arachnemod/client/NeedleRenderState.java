package com.astianbk.arachnemod.client;

import com.astianbk.arachnemod.server.VoidNeedleEntity;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class NeedleRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public AnimationState loop_charge = new AnimationState();
    public AnimationState change = new AnimationState();

    public VoidNeedleEntity.AttackPhase phase= VoidNeedleEntity.AttackPhase.CIRCLE;
    public NeedleRenderState(){

    }
}
