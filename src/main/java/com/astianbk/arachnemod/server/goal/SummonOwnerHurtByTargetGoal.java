package com.astianbk.arachnemod.server.goal;

import com.astianbk.arachnemod.server.entity.SummoneableSpiderEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class SummonOwnerHurtByTargetGoal extends TargetGoal {
    private final SummoneableSpiderEntity summon;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public SummonOwnerHurtByTargetGoal(SummoneableSpiderEntity summon) {
        super(summon, false);
        this.summon = summon;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        LivingEntity owner = this.summon.getOwner();
        if (owner == null) {
            return false;
        } else {
            this.ownerLastHurtBy = owner.getLastHurtByMob();
            int ts = owner.getLastHurtByMobTimestamp();
            return ts != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.summon.wantsToAttack(this.ownerLastHurtBy, owner);
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity owner = this.summon.getOwner();
        if (owner != null) {
            this.timestamp = owner.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}