package com.astianbk.arachnemod.server;

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
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        setType(Type.valueOf(valueInput.getStringOr("type","CANCEL")));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putString("type",entityData.get(TYPE));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {

        return super.interact(player, hand, location);
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
