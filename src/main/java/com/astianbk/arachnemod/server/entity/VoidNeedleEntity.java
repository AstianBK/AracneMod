package com.astianbk.arachnemod.server.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;

import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class VoidNeedleEntity extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> STUN = SynchedEntityData.defineId(VoidNeedleEntity.class, EntityDataSerializers.BOOLEAN);
    private Vec3 moveTargetPoint;
    private @Nullable BlockPos anchorPoint;
    private AttackPhase attackPhase;
    public int idleResetTimer = 0;
    public Vec3 direction = Vec3.ZERO;

    public AnimationState idle = new AnimationState();
    public AnimationState change = new AnimationState();
    public AnimationState changeLoop = new AnimationState();

    public AnimationState stun = new AnimationState();
    private int chargeTick = 0;
    private int changeTick = 0;
    private int stunTick = 0;
    @Nullable private Vec3 chargeControlPoint;
    @Nullable private Vec3 chargeEndPoint;
    private int swoopStage;
    @Nullable private Vec3 swoopStart;
    public VoidNeedleEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveTargetPoint = Vec3.ZERO;
        this.attackPhase = AttackPhase.CIRCLE;
        this.xpReward = 5;
        this.moveControl = new VoidNeedleEntityMoveControl(this);
        this.lookControl = new VoidNeedleEntityLookControl(this);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.ATTACK_KNOCKBACK,20)
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.ATTACK_DAMAGE, 12.0);
    }

    protected BodyRotationControl createBodyControl() {
        return new VoidNeedleEntityBodyRotationControl(this);
    }

    public int getUniqueFlapTickOffset() {
        return this.getId() * 3;
    }

    @Override
    public boolean shouldDiscardFriction() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.attackPhase != AttackPhase.CIRCLE
                    && !this.hasValidSwoopTarget()) {

                this.cancelSwoop();
                this.setTarget(null);
            }

            if (this.attackPhase == AttackPhase.PREPARE_SWOOP){
                this.changeTick++;
                if (this.changeTick > 20){
                    this.changeTick=0;
                    this.attackPhase = AttackPhase.SWOOP;
                }
            }
            if (this.entityData.get(STUN)){
                this.stunTick++;
                if (this.stunTick>=60){
                    this.stunTick = 0;
                    this.entityData.set(STUN,false);
                    this.level().broadcastEntityEvent(this,(byte) 16);
                }
            }
            if (VoidNeedleEntity.this.attackPhase == AttackPhase.SWOOP){
                this.chargeTick++;
                boolean stun = false;
                for (Entity living : VoidNeedleEntity.this.level().getEntities(VoidNeedleEntity.this,VoidNeedleEntity.this.getBoundingBox().inflate(1.0F), e->{
                    if (e instanceof LivingEntity && !e.is(EntityTypeTags.ARTHROPOD)){
                        return VoidNeedleEntity.this.getBoundingBox().inflate(1).intersects(e.getBoundingBox());
                    }
                    return false;
                })){
                    if (VoidNeedleEntity.this.doHurtTarget(((ServerLevel) level()), living)){
                        VoidNeedleEntity.this.heal(3.0F);
                    }else {
                        if (living instanceof Player){
                            if (((LivingEntity)living).isBlocking()){
                                stun = true;
                            }
                        }
                        break;
                    }
                    if (!VoidNeedleEntity.this.isSilent()) {
                        VoidNeedleEntity.this.level().levelEvent(1039, VoidNeedleEntity.this.blockPosition(), 0);
                    }
                }
                if (stun){
                    VoidNeedleEntity.this.entityData.set(STUN,true);
                    VoidNeedleEntity.this.level().broadcastEntityEvent(VoidNeedleEntity.this,(byte) 8);
                }
                if (this.chargeTick>70){
                    this.chargeTick = 0;
                    VoidNeedleEntity.this.attackPhase = AttackPhase.CIRCLE;
                    this.level().broadcastEntityEvent(this,(byte) 16);
                }
            }
        }

        if (this.level().isClientSide()){
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX(), this.getY() , this.getZ(), 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() , this.getY(), this.getZ() , 0.0, 0.0, 0.0);

            this.setupAnimation();
        }
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new VoidNeedleEntityAttackStrategyGoal());
        this.goalSelector.addGoal(3, new VoidNeedleEntityCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new VoidNeedleEntityAttackPlayerTargetGoal());
    }

    public void setupAnimation(){
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 25;
            this.change.stop();
            this.idle.start(this.tickCount);
        }
        if (this.attackPhase == AttackPhase.SWOOP){
            this.idle.stop();
            this.changeLoop.animateWhen(true,this.tickCount);
        }
    }

    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    public boolean onClimbable() {
        return false;
    }

    public void travel(Vec3 input) {
        this.travelFlying(input, 0.2F);
    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        this.anchorPoint = this.blockPosition().above(10);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.anchorPoint = (BlockPos)input.read("anchor_pos", BlockPos.CODEC).orElse((BlockPos) null);
    }

    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.storeNullable("anchor_pos", BlockPos.CODEC, this.anchorPoint);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.changeTick = 0;
            this.change.start(this.tickCount);
            this.idle.stop();
            this.idleResetTimer = 18;
        }else if (id == 8){
            this.idle.stop();
            this.change.stop();
            this.changeLoop.stop();
            this.stunTick = 0;
            this.stun.start(this.tickCount);
            this.idleResetTimer = 60;
        }else if (id == 16){
            this.idle.start(this.tickCount);
            this.change.stop();
            this.changeLoop.stop();
            this.stun.stop();
            this.idleResetTimer = 25;
        } else if (id == 32){
            for (int i = 0 ; i < 15 ; i++){
                Minecraft.getInstance().particleEngine.createParticle(new DustParticleOptions(16711680,167116802.0F),getRandomX(0.5F),getY(),getRandomZ(0.5F),0.0F,0.05F,0.0F);
            }
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(STUN,false);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return this.attackPhase != AttackPhase.SWOOP && super.canCollideWith(entity);
    }

    private boolean canAttack(ServerLevel level, LivingEntity target, TargetingConditions targetConditions) {
        return targetConditions.test(level, this, target);
    }

    private boolean hasPassedPointB() {
        if (this.swoopStart == null || this.chargeControlPoint == null) {
            return false;
        }

        Vec3 ab = this.chargeControlPoint.subtract(this.swoopStart);
        Vec3 am = this.position().subtract(this.swoopStart);
        return am.dot(ab) >= ab.lengthSqr();
    }

    private boolean hasValidSwoopTarget() {
        LivingEntity target = this.getTarget();

        if (target == null || !target.isAlive()) {
            return false;
        }

        if (target instanceof Player player) {
            return !player.isCreative() && !player.isSpectator();
        }

        return true;
    }

    private void cancelSwoop() {
        this.setDeltaMovement(Vec3.ZERO);

        this.attackPhase = AttackPhase.CIRCLE;
        this.chargeTick = 0;
        this.changeTick = 0;
        this.swoopStage = 0;

        this.swoopStart = null;
        this.chargeControlPoint = null;
        this.chargeEndPoint = null;

        this.anchorPoint = this.blockPosition().above(6);

        this.level().broadcastEntityEvent(this, (byte) 16);
    }
    public enum AttackPhase {
        CIRCLE,
        PREPARE_SWOOP,
        SWOOP;

        AttackPhase() {
        }
    }
    private static class VoidNeedleEntityLookControl extends LookControl {
        public VoidNeedleEntityLookControl(Mob mob) {
            super(mob);
        }

        public void tick() {
        }
    }

    private class VoidNeedleEntityBodyRotationControl extends BodyRotationControl {
        public VoidNeedleEntityBodyRotationControl(Mob mob) {
            super(mob);
        }

        public void clientTick() {
            VoidNeedleEntity.this.yHeadRot = VoidNeedleEntity.this.yBodyRot;
            VoidNeedleEntity.this.yBodyRot = VoidNeedleEntity.this.getYRot();
        }
    }

    private class VoidNeedleEntityAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        private VoidNeedleEntityAttackStrategyGoal() {
            super();
        }

        public boolean canUse() {
            if (VoidNeedleEntity.this.entityData.get(STUN) || VoidNeedleEntity.this.attackPhase != AttackPhase.CIRCLE){
                return false;
            }
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            return target != null && VoidNeedleEntity.this.canAttack(getServerLevel(VoidNeedleEntity.this.level()), target, TargetingConditions.DEFAULT);
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);

        }

        public void stop() {

        }

        public void tick() {
            if (VoidNeedleEntity.this.attackPhase != AttackPhase.CIRCLE) {
                return;
            }

            if (--this.nextSweepTick > 0) {
                return;
            }

            this.nextSweepTick = this.adjustedTickDelay(60);

            Vec3 launchPoint = this.findLaunchPoint();
            if (launchPoint == null) {
                return;
            }

            Vec3 pointA = VoidNeedleEntity.this.position();

            Vec3 ab = launchPoint.subtract(pointA);
            if (ab.lengthSqr() < 0.001D) {
                return;
            }

            VoidNeedleEntity.this.swoopStart = pointA;
            VoidNeedleEntity.this.chargeControlPoint = launchPoint;
            VoidNeedleEntity.this.chargeEndPoint = launchPoint.add(ab.normalize().scale(20.0D)).add(0,10,0);

            VoidNeedleEntity.this.swoopStage = 0;
            VoidNeedleEntity.this.moveTargetPoint = launchPoint;
            VoidNeedleEntity.this.attackPhase = AttackPhase.PREPARE_SWOOP;
            VoidNeedleEntity.this.level().broadcastEntityEvent(VoidNeedleEntity.this, (byte) 4);

        }

        @Nullable
        private Vec3 findLaunchPoint() {
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            if (target == null) return null;

            return target.position();
        }
    }


    private class VoidNeedleEntityCircleAroundAnchorGoal extends VoidNeedleEntity.VoidNeedleEntityMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        private VoidNeedleEntityCircleAroundAnchorGoal() {
            super();

        }

        public boolean canUse() {
            if (VoidNeedleEntity.this.entityData.get(STUN)){
                return false;
            }
            return VoidNeedleEntity.this.getTarget() == null || VoidNeedleEntity.this.attackPhase == VoidNeedleEntity.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 10.0F + VoidNeedleEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + VoidNeedleEntity.this.random.nextFloat() * 3.0F;
            this.clockwise = VoidNeedleEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (VoidNeedleEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + VoidNeedleEntity.this.random.nextFloat() * 9.0F;
            }

            if (VoidNeedleEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 35.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (VoidNeedleEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = VoidNeedleEntity.this.random.nextFloat() * 2.0F * 3.1415927F;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (VoidNeedleEntity.this.moveTargetPoint.y < VoidNeedleEntity.this.getY() && !VoidNeedleEntity.this.level().isEmptyBlock(VoidNeedleEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (VoidNeedleEntity.this.moveTargetPoint.y > VoidNeedleEntity.this.getY() && !VoidNeedleEntity.this.level().isEmptyBlock(VoidNeedleEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }
        }


        private void selectNext() {
            if (VoidNeedleEntity.this.anchorPoint == null) {
                VoidNeedleEntity.this.anchorPoint = VoidNeedleEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * 0.017453292F;
            VoidNeedleEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(VoidNeedleEntity.this.anchorPoint).add((double)(this.distance * Mth.cos((double)this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin((double)this.angle)));
        }

    }

    private class VoidNeedleEntityAttackPlayerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting;
        private int nextScanTick;

        private VoidNeedleEntityAttackPlayerTargetGoal() {
            super();
            this.attackTargeting = TargetingConditions.forCombat().range(64.0);
            this.nextScanTick = reducedTickDelay(20);
        }

        @Override
        public void stop() {
            LivingEntity target = VoidNeedleEntity.this.getTarget();

            if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
                VoidNeedleEntity.this.cancelSwoop();
                VoidNeedleEntity.this.setTarget(null);
            }
        }

        public boolean canUse() {
            if (VoidNeedleEntity.this.entityData.get(STUN)){
                return false;
            }
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                ServerLevel level = getServerLevel(VoidNeedleEntity.this.level());
                List<Player> players = level.getNearbyPlayers(this.attackTargeting, VoidNeedleEntity.this, VoidNeedleEntity.this.getBoundingBox().inflate(16.0, 64.0, 16.0));
                if (!players.isEmpty()) {
                    players.sort(Collections.reverseOrder(Comparator.comparing(Entity::getY)));
                    Iterator var3 = players.iterator();

                    while(var3.hasNext()) {
                        Player player = (Player)var3.next();
                        if (VoidNeedleEntity.this.canAttack(level, player, TargetingConditions.DEFAULT)) {
                            VoidNeedleEntity.this.setTarget(player);
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        public boolean canContinueToUse() {
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            return target != null ? VoidNeedleEntity.this.canAttack(getServerLevel(VoidNeedleEntity.this.level()), target, TargetingConditions.DEFAULT) : false;
        }
    }

    private abstract class VoidNeedleEntityMoveTargetGoal extends Goal {
        public VoidNeedleEntityMoveTargetGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return VoidNeedleEntity.this.moveTargetPoint.distanceToSqr(VoidNeedleEntity.this.getX(), VoidNeedleEntity.this.getY(), VoidNeedleEntity.this.getZ()) < 4.0;
        }
    }

    private class VoidNeedleEntityMoveControl extends MoveControl {
        private float speed;

        public VoidNeedleEntityMoveControl(Mob mob) {
            super(mob);
            Objects.requireNonNull(VoidNeedleEntity.this);
            Objects.requireNonNull(VoidNeedleEntity.this);

            this.speed = 0.1F;
        }

        public void tick() {
            if (VoidNeedleEntity.this.entityData.get(STUN)){
                VoidNeedleEntity.this.stopInPlace();
                return;
            }
            double tdx = VoidNeedleEntity.this.moveTargetPoint.x - VoidNeedleEntity.this.getX();
            double tdy = VoidNeedleEntity.this.moveTargetPoint.y - VoidNeedleEntity.this.getY();
            double tdz = VoidNeedleEntity.this.moveTargetPoint.z - VoidNeedleEntity.this.getZ();
            double sd = Math.sqrt(tdx * tdx + tdz * tdz);



            Vec3 toTarget = VoidNeedleEntity.this.moveTargetPoint.subtract(VoidNeedleEntity.this.position());

            double horizontalDistance = Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);

            float yaw = (float) (Mth.atan2(toTarget.z, toTarget.x) * Mth.RAD_TO_DEG) - 90.0F;

            float pitch = (float) -(Mth.atan2(toTarget.y, horizontalDistance) * Mth.RAD_TO_DEG);

            VoidNeedleEntity.this.setYRot(yaw);
            VoidNeedleEntity.this.setYBodyRot(yaw);
            VoidNeedleEntity.this.setYHeadRot(yaw);
            VoidNeedleEntity.this.setXRot(pitch);
            if (VoidNeedleEntity.this.attackPhase == AttackPhase.PREPARE_SWOOP){
                VoidNeedleEntity.this.stopInPlace();

                return;
            }
            if (VoidNeedleEntity.this.attackPhase==AttackPhase.SWOOP){
                if (VoidNeedleEntity.this.swoopStage == 1 && toTarget.lengthSqr() <= 1.0D) {
                    VoidNeedleEntity.this.cancelSwoop();
                    return;
                }

                if (VoidNeedleEntity.this.swoopStage == 0 && VoidNeedleEntity.this.hasPassedPointB() && VoidNeedleEntity.this.chargeEndPoint != null) {
                    VoidNeedleEntity.this.swoopStage = 1;
                    VoidNeedleEntity.this.moveTargetPoint = VoidNeedleEntity.this.chargeEndPoint;
                    VoidNeedleEntity.this.level().broadcastEntityEvent(VoidNeedleEntity.this,(byte) 16);
                }



                VoidNeedleEntity.this.setDeltaMovement(toTarget.normalize().scale(2.0D));
                return;
            }


            Vec3 movement = VoidNeedleEntity.this.getDeltaMovement();
            if (Math.abs(sd) > 9.999999747378752E-6) {
                double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
                float prev = VoidNeedleEntity.this.getYRot();
                float angle = (float)Mth.atan2(tdz, tdx);
                float a = Mth.wrapDegrees(VoidNeedleEntity.this.getYRot() + 90.0F);
                float b = Mth.wrapDegrees(angle * 57.295776F);
                VoidNeedleEntity.this.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
                VoidNeedleEntity.this.yBodyRot = VoidNeedleEntity.this.getYRot();
                if (Mth.degreesDifferenceAbs(prev, VoidNeedleEntity.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float xRotD = (float)(-(Mth.atan2(-tdy, sd) * 180.0 / 3.1415927410125732));
                VoidNeedleEntity.this.setXRot(xRotD);
                float moveAngle = VoidNeedleEntity.this.getYRot() + 90.0F;
                double txd = (double)(this.speed * Mth.cos((double)(moveAngle * 0.017453292F))) * Math.abs(tdx / sd2);
                double tzd = (double)(this.speed * Mth.sin((double)(moveAngle * 0.017453292F))) * Math.abs(tdz / sd2);
                double tyd = (double)(this.speed * Mth.sin((double)(xRotD * 0.017453292F))) * Math.abs(tdy / sd2);

                VoidNeedleEntity.this.setDeltaMovement(movement.add((new Vec3(txd, tyd, tzd))).subtract(movement).scale(0.2));
            }
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BABY_NAUTILUS_HURT_ON_LAND;
    }

}
