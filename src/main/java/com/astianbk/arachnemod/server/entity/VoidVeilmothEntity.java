package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

public class VoidVeilmothEntity extends PathfinderMob {
    public int idleResetTimer = 0;
    public AnimationState idle = new AnimationState();

    public VoidVeilmothEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.20F)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MAX_HEALTH, 2.0)
                .add(Attributes.FLYING_SPEED,0.3F)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(false);
        flyingPathNavigation.setRequiredPathLength(48.0F);
        return flyingPathNavigation;
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4,new VoidVeilmothEntityWanderGoal());
    }


    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {

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
            this.idle.start(this.tickCount);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {

        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
    }
    private class VoidVeilmothEntityWanderGoal extends Goal {
        VoidVeilmothEntityWanderGoal() {
            super();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return VoidVeilmothEntity.this.navigation.isDone() && VoidVeilmothEntity.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return VoidVeilmothEntity.this.navigation.isInProgress();
        }

        public void start() {
            Vec3 targetPos = this.findPos();
            if (targetPos != null) {
                VoidVeilmothEntity.this.navigation.moveTo(VoidVeilmothEntity.this.navigation.createPath(BlockPos.containing(targetPos), 1), 1.0);
            }

        }

        private @Nullable Vec3 findPos() {
            Vec3 wanderDirection;
            wanderDirection = VoidVeilmothEntity.this.getViewVector(0.0F);


            boolean xzDist = true;
            Vec3 groundBasedPosition = HoverRandomPos.getPos(VoidVeilmothEntity.this, 8, 7, wanderDirection.x, wanderDirection.z, 1.5707964F, 3, 1);
            return groundBasedPosition != null ? groundBasedPosition : AirAndWaterRandomPos.getPos(VoidVeilmothEntity.this, 8, 4, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866);
        }

        private int getWanderThreshold() {
            int distanceReduction = 16;
            return 48 - distanceReduction;
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return  SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }
}
