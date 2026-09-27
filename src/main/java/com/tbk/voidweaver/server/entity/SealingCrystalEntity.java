package com.tbk.voidweaver.server.entity;

import com.tbk.voidweaver.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class SealingCrystalEntity extends Entity {
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET =  SynchedEntityData.defineId(SealingCrystalEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM =  SynchedEntityData.defineId(SealingCrystalEntity.class, EntityDataSerializers.BOOLEAN);;
    private static final boolean DEFAULT_SHOW_BOTTOM = true;
    public int time;
    public AnimationState idle = new AnimationState();
    public int idleTime = 0;

    public SealingCrystalEntity(EntityType<? extends SealingCrystalEntity> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
        this.time = this.random.nextInt(100000);
    }




    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_BEAM_TARGET, Optional.empty());
        entityData.define(DATA_SHOW_BOTTOM, true);
    }

    public void tick() {
        ++this.time;

        this.handlePortal();
        if (this.level().isClientSide()){
            this.setupClient();
        }
    }

    private void setupClient() {
        if (this.idleTime--<=0){
            this.idleTime = 80;
            this.idle.start(this.tickCount);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }



    public boolean isPickable() {
        return true;
    }
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source.getEntity() instanceof EnderDragon) {
            return false;
        } else {
            if (!this.isRemoved() && !this.level().isClientSide) {
                this.remove(RemovalReason.KILLED);
                if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
                    DamageSource damagesource = source.getEntity() != null ? this.damageSources().explosion(this, source.getEntity()) : null;
                    this.level().explode(this, damagesource, (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), 6.0F, false, Level.ExplosionInteraction.BLOCK);
                }
                this.spawnAtLocation(new ItemStack(NRegistry.POWER_FRAGMENT.get()));
            }

            return true;
        }
    }


    public void setBeamTarget(@Nullable BlockPos target) {
        this.getEntityData().set(DATA_BEAM_TARGET, Optional.ofNullable(target));
    }

    public @Nullable BlockPos getBeamTarget() {
        return (BlockPos)((Optional)this.getEntityData().get(DATA_BEAM_TARGET)).orElse((Object)null);
    }

    public void setShowBottom(boolean showBottom) {
        this.getEntityData().set(DATA_SHOW_BOTTOM, showBottom);
    }

    public boolean showsBottom() {
        return (Boolean)this.getEntityData().get(DATA_SHOW_BOTTOM);
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        return super.shouldRenderAtSqrDistance(distance) || this.getBeamTarget() != null;
    }

}