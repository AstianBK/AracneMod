package com.astianbk.arachnemod.server.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.spider.Spider;

public class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public SpiderTargetGoal(Spider mob, Class<T> targetType) {
            super(mob, targetType, true);
        }

        public boolean canUse() {
            float br = this.mob.getLightLevelDependentMagicValue();
            return br >= 0.5F ? false : super.canUse();
        }
    }