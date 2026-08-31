package com.astianbk.arachnemod.server.entity;

import net.minecraft.world.entity.AnimationState;

public record ChargeAnimationState(AnimationState prepare, VoidScytheEntity.PhaseAttack preparePhase, int maxPrepareDuration, AnimationState loop , VoidScytheEntity.PhaseAttack loopPhase, int maxLoopDuration, AnimationState attack, VoidScytheEntity.PhaseAttack attackPhase, int maxAttackDuration){
    public void stopAll(){
        prepare.stop();
        loop.stop();
        attack.stop();
    }
    public void stopPrepare(){
        prepare.stop();
    }
    public void stopLoop(){
        loop.stop();
    }
    public void stopAttack(){
        attack.stop();
    }
}
