package com.astianbk.arachnemod.client.render_state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class VoidScytheRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState attackLoop1 = new AnimationState();

    public AnimationState prepareAttack1 = new AnimationState();

    public AnimationState attack2 = new AnimationState();
    public AnimationState attackLoop2 = new AnimationState();

    public AnimationState prepareAttack2 = new AnimationState();
    public AnimationState counter = new AnimationState();
    public AnimationState counterLoop = new AnimationState();

    public AnimationState prepareCounter = new AnimationState();

    public AnimationState land = new AnimationState();
    public AnimationState jumpLoop = new AnimationState();

    public AnimationState jump = new AnimationState();
    public boolean isJump = false;
    public boolean isMoving = false;

    public VoidScytheRenderState(){

    }
}
