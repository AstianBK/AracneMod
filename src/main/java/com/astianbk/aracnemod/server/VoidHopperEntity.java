package com.astianbk.aracnemod.server;

import com.astianbk.aracnemod.common.registry.NRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class VoidHopperEntity extends Mob {
    public AnimationState idle = new AnimationState();
    public int idleResetTimer = 0;
    public VoidHopperEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3,new LookAtPlayerGoal(this, Player.class,30.0F));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()){
            this.setupAnimation();
        }
    }

    public void setupAnimation(){
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 30;

            this.idle.start(this.tickCount);
        }
    }
    public void addMarkSilent(LivingEntity living){
        int amplifier = 0;
        if (living.hasEffect(NRegistry.MARK_SILENT)){
            amplifier = living.getEffect(NRegistry.MARK_SILENT).getAmplifier()+1;
        }
        if (amplifier+1 == 3){
            living.addEffect(new MobEffectInstance(NRegistry.SILENT,500,0));
        }else {
            living.addEffect(new MobEffectInstance(NRegistry.MARK_SILENT,100,amplifier));
        }
    }
    public void cricri(){
        for (Mob living : this.level().getEntitiesOfClass(Mob.class,this.getHitbox().inflate(30.0F))){
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addMarkSilent(living);
            }
        }
    }

    @Override
    public void push(Vec3 impulse) {
//        super.push(impulse);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level){
            @Override
            public void tick() {

            }
        };
    }
}
