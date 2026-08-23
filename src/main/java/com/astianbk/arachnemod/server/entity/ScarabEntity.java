package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.goal.FleeBlockLightGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class ScarabEntity extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(ScarabEntity.class,EntityDataSerializers.BOOLEAN);
    public int idleResetTimer = 0;
    public int attackTimer = 0;
    public AnimationState idle = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState bite = new AnimationState();
    public int combo = 0;

    public ScarabEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.26F)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.ARMOR_TOUGHNESS, 5.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0);
    }
    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {

    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1,new NearestAttackableTargetGoal<>(this, Player.class,true){
            @Override
            public boolean canUse() {
                return super.canUse();
            }
            protected void findTarget() {
                ServerLevel level = getServerLevel(this.mob);
                if (this.targetType != Player.class) {
                    this.target = level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (entity) -> {
                        return true;
                    }), this.getTargetConditions(), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
                } else {
                    this.target = level.getNearestPlayer(this.getTargetConditions(), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
                }

            }
            private TargetingConditions getTargetConditions() {
                return this.targetConditions.range(this.getFollowDistance()).selector((living,level)->{
                    return level.getBrightness(LightLayer.BLOCK,living.blockPosition())<=0;
                });
            }
        });
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4,new MeleeAttackGoal(this,1.0D,false){
            @Override
            protected void checkAndPerformAttack(LivingEntity target) {
                if (this.canPerformAttack(target)) {
                    this.resetAttackCooldown();
                    this.mob.getNavigation().stop();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(getServerLevel(this.mob), target);
                    this.mob.getEntityData().set(ATTACKING,true);
                    attackTimer = 10;
                    this.mob.level().broadcastEntityEvent(this.mob,(byte) 4);
                }
            }

            @Override
            public void tick() {
                if (this.mob.getTarget()!=null && level().getBrightness(LightLayer.BLOCK,this.mob.getTarget().blockPosition())>0){
                    this.mob.setTarget(null);
                }
                super.tick();
            }

            @Override
            public boolean canUse() {
                return !entityData.get(ATTACKING) && super.canUse() && level().getBrightness(LightLayer.BLOCK,this.mob.getTarget().blockPosition())<=0;
            }
        });
        this.goalSelector.addGoal(1,new FleeBlockLightGoal(this,2.0F));
    }


    @Override
    public void tick() {
        super.tick();
        if (entityData.get(ATTACKING)){
            this.attackTimer--;
            if (this.attackTimer<=0){
                entityData.set(ATTACKING,false);
            }
        }
        LivingEntity target = getTarget();

        if (target != null) {
            double dx = target.getX() - getX();
            double dz = target.getZ() - getZ();

            float yaw = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;

            setYRot(yaw);
            setYBodyRot(yaw);
            setYHeadRot(yaw);
        }

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
            this.idleResetTimer = 10;

            this.combo++;
            if (this.combo == 3){
                this.combo = 0;
                this.bite.start(this.tickCount);
            }else if(this.combo == 2){
                this.attack2.start(this.tickCount);
            }else {
                this.attack1.start(this.tickCount);
            }
            this.getEntityData().set(ATTACKING,true);
            attackTimer = 20;
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(ATTACKING,false);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.5F, -2.0F);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.random.nextBoolean() ? NRegistry.SCARAB_IDLE1.get() : NRegistry.SCARAB_IDLE2.get();}
}
