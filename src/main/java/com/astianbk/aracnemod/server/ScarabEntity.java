package com.astianbk.aracnemod.server;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
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
    public int idleResetTimer = 0;
    public AnimationState idle = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState bite = new AnimationState();
    public int combo = 0;
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
        this.goalSelector.addGoal(4,new MeleeAttackGoal(this,2.0D,true){
            @Override
            protected void checkAndPerformAttack(LivingEntity target) {
                if (this.canPerformAttack(target)) {
                    this.resetAttackCooldown();
                    this.mob.getNavigation().stop();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(getServerLevel(this.mob), target);
                    this.mob.level().broadcastEntityEvent(this.mob,(byte) 4);
                }
            }

            @Override
            protected int getAttackInterval() {
                return adjustedTickDelay(40);
            }
        });
    }


    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()){
            this.setupAnimation();
        }
    }
    public void setupAnimation(){
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 40;
            this.attack2.stop();
            this.attack1.stop();
            this.bite.stop();
            this.idle.start(this.tickCount);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.idle.stop();
            this.idleResetTimer = 20;
            
            this.combo++;
            if (this.combo == 3){
                this.combo = 0;
                this.bite.start(this.tickCount);
            }else if(this.combo == 2){
                this.attack2.start(this.tickCount);
            }else {
                this.attack1.start(this.tickCount);
            }
        }
        super.handleEntityEvent(id);
    }
}
