package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.goal.FleeBlockLightGoal;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.IntFunction;

public class ScarabEntity extends PathfinderMob {
    public static final EntityDataAccessor<Attack> ATTACK = SynchedEntityData.defineId(ScarabEntity.class,NRegistry.ATTACK_SERIALIZER.get());
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
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.ARMOR_TOUGHNESS, 15.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.5F);

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
                if (this.canPerformAttack(target) && ScarabEntity.this.attackTimer<=0) {
                    this.mob.getNavigation().stop();
                    ScarabEntity.this.attackTimer=10;
                    if (ScarabEntity.this.entityData.get(ATTACK)==Attack.NONE){
                        switch (this.mob.getRandom().nextInt(0,3)){
                            case 0->{
                                ScarabEntity.this.entityData.set(ATTACK,Attack.ATTACK_1);
                                this.mob.level().broadcastEntityEvent(this.mob,(byte) 4);
                            }
                            case 1->{
                                ScarabEntity.this.entityData.set(ATTACK,Attack.ATTACK_2);
                                this.mob.level().broadcastEntityEvent(this.mob,(byte) 8);
                            }
                            case 2->{
                                ScarabEntity.this.entityData.set(ATTACK,Attack.BITE);
                                this.mob.level().broadcastEntityEvent(this.mob,(byte) 16);
                            }
                        }
                    }else {
                        Attack attack = ScarabEntity.this.entityData.get(ATTACK);
                        ScarabEntity.this.entityData.set(ATTACK, Attack.BY_ID.apply((attack.getId()+1)%3));
                        switch (ScarabEntity.this.entityData.get(ATTACK).id){
                            case 0->{
                                this.mob.level().broadcastEntityEvent(this.mob,(byte) 4);
                            }
                            case 1->{
                                this.mob.level().broadcastEntityEvent(this.mob,(byte) 8);
                            }
                            case 2->{
                                this.mob.level().broadcastEntityEvent(this.mob,(byte) 16);
                            }
                        }
                    }

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
                return super.canUse() && level().getBrightness(LightLayer.BLOCK,this.mob.getTarget().blockPosition())<=0;
            }
        });
        this.goalSelector.addGoal(1,new FleeBlockLightGoal(this,2.0F));
    }


    @Override
    public void tick() {
        super.tick();



        LivingEntity target = getTarget();

        if (target != null) {
            if (!this.level().isClientSide()){
                if (this.entityData.get(ATTACK) != Attack.NONE){
                    this.attackTimer--;
                    switch (this.entityData.get(ATTACK)){
                        case ATTACK_1 ,ATTACK_2-> {
                            if (this.attackTimer == 5){
                                if (target.distanceTo(this)<4){
                                    this.doHurtTarget((ServerLevel) level(),target);
                                }
                            }
                        }
                        case BITE -> {
                            if (this.attackTimer == 5){
                                if (target.distanceTo(this)<4){
                                    this.doHurtTarget((ServerLevel) level(),target);
                                    target.addEffect(new MobEffectInstance(MobEffects.POISON,200,1));
                                }
                            }
                        }
                    }
                    if (this.attackTimer==0){
                        this.entityData.set(ATTACK,Attack.NONE);
                    }
                }
                double dx = target.getX() - getX();
                double dz = target.getZ() - getZ();

                float yaw = (float)(Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;

                setYRot(yaw);
                setYBodyRot(yaw);
                setYHeadRot(yaw);
            }

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
            attackTimer = 10;
            this.attack1.start(this.tickCount);
        }else if (id == 8){
            this.idle.stop();
            this.idleResetTimer = 10;
            attackTimer = 10;
            this.attack2.start(this.tickCount);
        }else if (id == 16){
            this.idle.stop();
            this.idleResetTimer = 10;
            attackTimer = 10;
            this.bite.start(this.tickCount);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(ATTACK,Attack.ATTACK_1);
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
        return this.random.nextBoolean() ? NRegistry.SCARAB_IDLE1.get() : NRegistry.SCARAB_IDLE2.get();
    }


    public enum Attack implements StringRepresentable {
        ATTACK_1("attack_1",0),
        ATTACK_2("attack_2",1),
        BITE("bite",2),
        NONE("none",3);
        public static final Codec<Attack> CODEC = StringRepresentable.fromEnum(Attack::values);
        private static final IntFunction<Attack> BY_ID = ByIdMap.continuous(Attack::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Attack> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Attack::getId);

        public final String serialize;
        public final int id;
        Attack (String serialize,int id){
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
}
