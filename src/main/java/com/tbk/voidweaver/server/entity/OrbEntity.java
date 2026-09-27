package com.tbk.voidweaver.server.entity;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.ArachneIdolBlockEntity;
import com.tbk.voidweaver.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.phys.Vec3;

public class OrbEntity extends Entity {
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(OrbEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> LOCK = SynchedEntityData.defineId(OrbEntity.class, EntityDataSerializers.BOOLEAN);

    private Type type=Type.CANCEL;
    public BlockPos sourceBlock = null;
    public OrbEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TYPE,"CANCEL");
        builder.define(LOCK,false);
    }

    public void setType(Type type){
        this.type = type;
        entityData.set(TYPE,type.name());
        this.setCustomName(Component.translatable("entity.voidweaver.orb_entity."+type.name().toLowerCase()+((this.isLock()) ? ".off" : ".on")));
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


    public void setLock(boolean lock){
        this.entityData.set(LOCK,lock);
    }
    public boolean isLock(){
        return this.entityData.get(LOCK);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setType(Type.valueOf(compoundTag.getString("type")));
        if (compoundTag.contains("x")){
            this.sourceBlock = new BlockPos(compoundTag.getInt("x"),compoundTag.getInt("y"),compoundTag.getInt("z"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putString("type",entityData.get(TYPE));
        if (this.sourceBlock!=null){
            compoundTag.putInt("x",this.sourceBlock.getX());
            compoundTag.putInt("y",this.sourceBlock.getY());
            compoundTag.putInt("z",this.sourceBlock.getZ());
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
                discard();
            }else {
                if (level().getBlockEntity(this.sourceBlock) instanceof ArachneIdolBlockEntity arachneIdolBlockEntity){
                    if (!arachneIdolBlockEntity.orbs.contains(this)){
                        arachneIdolBlockEntity.addOrb(this);
                    }
                }else {
                    discard();
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (sourceBlock != null && level().getBlockEntity(sourceBlock) instanceof ArachneIdolBlockEntity idol && idol.orbs.contains(this)) {
            idol.selectOrb(this, player, getOrbType(), level(), sourceBlock);

            return InteractionResult.sidedSuccess(level().isClientSide).SUCCESS;
        }

        return InteractionResult.sidedSuccess(level().isClientSide).SUCCESS;
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
        QUEST_GET,
        ARACHNE_MOVE,
        ARACHNE_ANTI_FALL,
        ARACHNE_FANG,
        ARACHNE_ALLIE,
        ARACHNE_INFECTION,
        ARACHNE_PROTECTION,
        ARACHNE_FORM;
    }
}
