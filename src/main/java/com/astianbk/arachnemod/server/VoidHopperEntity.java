package com.astianbk.arachnemod.server;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VoidHopperEntity extends Mob {
    public AnimationState idle = new AnimationState();
    public AnimationState flee = new AnimationState();
    public AnimationState emerge = new AnimationState();
    public AnimationState casting = new AnimationState();
    public int idleResetTimer = 0;

    public Blessing prevBlessing = Blessing.NONE;
    public VoidHopperEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE,10.0F)
                .add(Attributes.FOLLOW_RANGE, 12.0)
                .add(Attributes.MAX_HEALTH, 24.0);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,false));
        this.goalSelector.addGoal(5,new LookAtPlayerGoal(this, Player.class,30.0F));
        this.goalSelector.addGoal(3,new SilentCastingGoal());
        this.goalSelector.addGoal(4,new DamnationCastingGoal());

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
            this.emerge.stop();
            this.flee.stop();
            this.casting.stop();
            this.idle.start(this.tickCount);
        }

    }
    public void addMarkSilent(LivingEntity living){
        int amplifier = 0;
        if (living.hasEffect(NRegistry.SILENT_HEX)){
            amplifier = living.getEffect(NRegistry.SILENT_HEX).getAmplifier()+1;
        }
        if (amplifier+1 == 3){
            living.addEffect(new MobEffectInstance(NRegistry.SILENT,500,0));
        }else {
            living.addEffect(new MobEffectInstance(NRegistry.SILENT_HEX,100,amplifier));
        }
    }

    public void addDamnationHex(LivingEntity living){
        int amplifier = 0;
        if (living.hasEffect(NRegistry.DAMNATION_HEX)){
            amplifier = living.getEffect(NRegistry.DAMNATION_HEX).getAmplifier()+1;
        }
        if (amplifier+1 == 3){
            living.addEffect(new MobEffectInstance(MobEffects.DARKNESS,250,0));
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,250,0));

        }else {
            living.addEffect(new MobEffectInstance(NRegistry.DAMNATION_HEX,100,amplifier));
        }
    }
    public void criSilent(){
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30.0F))){
            AracneMod.LOGGER.info("cri {}",living);
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addMarkSilent(living);
            }
        }
    }
    public void criDamnation(){
        for (Mob living : this.level().getEntitiesOfClass(Mob.class,this.getHitbox().inflate(30.0F))){
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addDamnationHex(living);
            }
        }
    }
    @Override
    public void push(Vec3 impulse) {
//        super.push(impulse);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            this.idleResetTimer = 100;
            this.idle.stop();
            this.casting.start(this.tickCount);
        }else if (id == 6){
            this.idleResetTimer = 50;
            this.idle.stop();
            this.flee.start(this.tickCount);
        }else if (id == 8){
            this.idleResetTimer = 50;
            this.idle.stop();
            this.emerge.start(this.tickCount);
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level){
            @Override
            public void tick() {

            }
        };
    }
    public abstract class BlessingGoal extends Goal{
        public int castingTime = 0;
        public int durationTime = 0;
        public int chargeTime = 0;
        public BlessingGoal(int durationTime){
            this.durationTime = durationTime;
            this.chargeTime = this.durationTime/3;
        }

        @Override
        public void start() {
            super.start();
            this.castingTime = 0;
            VoidHopperEntity.this.level().broadcastEntityEvent(VoidHopperEntity.this,(byte) 4);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.chargeBlessing()){
                if ((this.castingTime % chargeTime)==0){
                    blessing();
                }
            }

            if (this.castingTime == this.durationTime){
                finalBlessing();
            }
            this.castingTime++;
        }
        public boolean chargeBlessing(){
            return false;
        }
        private void finalBlessing(){
            VoidHopperEntity.this.prevBlessing = getType();
        }
        abstract void blessing();
        abstract Blessing getType();
        @Override
        public boolean canUse() {
            return VoidHopperEntity.this.getTarget() != null && VoidHopperEntity.this.prevBlessing != getType();
        }
    }
    public class SilentCastingGoal extends BlessingGoal{

        public SilentCastingGoal() {
            super(100);
        }


        @Override
        void blessing() {
            VoidHopperEntity.this.criSilent();
        }

        @Override
        public boolean chargeBlessing() {
            return true;
        }

        @Override
        Blessing getType() {
            return Blessing.SILENT;
        }
    }
    public class DamnationCastingGoal extends BlessingGoal{

        public DamnationCastingGoal() {
            super(100);
        }

        @Override
        void blessing() {
            VoidHopperEntity.this.criDamnation();
        }

        @Override
        Blessing getType() {
            return Blessing.DAMNATION;
        }
    }
    public enum Blessing{
        NONE,
        SILENT,
        DAMNATION,
        SUMMON_NEEDLE,
        CALL_ALLIES;
    }
}
