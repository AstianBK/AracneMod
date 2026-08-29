package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.Events;
import com.astianbk.arachnemod.server.network.PacketSetScreen;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ArachneLegEntity extends Entity {
    public AnimationState spawn = new AnimationState();
    public int portalTime = 0;
    public boolean active = false;
    public int delay = 0;
    public boolean visible = false;
    public int spawnTime = 0;
    public LivingEntity owner = null;
    public ArachneLegEntity(EntityType<?> type, Level level) {
        super(type, level);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        float yRot = packet.getYRot();
        float xRot = packet.getXRot();
        this.syncPacketPositionCodec(x, y, z);
        this.setId(packet.getId());
        this.setUUID(packet.getUUID());
        this.absSnapTo(x, y, z, yRot, xRot);
        this.setDeltaMovement(packet.getMovement());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.delay>0){
            this.delay--;
            if (!this.level().isClientSide()){
                if (this.delay==0){
                    this.level().broadcastEntityEvent(this,(byte) 8);
                }
            }

            return;
        }
        if (this.spawnTime>=0){
            this.spawnTime--;
            if (this.spawnTime == 4){
                if (!this.level().isClientSide()){
                    if (this.owner != null){
                        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(0.5D,2D,0.5D),e->!this.owner.is(e) && !e.isAlliedTo(this.owner))){
                            living.invulnerableTime=0;
                            living.hurtServer(((ServerLevel)level()),damageSources().sonicBoom(owner),5.0F);
                        }
                    }else {
                        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(0.5D,2D,0.5D))){
                            living.invulnerableTime=0;
                            living.hurtServer(((ServerLevel)level()),damageSources().magic(),5.0F);
                        }
                    }

                }
            }
            if (this.spawnTime == 0){
                this.discard();
            }
        }


    }

    @Override
    public boolean isPickable() {
        return true;
    }
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.visible = false;
        }else if (id == 8){
            this.spawn.start(this.tickCount);
            this.spawnTime = 10;
            this.visible = true;
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void push(Vec3 impulse) {

    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }


    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {

    }
}
