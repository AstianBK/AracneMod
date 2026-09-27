package com.tbk.voidweaver.server.goal;

import com.tbk.voidweaver.server.cap.ArachneAttachment;
import com.tbk.voidweaver.server.cap.data.BlessingData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;

public class SpiderTargetEnemyGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public SpiderTargetEnemyGoal(Spider mob) {
            super(mob, (Class<T>) Player.class, true);
        }

        public boolean canUse() {
            float br = this.mob.getLightLevelDependentMagicValue();
            return !(br >= 0.5F) && super.canUse() && target instanceof Player player && !ArachneAttachment.get(player).orElseGet(null).blessingIsActive(BlessingData.BlessingType.ARACHNE_ALLIE);
        }
    }