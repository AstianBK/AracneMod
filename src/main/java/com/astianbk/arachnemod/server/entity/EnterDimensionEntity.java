package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.Events;
import com.astianbk.arachnemod.server.network.PacketSetScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnterDimensionEntity extends Entity implements Portal {
    public AnimationState idle = new AnimationState();
    public AnimationState take = new AnimationState();
    public AnimationState spawn = new AnimationState();
    public int idleResetTimer = 0;
    public int portalTime = 0;
    public boolean active = false;
    public int takeTime = 0;
    public int spawnTime = 0;
    public EnterDimensionEntity(EntityType<?> type, Level level) {
        super(type, level);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }


    @Override
    public void tick() {
        super.tick();
        if (this.spawnTime>=0){
            this.spawnTime--;
            if (this.spawnTime == 0){

            }
        }

        if (this.level().isClientSide()){
            this.setupAnimation();
        }
        if (!this.level().isClientSide()){
            if (this.tickCount%10==0){
                this.level().broadcastEntityEvent(this,(byte) 7);
            }
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
                if (this.takeTime == 0){
                    list.forEach(living -> {
                        Events.teleportToVoid(this.position(),level(),living);
                    });
                    this.discard();
                }else if (this.takeTime == 1){
                    list.forEach(living -> {
                        if (living instanceof Player player){
                            PacketDistributor.sendToPlayer((ServerPlayer) player,new PacketSetScreen(player.getId()));
                        }
                    });
                }
            }else {
                if (active){
                    portalTime++;
                    if (portalTime == 10){
                        this.takeTime = 60;
                        this.level().broadcastEntityEvent(this,(byte) 4);
                        list.forEach(living -> {
                            living.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.DARKNESS,100,0)));
                        });
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
            this.spawn.stop();
            this.idle.start(this.tickCount);
        }
    }
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.take.start(this.tickCount);
            this.idle.stop();
            this.idleResetTimer = 60;
        }else if (id == 8){
            this.spawn.start(this.tickCount);
            this.spawnTime = 40;
            this.idle.stop();
            this.idleResetTimer = 40;
        }else if (id == 7){
            for (int i = 0 ; i < 5 ; i++){
                Particle particle = Minecraft.getInstance().particleEngine.createParticle(new DustParticleOptions(0,3.0F),getRandomX(0.75F),getY(),getRandomZ(0.75F),random.nextFloat(),0.3F,random.nextFloat());
            }
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

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return 60;
    }

    public Portal.Transition getLocalTransition() {
        return Transition.CONFUSION;
    }
    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel serverLevel, Entity entity, BlockPos blockPos) {
        return new TeleportTransition(serverLevel,Vec3.atBottomCenterOf(blockPos),Vec3.ZERO,0,0,TeleportTransition.PLACE_PORTAL_TICKET);
    }
}
