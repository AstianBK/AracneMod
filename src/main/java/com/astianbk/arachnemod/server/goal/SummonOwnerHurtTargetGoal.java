package com.astianbk.arachnemod.server.goal;

import com.astianbk.arachnemod.server.entity.SummoneableSpiderEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class SummonOwnerHurtTargetGoal extends TargetGoal {
    private final SummoneableSpiderEntity summon;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public SummonOwnerHurtTargetGoal(SummoneableSpiderEntity summon) {
        super(summon, false);
        this.summon = summon;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        LivingEntity owner = this.summon.getOwner();
        if (owner == null) {
            return false;
        } else {
            this.ownerLastHurt = owner.getLastHurtMob();
            int ts = owner.getLastHurtMobTimestamp();
            return ts != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && this.summon.wantsToAttack(this.ownerLastHurt, owner);
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity owner = this.summon.getOwner();
        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
