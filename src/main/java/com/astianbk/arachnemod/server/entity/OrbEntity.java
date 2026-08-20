package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.ArachneIdolBlockEntity;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class OrbEntity extends Entity {
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(OrbEntity.class, EntityDataSerializers.STRING);
    private Type type=Type.CANCEL;
    public BlockPos sourceBlock = null;
    public OrbEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TYPE,"CANCEL");
    }

    public void setType(Type type){
        this.type = type;
        entityData.set(TYPE,type.name());
    }

    public Type getOrbType(){
        return this.type;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (TYPE.equals(accessor)){
            this.type=Type.valueOf(entityData.get(TYPE));
        }
    }



    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        setType(Type.valueOf(valueInput.getStringOr("type","CANCEL")));
        if (valueInput.getInt("x").isPresent()){
            this.sourceBlock = new BlockPos(valueInput.getInt("x").get(),valueInput.getInt("y").get(),valueInput.getInt("z").get());
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putString("type",entityData.get(TYPE));
        if (this.sourceBlock!=null){
            valueOutput.putInt("x",this.sourceBlock.getX());
            valueOutput.putInt("y",this.sourceBlock.getY());
            valueOutput.putInt("z",this.sourceBlock.getZ());
        }
    }



    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()){
            if (this.sourceBlock==null){
                AracneMod.LOGGER.info("discard xd.");
                discard();
            }else {
                if (level().getBlockEntity(this.sourceBlock) instanceof ArachneIdolBlockEntity arachneIdolBlockEntity){
                    if (!arachneIdolBlockEntity.orbs.contains(this)){
                        arachneIdolBlockEntity.addOrb(this);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (sourceBlock!=null && level().getBlockEntity(sourceBlock) instanceof ArachneIdolBlockEntity arachneIdol){
            if (arachneIdol.orbs.contains(this)){

                arachneIdol.selectOrb(player, getOrbType(),level(),sourceBlock);
            }
        }

        return super.interact(player, hand, location);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
    }

    public enum Type {
        BLESSING,
        CANCEL,
        QUEST,
        QUEST_KILL,
        QUEST_REPUTATION,
        QUEST_GET;

    }
}
