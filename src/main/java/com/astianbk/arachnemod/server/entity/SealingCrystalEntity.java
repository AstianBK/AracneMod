package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

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
        this.applyEffectsFromBlocks();
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

    protected void addAdditionalSaveData(ValueOutput output) {
        output.storeNullable("beam_target", BlockPos.CODEC, this.getBeamTarget());
        output.putBoolean("ShowBottom", this.showsBottom());
    }

    protected void readAdditionalSaveData(ValueInput input) {
        this.setBeamTarget((BlockPos)input.read("beam_target", BlockPos.CODEC).orElse((BlockPos) null));
        this.setShowBottom(input.getBooleanOr("ShowBottom", true));
    }

    public boolean isPickable() {
        return true;
    }

    public final boolean hurtClient(DamageSource source) {
        return !this.isInvulnerableToBase(source) && !(source.getEntity() instanceof EnderDragon);
    }

    public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        } else if (source.getEntity() instanceof EnderDragon) {
            return false;
        } else {
            if (!this.isRemoved()) {
                this.remove(RemovalReason.KILLED);
            }
            return true;
        }
    }

    public void kill(ServerLevel level) {
        super.kill(level);
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