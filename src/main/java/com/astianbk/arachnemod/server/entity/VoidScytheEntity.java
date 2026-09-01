package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.IntFunction;

public class VoidScytheEntity extends PathfinderMob {
    public static final EntityDataAccessor<Phase> PHASE = SynchedEntityData.defineId(VoidScytheEntity.class, NRegistry.PHASE_SERIALIZER.get());
    public static final EntityDataAccessor<PhaseAttack> PHASE_ATTACK = SynchedEntityData.defineId(VoidScytheEntity.class, NRegistry.PHASE_ATTACK_SERIALIZER.get());
    public int idleResetTimer = 0;
    public int durationPhaseTimer = 0;
    public AnimationState idle = new AnimationState();
    public ChargeAnimationState attack1 = new ChargeAnimationState(new AnimationState(),PhaseAttack.PREPARE_1,20,new AnimationState(),PhaseAttack.ATTACK_LOOP_1,2,new AnimationState(),PhaseAttack.ATTACK_1,12);
    public ChargeAnimationState attack2 = new ChargeAnimationState(new AnimationState(),PhaseAttack.PREPARE_2, 20, new AnimationState(),PhaseAttack.ATTACK_LOOP_2, 100, new AnimationState(),PhaseAttack.ATTACK_2, 6);
    public ChargeAnimationState counter = new ChargeAnimationState(new AnimationState(),PhaseAttack.PREPARE_COUNTER,7,new AnimationState(),PhaseAttack.COUNTER_LOOP,100,new AnimationState(),PhaseAttack.COUNTER,21);
    public ChargeAnimationState jump = new ChargeAnimationState(new AnimationState(),PhaseAttack.PREPARE_JUMP,10,new AnimationState(),PhaseAttack.JUMP_LOOP,100,new AnimationState(),PhaseAttack.LAND,20);
    public Vec3 targetPos = null;
    public int nextPhaseTimer = 0;
    public VoidScytheEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.ATTACK_DAMAGE, 15.0)
                .add(Attributes.ARMOR, 5.0)
                .add(Attributes.ARMOR_TOUGHNESS, 15.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.3F);

    }
    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {

    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1,new NearestAttackableTargetGoal<>(this, Player.class,true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2,new JumpToTargetGoal(this ));
        this.goalSelector.addGoal(2,new ChargedAttackGoal(this,2.5F));
        this.goalSelector.addGoal(4,new MeleeAttackGoal(this,1.0D,false){
            @Override
            protected void checkAndPerformAttack(LivingEntity target) {
                if (this.canPerformAttack(target) && VoidScytheEntity.this.getPhaseAttack()==PhaseAttack.NONE) {
                    this.resetAttackCooldown();
                    this.mob.getNavigation().stop();
                    VoidScytheEntity.this.setPhase(Phase.ATTACK_1);
                    VoidScytheEntity.this.setPhaseAttack(PhaseAttack.PREPARE_1);
                    this.mob.level().broadcastEntityEvent(this.mob,(byte) 4);
                }
            }

            @Override
            public void tick() {
                super.tick();
            }

            @Override
            public boolean canUse() {
                return VoidScytheEntity.this.getPhase()!=Phase.JUMP && (VoidScytheEntity.this.getPhase() == Phase.COUNTER || VoidScytheEntity.this.getPhase() == Phase.AROUND || VoidScytheEntity.this.getPhase() == Phase.ATTACK_1) && super.canUse();
            }
        });
    }



    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()){
            LivingEntity target = getTarget();

            if (target != null) {
                double dx = target.getX() - getX();
                double dz = target.getZ() - getZ();

                float yaw = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;

                setYRot(yaw);
                setYBodyRot(yaw);
                setYHeadRot(yaw);
            }
            if (this.getTarget()!=null){
                if (this.getPhase()!=Phase.JUMP && this.nextPhaseTimer++>400){
                    switch (this.level().getRandom().nextInt(0,3)){
                        case 0 ->{
                            this.setPhase(Phase.COUNTER);
                            this.setPhaseAttack(PhaseAttack.PREPARE_COUNTER);
                            this.level().broadcastEntityEvent(this,(byte) 16);
                        }
                        case 1 -> this.setPhase(Phase.ATTACK_2);
                    }
                    this.nextPhaseTimer = 0;
                }
            }
        }
        switch (getPhase()){
            case AROUND -> {
                if (this.level().isClientSide()){
                    this.setupAnimation();
                }
            }
            case JUMP -> {
                if (this.targetPos==null)return;
                if (this.getPhaseAttack() == PhaseAttack.PREPARE_JUMP){
                    this.durationPhaseTimer++;
                    if (this.durationPhaseTimer == 3){
                        double dx = this.targetPos.x - this.getX();
                        double dz = this.targetPos.z - this.getZ();

                        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                        if (horizontalDistance < 1.0E-7) {
                            this.setDeltaMovement(0.0, calculateVerticalVelocity(1), 0.0);
                            return;
                        }

                        double speed = 0.5;

                        double vx = dx / horizontalDistance * speed;
                        double vz = dz / horizontalDistance * speed;


                        double ticks = horizontalDistance / speed;
                        ticks = Math.max(ticks, 1.0);
                        double vy = calculateVerticalVelocity(ticks);
                        this.setDeltaMovement(vx, vy, vz);
                    }
                    if (this.durationPhaseTimer > 10){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.JUMP_LOOP);
                        if (this.level().isClientSide()){
                            jump.stopAll();
                            jump.loop().start(this.tickCount);
                        }
                    }
                }else if (this.getPhaseAttack() == PhaseAttack.JUMP_LOOP){
                    if (this.onGround()){
                        this.stopInPlace();
                        this.setPhaseAttack(PhaseAttack.LAND);
                        if (!this.level().isClientSide()){
                            this.level().broadcastEntityEvent(this,(byte) 64);
                            this.level().getEntities(this,getBoundingBox().inflate(3.5F)).forEach(living->this.doHurtTarget((ServerLevel) level(),living));

                        }
                    }
                }else if (this.getPhaseAttack() == PhaseAttack.LAND){
                    this.durationPhaseTimer++;
                    this.stopInPlace();
                    if (this.durationPhaseTimer > 20){
                        this.durationPhaseTimer = 0;
                        this.setPhase(Phase.AROUND);
                        this.setPhaseAttack(PhaseAttack.NONE);
                        if (!this.level().isClientSide()){
                            this.level().broadcastEntityEvent(this,(byte) 6);
                        }
                        this.targetPos = null;
                    }
                }
            }
            case ATTACK_1 -> {
                if (this.getPhaseAttack() == PhaseAttack.PREPARE_1){
                    this.durationPhaseTimer++;
                    if (this.durationPhaseTimer > attack1.maxPrepareDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.ATTACK_LOOP_1);
                        if (this.level().isClientSide()){
                            this.attack1.stopAll();
                            this.attack1.loop().start(this.tickCount);
                        }
                    }
                }else if (this.getPhaseAttack() == PhaseAttack.ATTACK_LOOP_1){
                    this.durationPhaseTimer++;
                    if (this.durationPhaseTimer > attack1.maxLoopDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.ATTACK_1);
                        if (this.level().isClientSide()){
                            this.attack1.stopAll();
                            this.attack1.attack().start(this.tickCount);
                        }

                    }
                }else if (this.getPhaseAttack() == PhaseAttack.ATTACK_1){
                    this.durationPhaseTimer++;
                    if (this.durationPhaseTimer > attack1.maxAttackDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.NONE);
                        this.setPhase(Phase.AROUND);
                        this.level().broadcastEntityEvent(this,(byte) 61);
                    }
                    if (!this.level().isClientSide() && this.durationPhaseTimer==6){
                        float coneAngle = 60.0F;
                        double range = 3.5D;

                        this.level().getEntities(this, this.getBoundingBox().inflate(range),
                                living -> {
                                    if (!(living instanceof LivingEntity)) {
                                        return false;
                                    }

                                    double dx = living.getX() - this.getX();
                                    double dz = living.getZ() - this.getZ();

                                    double distanceSqr = dx * dx + dz * dz;

                                    if (distanceSqr < 1.0E-7) {
                                        return true;
                                    }

                                    double angleToEntity = Math.toDegrees(Math.atan2(dz, dx)) - 90.0D;

                                    double angleDifference = Mth.wrapDegrees((float)(angleToEntity - this.getYRot()));

                                    return Math.abs(angleDifference) <= coneAngle;
                                }
                        ).forEach(living ->
                                this.doHurtTarget((ServerLevel) level(), living)
                        );
                    }
                }
            }
            case ATTACK_2 -> {
                this.durationPhaseTimer++;
                if (this.getPhaseAttack() == PhaseAttack.PREPARE_2){
                    if (this.durationPhaseTimer > attack2.maxPrepareDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.ATTACK_LOOP_2);
                        if (this.level().isClientSide()){
                            this.attack2.stopAll();
                            this.attack2.loop().start(this.tickCount);
                        }
                    }
                }else if (this.getPhaseAttack() == PhaseAttack.ATTACK_LOOP_2){
                    if (this.durationPhaseTimer > attack2.maxLoopDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.ATTACK_2);
                        if (this.level().isClientSide()){
                            this.attack2.stopAll();
                            this.attack2.attack().start(this.tickCount);
                        }
                    }
                }else if (this.getPhaseAttack() == PhaseAttack.ATTACK_2){
                    if (this.durationPhaseTimer > attack2.maxAttackDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.NONE);
                        this.setPhase(Phase.AROUND);
                        if (!this.level().isClientSide()){
                            this.level().getEntities(this,getBoundingBox().inflate(2.5F)).forEach(living->this.doHurtTarget((ServerLevel) level(),living));
                            this.level().broadcastEntityEvent(this,(byte) 61);
                        }
                    }
                }
            }
            case COUNTER -> {
                this.durationPhaseTimer++;
                if (this.getPhaseAttack() == PhaseAttack.PREPARE_COUNTER){
                    if (this.durationPhaseTimer > counter.maxPrepareDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.COUNTER_LOOP);
                        if (this.level().isClientSide()){
                            this.counter.stopAll();
                            this.counter.loop().start(this.tickCount);
                        }

                    }
                }else if (this.getPhaseAttack() == PhaseAttack.COUNTER_LOOP){
                    if (this.durationPhaseTimer > counter.maxLoopDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.NONE);
                        this.setPhase(Phase.AROUND);
                        if (this.level().isClientSide()){
                            this.counter.stopAll();
                            this.counter.attack().start(this.tickCount);
                        }
                    }
                }else if (this.getPhaseAttack() == PhaseAttack.COUNTER){
                    if (this.durationPhaseTimer > counter.maxAttackDuration()){
                        this.durationPhaseTimer = 0;
                        this.setPhaseAttack(PhaseAttack.NONE);
                        this.setPhase(Phase.AROUND);
                        if (!this.level().isClientSide()){
                            this.level().broadcastEntityEvent(this,(byte) 61);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.getPhaseAttack() == PhaseAttack.COUNTER_LOOP){
            Entity entity = source.getEntity();
            if (entity instanceof LivingEntity living && entity.distanceTo(this) < 4){
                living.hurtServer(level,damageSources().mobAttack(this),damage*4);
            }
            this.playSound(NRegistry.SCYTHE_PARRY.get(),2.0F,1.0F);
            this.durationPhaseTimer = 0;
            this.setPhaseAttack(PhaseAttack.COUNTER);
            this.level().broadcastEntityEvent(this,(byte) 5);
            return false;
        }

        if (this.getPhaseAttack() == PhaseAttack.NONE){
            if (this.level().getRandom().nextFloat()<0.8F){
                this.setPhase(Phase.COUNTER);
                this.setPhaseAttack(PhaseAttack.PREPARE_COUNTER);
                this.level().broadcastEntityEvent(this,(byte) 16);
            }
        }
        return super.hurtServer(level, source, damage);
    }



    private double calculateVerticalVelocity(double ticks) {
        double dy = this.targetPos.y - this.getY();
        double gravity = this.getGravity();

        return (dy + 0.5 * gravity * ticks * ticks) / ticks;
    }


    @Override
    public boolean shouldDiscardFriction() {
        return this.getPhase()==Phase.JUMP || super.shouldDiscardFriction();
    }

    public void setupAnimation(){
        this.counter.loop().animateWhen(getPhaseAttack() == PhaseAttack.COUNTER_LOOP,this.tickCount);
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 60;
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.counter.stopAll();
            this.jump.stopAll();
            this.idle.start(this.tickCount);
        }

    }


    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.idle.stop();
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.attack1.prepare().start(this.tickCount);
        }else if (id == 5){
            this.durationPhaseTimer = 0;
            this.idle.stop();
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.counter.attack().start(this.tickCount);
        }else if (id == 6){
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.idleResetTimer = 60;
            this.idle.start(this.tickCount);
        }else if (id == 8){
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.idle.stop();
            this.attack2.prepare().start(this.tickCount);
        }else if(id==12){
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.durationPhaseTimer = 0;
            this.attack2.attack().start(this.tickCount);
        }else if (id == 16){
            this.durationPhaseTimer = 0;
            this.idle.stop();
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.counter.prepare().start(this.tickCount);
        }else if (id == 32){
            this.idle.stop();
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.jump.prepare().start(this.tickCount);
        }else if (id == 64){
            this.idle.stop();
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.jump.attack().start(this.tickCount);
        }else if (id == 61){
            this.counter.stopAll();
            this.attack2.stopAll();
            this.attack1.stopAll();
            this.jump.stopAll();
            this.idleResetTimer = 60;
            this.idle.start(tickCount);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(PHASE,Phase.AROUND);
        entityData.define(PHASE_ATTACK,PhaseAttack.NONE);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("phase_attack",PhaseAttack.CODEC,getPhaseAttack());
        output.store("phase",Phase.CODEC,getPhase());
    }

    public PhaseAttack getPhaseAttack(){
        return entityData.get(PHASE_ATTACK);
    }
    public void setPhaseAttack(PhaseAttack phaseAttack){
        this.entityData.set(PHASE_ATTACK,phaseAttack);
    }
    public Phase getPhase(){
        return entityData.get(PHASE);
    }
    public void setPhase(Phase phase){
        this.entityData.set(PHASE,phase);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(PHASE_ATTACK,input.read("phase_attack",PhaseAttack.CODEC).orElse(PhaseAttack.NONE));
        entityData.set(PHASE,input.read("phase",Phase.CODEC).orElse(Phase.AROUND));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.5F, -2.0F);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return NRegistry.SCYTHE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return NRegistry.SCYTHE_IDLE.get();
    }

    public class JumpToTargetGoal extends Goal{
        private final Mob mob;

        public JumpToTargetGoal(Mob mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.mob.hasControllingPassenger()) {
                return false;
            }
            if (!this.mob.onGround()) {
                return false;
            }
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return false;
            }

            double distance = this.mob.distanceToSqr(target);

            if (distance < 128.0 || distance > 400.0) {
                return false;
            }

            if (this.mob.getRandom().nextInt(reducedTickDelay(5)) != 0) {
                return false;
            }

            VoidScytheEntity.this.targetPos = target.position();
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.mob.onGround();
        }

        @Override
        public void start() {
            if (VoidScytheEntity.this.targetPos == null) {
                return;
            }
            VoidScytheEntity.this.setPhase(Phase.JUMP);
            VoidScytheEntity.this.setPhaseAttack(PhaseAttack.PREPARE_JUMP);
            this.mob.level().broadcastEntityEvent(this.mob,(byte) 32);
        }

    }
    public class ChargedAttackGoal extends MeleeAttackGoal{

        public ChargedAttackGoal(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier, true);
        }

        @Override
        public void tick() {
            if (VoidScytheEntity.this.getPhaseAttack() == PhaseAttack.ATTACK_LOOP_2){
                super.tick();
            }else {
                this.mob.stopInPlace();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return VoidScytheEntity.this.getPhase() == Phase.ATTACK_2;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.mob.stopInPlace();
                this.mob.doHurtTarget(getServerLevel(level()),target);
                VoidScytheEntity.this.durationPhaseTimer = 0;
                VoidScytheEntity.this.setPhaseAttack(PhaseAttack.ATTACK_2);
                this.mob.level().broadcastEntityEvent(this.mob,(byte) 12);
            }
        }

        @Override
        public void start() {
            super.start();
            if (VoidScytheEntity.this.getPhaseAttack()==PhaseAttack.NONE){
                VoidScytheEntity.this.setPhaseAttack(PhaseAttack.PREPARE_2);
                this.mob.level().broadcastEntityEvent(this.mob,(byte) 8);
            }
        }

        @Override
        public boolean canUse() {
            return VoidScytheEntity.this.getPhase()==Phase.ATTACK_2;
        }
    }
    public enum Phase implements StringRepresentable{
        AROUND("around",0),
        JUMP("jump",1),
        ATTACK_1("attack_1",2),
        ATTACK_2("attack_2",3),
        COUNTER("counter",4);
        public static final Codec<Phase> CODEC = StringRepresentable.fromEnum(Phase::values);
        private static final IntFunction<Phase> BY_ID = ByIdMap.continuous(Phase::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Phase> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Phase::getId);

        public final String serialize;
        public final int id;
        Phase (String serialize,int id){
            this.serialize = serialize;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String getSerializedName() {
            return this.serialize;
        }
    }
    public enum PhaseAttack implements StringRepresentable{
        NONE("none",0),
        ATTACK_1("attack_1",1),
        ATTACK_2("attack_2",2),
        PREPARE_1("prepare_1",3),
        PREPARE_2("prepare_2",4),
        ATTACK_LOOP_1("attack_loop_1",5),
        ATTACK_LOOP_2("attack_loop_2",6),
        COUNTER("counter",7),
        COUNTER_LOOP("counter_loop",8),
        PREPARE_COUNTER("prepare_counter",9),
        LAND("land",10),
        JUMP_LOOP("jump_loop",11),
        PREPARE_JUMP("prepare_jump",12);
        public static final Codec<PhaseAttack> CODEC = StringRepresentable.fromEnum(PhaseAttack::values);
        private static final IntFunction<PhaseAttack> BY_ID = ByIdMap.continuous(PhaseAttack::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, PhaseAttack> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, PhaseAttack::getId);

        public final String serialize;
        public final int id;
        PhaseAttack (String serialize,int id){
            this.serialize = serialize;
            this.id = id;
        }

        public int getId() {
            return id;
        }
        @Override
        public String getSerializedName() {
            return serialize;
        }
    }



}
