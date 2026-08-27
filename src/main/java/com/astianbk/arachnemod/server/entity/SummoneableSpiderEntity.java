package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.server.goal.SummonFollowOwnerGoal;
import com.astianbk.arachnemod.server.goal.SummonOwnerHurtByTargetGoal;
import com.astianbk.arachnemod.server.goal.SummonOwnerHurtTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SummoneableSpiderEntity extends Monster implements OwnableEntity {
    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID =
            SynchedEntityData.defineId(SummoneableSpiderEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    public int summonTimer = 0;
    public SummoneableSpiderEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal(this, Armadillo.class, 6.0F, 1.0, 1.2, (entity) -> {
            return !((Armadillo)entity).isScared();
        }));
        this.goalSelector.addGoal(3,new SummonFollowOwnerGoal(this,1.3F,15,6));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new SummoneableSpiderEntity.SpiderAttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(1, new SummonOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new SummonOwnerHurtTargetGoal(this));

        this.targetSelector.addGoal(2, new SummoneableSpiderEntity.SpiderTargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(3, new SummoneableSpiderEntity.SpiderTargetGoal<>(this, IronGolem.class));
    }

    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_OWNERUUID_ID, Optional.empty());

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        EntityReference<LivingEntity> owner = this.getOwnerReference();
        EntityReference.store(owner, output, "Owner");
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        EntityReference<LivingEntity> owner = EntityReference.readWithOldOwnerConversion(input, "Owner", this.level());
        if (owner != null) {
            try {
                this.entityData.set(DATA_OWNERUUID_ID, Optional.of(owner));
            } catch (Throwable ignored) {
            }
        } else {
            this.entityData.set(DATA_OWNERUUID_ID, Optional.empty());
        }
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide()){
            this.summonTimer++;
            if (this.summonTimer > 400){
                this.discard();
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }


    public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, speedMultiplier);
        }

    }

    public boolean canBeAffected(MobEffectInstance newEffect) {
        return !newEffect.is(MobEffects.POISON) && super.canBeAffected(newEffect);
    }


    public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
        return vehicle.getBbWidth() <= this.getBbWidth() ? new Vec3(0.0, 0.3125 * (double)this.getScale(), 0.0) : super.getVehicleAttachmentPoint(vehicle);
    }

    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return (EntityReference)((Optional)this.entityData.get(DATA_OWNERUUID_ID)).orElse((Object)null);
    }
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return true;
    }

    public @Nullable PlayerTeam getTeam() {
        PlayerTeam ownTeam = super.getTeam();
        if (ownTeam != null) {
            return ownTeam;
        } else {
            LivingEntity owner = this.getRootOwner();
            if (owner != null) {
                return owner.getTeam();
            }
        }
        return null;
    }

    protected boolean considersEntityAsAlly(Entity other) {
        LivingEntity owner = this.getRootOwner();
        if (other == owner) {
            return true;
        }

        if (owner != null) {
            return owner.isAlliedTo((Team)other.getTeam());
        }
        return super.considersEntityAsAlly(other);
    }
    public void setOwner(@Nullable LivingEntity owner) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner).map(EntityReference::of));
    }

    public void setOwnerReference(@Nullable EntityReference<LivingEntity> owner) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner));
    }

    private static class SpiderAttackGoal extends MeleeAttackGoal {
        public SpiderAttackGoal(SummoneableSpiderEntity mob) {
            super(mob, 1.0, true);
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        public boolean canContinueToUse() {
            float br = this.mob.getLightLevelDependentMagicValue();
            if (br >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget((LivingEntity)null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }
    }

    private static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public SpiderTargetGoal(SummoneableSpiderEntity mob, Class<T> targetType) {
            super(mob, targetType, true);
        }

        public boolean canUse() {
            float br = this.mob.getLightLevelDependentMagicValue();
            return br >= 0.5F ? false : super.canUse();
        }
    }

}
