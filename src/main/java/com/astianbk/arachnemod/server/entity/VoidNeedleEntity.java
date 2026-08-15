package com.astianbk.arachnemod.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.monster.Monster;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class VoidNeedleEntity extends Monster {
    public static final EntityDataAccessor<Boolean> CHARGE = SynchedEntityData.defineId(VoidNeedleEntity.class, EntityDataSerializers.BOOLEAN);
    public static final float FLAP_DEGREES_PER_TICK = 7.448451F;
    public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);

    private Vec3 moveTargetPoint;
    private @Nullable BlockPos anchorPoint;
    private AttackPhase attackPhase;
    public int idleResetTimer = 0;
    public Vec3 direction = Vec3.ZERO;
   
    public AnimationState idle = new AnimationState();
    public AnimationState change = new AnimationState();

    private int changeTick = 0;
    private int chargeTick = 0;

    public VoidNeedleEntity(EntityType<? extends VoidNeedleEntity> type, Level level) {
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
                .add(Attributes.ATTACK_KNOCKBACK,10)
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    protected BodyRotationControl createBodyControl() {
        return new VoidNeedleEntityBodyRotationControl(this);
    }

    public int getUniqueFlapTickOffset() {
        return this.getId() * 3;
    }

    @Override
    public boolean shouldDiscardFriction() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            float anim = Mth.cos((double)((float)(this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * 0.017453292F + 3.1415927F));
            float nextAnim = Mth.cos((double)((float)(this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * 0.017453292F + 3.1415927F));
            if (anim > 0.0F && nextAnim <= 0.0F) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
            }

            float width = this.getBbWidth() * 1.48F;
            float c = Mth.cos((double)(this.getYRot() * 0.017453292F)) * width;
            float s = Mth.sin((double)(this.getYRot() * 0.017453292F)) * width;
            float h = (0.3F + anim * 0.45F) * this.getBbHeight() * 2.5F;
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)c, this.getY() + (double)h, this.getZ() + (double)s, 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)c, this.getY() + (double)h, this.getZ() - (double)s, 0.0, 0.0, 0.0);
        }
        if (this.level().isClientSide()){
            this.setupAnimation();
        }

        if (this.attackPhase == AttackPhase.CHARGE){
            this.changeTick ++;
            if (this.changeTick > 18){
                this.attackPhase = AttackPhase.SWOOP;
                this.changeTick = 0;
                this.chargeTick = 0;
                if (this.level().isClientSide()){
                    this.change.stop();
                }
            }
            LivingEntity target = getTarget();

            if (target != null) {
                double dx = target.getX() - getX();
                double dy = target.getY() - getY();
                double dz = target.getZ() - getZ();

                float yaw = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
                float pitch = (float)(Mth.atan2(Math.sqrt(dx*dx+dz*dz),dy) * (180F / Math.PI));

                setYRot(yaw);
                setYBodyRot(yaw);
                setYHeadRot(yaw);
                setXRot(pitch);
            }
        }else if (attackPhase == AttackPhase.SWOOP){
            this.chargeTick++;
            if (this.chargeTick>=20){
                this.attackPhase = AttackPhase.CIRCLE;
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new VoidNeedleEntityAttackStrategyGoal());
        this.goalSelector.addGoal(2, new VoidNeedleEntitySweepAttackGoal());
        this.goalSelector.addGoal(3, new VoidNeedleEntityCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new VoidNeedleEntityAttackPlayerTargetGoal());
    }

    public void setupAnimation(){
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 25;
            this.change.stop();
            this.idle.start(this.tickCount);
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
            this.attackPhase = AttackPhase.CHARGE;
            this.change.start(this.tickCount);
            this.idle.stop();
            this.idleResetTimer = 18;
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(CHARGE,false);
    }

    private boolean canAttack(ServerLevel level, LivingEntity target, TargetingConditions targetConditions) {
        return targetConditions.test(level, this, target);
    }

    public enum AttackPhase {
        CIRCLE,
        CHARGE,
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
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            return target != null && VoidNeedleEntity.this.canAttack(getServerLevel(VoidNeedleEntity.this.level()), target, TargetingConditions.DEFAULT);
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            VoidNeedleEntity.this.attackPhase = VoidNeedleEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            if (VoidNeedleEntity.this.anchorPoint != null) {
                VoidNeedleEntity.this.anchorPoint = VoidNeedleEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, VoidNeedleEntity.this.anchorPoint).above(10 + VoidNeedleEntity.this.random.nextInt(20));
            }

        }

        public void tick() {
            if (VoidNeedleEntity.this.attackPhase == VoidNeedleEntity.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
//                    VoidNeedleEntity.this.attackPhase = AttackPhase.SWOOP;
//                    this.setAnchorAboveTarget();
//                    this.nextSweepTick = this.adjustedTickDelay((8 + VoidNeedleEntity.this.random.nextInt(4)) * 20);
//                    VoidNeedleEntity.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + VoidNeedleEntity.this.random.nextFloat() * 0.1F);
                }
            }
        }

        private void setAnchorAboveTarget() {
            if (VoidNeedleEntity.this.anchorPoint != null) {
                VoidNeedleEntity.this.anchorPoint = VoidNeedleEntity.this.getTarget().blockPosition().above(20 + VoidNeedleEntity.this.random.nextInt(20));
                if (VoidNeedleEntity.this.anchorPoint.getY() < VoidNeedleEntity.this.level().getSeaLevel()) {
                    VoidNeedleEntity.this.anchorPoint = new BlockPos(VoidNeedleEntity.this.anchorPoint.getX(), VoidNeedleEntity.this.level().getSeaLevel() + 1, VoidNeedleEntity.this.anchorPoint.getZ());
                }
            }

        }
    }

    private class VoidNeedleEntitySweepAttackGoal extends VoidNeedleEntity.VoidNeedleEntityMoveTargetGoal {
        private static final int CAT_SEARCH_TICK_DELAY = 20;
        private boolean isScaredOfCat;
        private int catSearchTick;

        private VoidNeedleEntitySweepAttackGoal() {
            super();

            Objects.requireNonNull(VoidNeedleEntity.this);
            Objects.requireNonNull(VoidNeedleEntity.this);
        }

        public boolean canUse() {
            return VoidNeedleEntity.this.getTarget() != null && VoidNeedleEntity.this.attackPhase == VoidNeedleEntity.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                if (target instanceof Player) {
                    Player player = (Player)target;
                    if (target.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }

                if (!this.canUse()) {
                    return false;
                } else {
                    if (VoidNeedleEntity.this.tickCount > this.catSearchTick) {
                        this.catSearchTick = VoidNeedleEntity.this.tickCount + 20;
                        List<Cat> cats = VoidNeedleEntity.this.level().getEntitiesOfClass(Cat.class, VoidNeedleEntity.this.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);
                        Iterator var4 = cats.iterator();

                        while(var4.hasNext()) {
                            Cat cat = (Cat)var4.next();
                            cat.hiss();
                        }

                        this.isScaredOfCat = !cats.isEmpty();
                    }

                    return !this.isScaredOfCat;
                }
            }
        }

        @Override
        public void start() {
            super.start();
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            if (target!=null){
                VoidNeedleEntity.this.moveTargetPoint = new Vec3(target.getX(), target.getY(0.5), target.getZ());
                VoidNeedleEntity.this.direction = VoidNeedleEntity.this.moveTargetPoint.subtract(VoidNeedleEntity.this.position()).normalize();
            }
        }

        public void stop() {
            VoidNeedleEntity.this.setTarget((LivingEntity)null);
            VoidNeedleEntity.this.attackPhase = VoidNeedleEntity.AttackPhase.CIRCLE;
            VoidNeedleEntity.this.direction = Vec3.ZERO;
        }

        public void tick() {
            LivingEntity target = VoidNeedleEntity.this.getTarget();
            if (target != null) {
                if (VoidNeedleEntity.this.getBoundingBox().inflate(0.20000000298023224).intersects(target.getBoundingBox())) {
                    VoidNeedleEntity.this.doHurtTarget(getServerLevel(VoidNeedleEntity.this.level()), target);
                    VoidNeedleEntity.this.attackPhase = VoidNeedleEntity.AttackPhase.CIRCLE;
                    if (!VoidNeedleEntity.this.isSilent()) {
                        VoidNeedleEntity.this.level().levelEvent(1039, VoidNeedleEntity.this.blockPosition(), 0);
                    }
                } else if (VoidNeedleEntity.this.hurtTime > 0) {
                    VoidNeedleEntity.this.attackPhase = VoidNeedleEntity.AttackPhase.CIRCLE;
                }
            }
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
            return VoidNeedleEntity.this.getTarget() == null || VoidNeedleEntity.this.attackPhase == VoidNeedleEntity.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 5.0F + VoidNeedleEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + VoidNeedleEntity.this.random.nextFloat() * 9.0F;
            this.clockwise = VoidNeedleEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (VoidNeedleEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + VoidNeedleEntity.this.random.nextFloat() * 9.0F;
            }

            if (VoidNeedleEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
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
            if (VoidNeedleEntity.this.getTarget()!=null && VoidNeedleEntity.this.position().subtract(VoidNeedleEntity.this.getTarget().position()).y>10){
                VoidNeedleEntity.this.direction = VoidNeedleEntity.this.moveTargetPoint.subtract(VoidNeedleEntity.this.position()).normalize();
                VoidNeedleEntity.this.level().broadcastEntityEvent(VoidNeedleEntity.this,(byte) 4);
                VoidNeedleEntity.this.attackPhase = AttackPhase.CHARGE;
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
            Objects.requireNonNull(VoidNeedleEntity.this);
            Objects.requireNonNull(VoidNeedleEntity.this);

            this.attackTargeting = TargetingConditions.forCombat().range(64.0);
            this.nextScanTick = reducedTickDelay(20);
        }

        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
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

                return false;
            }
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
            if (VoidNeedleEntity.this.attackPhase == AttackPhase.CHARGE)return;

            if (VoidNeedleEntity.this.horizontalCollision) {
                VoidNeedleEntity.this.setYRot(VoidNeedleEntity.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }


            double tdx = VoidNeedleEntity.this.moveTargetPoint.x - VoidNeedleEntity.this.getX();
            double tdy = VoidNeedleEntity.this.moveTargetPoint.y - VoidNeedleEntity.this.getY();
            double tdz = VoidNeedleEntity.this.moveTargetPoint.z - VoidNeedleEntity.this.getZ();
            double sd = Math.sqrt(tdx * tdx + tdz * tdz);
            Vec3 movement = VoidNeedleEntity.this.getDeltaMovement();
            if (Math.abs(sd) > 9.999999747378752E-6) {
                double t = Mth.clamp((double) chargeTick / 20.0F, 0.0, 1.0);

                double parabola = 2.0 * t - 1.0;
                double enemyDist=0;
                if (VoidNeedleEntity.this.getTarget()!=null){
                    enemyDist = VoidNeedleEntity.this.getTarget().getY()-VoidNeedleEntity.this.getY();
                }
                double chargeYVelocity = -enemyDist* parabola / 20.0F;
                chargeYVelocity /= chargeYVelocity;
                double yRelativeScale = 1.0 - Math.abs(tdy * 0.699999988079071) / sd;
                tdx *= yRelativeScale;
                tdz *= yRelativeScale;
                sd = Math.sqrt(tdx * tdx + tdz * tdz);
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


                VoidNeedleEntity.this.setDeltaMovement(VoidNeedleEntity.this.attackPhase == AttackPhase.SWOOP ? new Vec3(VoidNeedleEntity.this.direction.x,-chargeYVelocity,VoidNeedleEntity.this.direction.z).scale(1.5F) :  movement.add((new Vec3(txd, tyd, tzd))).subtract(movement).scale(0.2));
            }
        }
    }

}
