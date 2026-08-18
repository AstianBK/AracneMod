package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.Events;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EnterDimensionEntity extends Entity {
    public AnimationState idle = new AnimationState();
    public AnimationState take = new AnimationState();

    public int idleResetTimer = 0;
    public int portalTime = 0;
    public boolean active = false;
    public int takeTime = 0;
    public EnterDimensionEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }


    @Override
    public void tick() {
        super.tick();


        if (this.level().isClientSide()){
            this.setupAnimation();
        }
        if (!this.level().isClientSide()){
            List<LivingEntity> list = new ArrayList<>();
            boolean entityAbove = false;
            for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(0D,2D,0D))){
                entityAbove = true;
                list.add(living);
            }
            if (active!=entityAbove){
                portalTime = 0;
            }
            active = entityAbove;

            if (this.takeTime>0){
                this.takeTime--;
                if (this.takeTime==0){
                    list.forEach(living -> Events.teleportToVoid(this.position(),level(),living));
                }
            }else {
                if (active){
                    portalTime++;
                    if (portalTime == 40){
                        this.takeTime = 60;
                        this.level().broadcastEntityEvent(this,(byte) 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public void setupAnimation(){
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 50;
            this.take.stop();
            this.idle.start(this.tickCount);
        }
    }
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.take.start(this.tickCount);
            this.idle.stop();
            this.idleResetTimer = 60;
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
