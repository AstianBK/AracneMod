package com.astianbk.aracnemod.server;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ScarabEntity extends PathfinderMob {
    public ScarabEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,20.0D)
                .add(Attributes.ATTACK_DAMAGE,5.0D)
                .add(Attributes.ATTACK_KNOCKBACK,0.0D)
                .add(Attributes.ARMOR,0.0D)
                .add(Attributes.ARMOR_TOUGHNESS,0.0D)
                .add(Attributes.MOVEMENT_EFFICIENCY,0.4D)
                .add(Attributes.MOVEMENT_SPEED,0.2D)
                .add(Attributes.FOLLOW_RANGE,15.0D)
                .add(Attributes.ATTACK_SPEED,0.5D)
                .add(Attributes.MAX_ABSORPTION,0.0)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE,6.0D)
                .add(Attributes.STEP_HEIGHT,2.0D)
                .add(Attributes.GRAVITY,1.0D)
                .add(Attributes.BURNING_TIME,0.0D)
                .add(Attributes.OXYGEN_BONUS,0.0D)
                .add(Attributes.SAFE_FALL_DISTANCE,100.0D)
                .add(Attributes.SCALE,1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1,new NearestAttackableTargetGoal<>(this, Player.class,false));
        this.goalSelector.addGoal(4,new MeleeAttackGoal(this,2.0D,true));
    }

}
