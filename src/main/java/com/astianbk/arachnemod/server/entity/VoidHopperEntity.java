package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
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
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class VoidHopperEntity extends PathfinderMob {
    private static final EntityDataAccessor<String> BLESSING = SynchedEntityData.defineId(VoidHopperEntity.class, EntityDataSerializers.STRING);


    public AnimationState idle = new AnimationState();
    public AnimationState flee = new AnimationState();
    public AnimationState emerge = new AnimationState();
    public AnimationState casting = new AnimationState();
    public int idleResetTimer = 0;
    public int emergeAnimationTimer = 0;
    public int diggingAnimationTimer = 0;
    public int sleepCastingTime = 0;
    public Blessing prevBlessing = Blessing.NONE;
    public Vec3 fleePos = null;

    public VoidHopperEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE,10.0F)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MAX_HEALTH, 20.0);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,false));
        this.goalSelector.addGoal(5,new LookAtPlayerGoal(this, Player.class,30.0F));
        this.goalSelector.addGoal(3,new SilentCastingGoal());
        this.goalSelector.addGoal(4,new DamnationCastingGoal());
        this.goalSelector.addGoal(4,new ArachnophobiaCastingGoal());

        this.goalSelector.addGoal(1,new FleeGoal());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()){
            this.setupAnimation();
        }
        if (this.diggingAnimationTimer > 0){
            this.diggingAnimationTimer--;
            if (this.diggingAnimationTimer ==0){
                if (this.fleePos != null){
                    this.setPose(Pose.EMERGING);
                    this.teleportTo(fleePos.x,fleePos.y,fleePos.z);
                    this.fleePos=null;
                }
            }
        }
        if (this.emergeAnimationTimer>0){
            this.emergeAnimationTimer--;
            if (this.emergeAnimationTimer==0){
                this.setPose(Pose.STANDING);
                this.emerge.stop();
            }
        }

        if (!level().isClientSide()){
            if (this.getTarget()!=null){
                if (this.getBlessing()==Blessing.NONE){
                    if (this.sleepCastingTime > 0){
                        this.sleepCastingTime--;
                    }
                    if (this.sleepCastingTime==0){
                        this.setBlessing(getRandomBlessing(random).name());
                        this.sleepCastingTime = 200;
                    }
                }
            }else {
                this.idleResetTimer=0;
                this.setBlessing("NONE");
            }
        }
    }
    public Blessing getRandomBlessing(RandomSource random){
        int randomI = random.nextInt(0,3);
        switch (randomI){
            case 0->{
                return Blessing.SILENT;
            }
            case 1->{
                return Blessing.DAMNATION;
            }
            case 2->{
                return Blessing.ARACHNOPHOBIA;
            }
        }
        return Blessing.NONE;
    }



    public void setupAnimation(){
        if (getBlessing()!= Blessing.NONE){
            this.casting.animateWhen(true,tickCount);
            this.idleResetTimer--;
            return;
        }
        if (this.idleResetTimer--<=0){
            this.idleResetTimer = 30;
            this.emerge.stop();
            this.flee.stop();
            this.casting.stop();
            this.idle.start(this.tickCount);
        }

    }

    public Blessing getBlessing(){
        return Blessing.valueOf(this.entityData.get(BLESSING));
    }
    public void setBlessing(String blessing){
        this.entityData.set(BLESSING,blessing);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(BLESSING,"NONE");
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {

    }
    public void addEffectToEnemy(LivingEntity living, Holder<MobEffect> effectHolder){
        if (living instanceof Player player){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                int size = arachneAttachment.hexes.size();
                if (size==3){
                    arachneAttachment.clearHexes(player);
                    living.addEffect(new MobEffectInstance(effectHolder,500,0));
                }else  {
                    level().playSound(null,living,NRegistry.HOPPER_HEX.get(), SoundSource.HOSTILE,3.0F,-1.0F);
                    arachneAttachment.addHex(level(), player);
                }
            });
        }
    }
    public void criArachnophobia(){
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30.0F))){
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addEffectToEnemy(living,NRegistry.ARACHNOPHOBIA);
            }
        }
    }
    public void criSilent(){
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30.0F))){
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addEffectToEnemy(living,NRegistry.SILENT);
            }
        }
    }
    public void criDamnation(){
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30.0F))){
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addEffectToEnemy(living,NRegistry.DAMNATION);
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
            this.flee.stop();
            this.emerge.stop();
            this.casting.start(this.tickCount);

        }
        super.handleEntityEvent(id);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_POSE.equals(accessor)) {
            switch (this.getPose()) {
                case EMERGING:
                    this.idle.stop();
                    this.flee.stop();
                    this.casting.stop();
                    this.idleResetTimer = 50;
                    this.emergeAnimationTimer = 50;
                    this.emerge.start(this.tickCount);
                    break;
                case DIGGING:
                    this.idle.stop();
                    this.emerge.stop();
                    this.casting.stop();
                    this.idleResetTimer = 30;
                    this.diggingAnimationTimer = 30;
                    this.flee.start(this.tickCount);
                    break;
            }
        }


        super.onSyncedDataUpdated(accessor);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND;
    }
    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level){
            @Override
            public void tick() {

            }
        };
    }
    public class FleeGoal extends Goal{

        public FleeGoal(){

        }

        @Override
        public void tick() {
            super.tick();
            flee();
            if (VoidHopperEntity.this.fleePos != null){
                VoidHopperEntity.this.setPose(Pose.DIGGING);
            }
        }
        public void flee(){
            double dist = 15.0D;
            while (dist > 3){
                double r = VoidHopperEntity.this.level().getRandom().nextInt(0,24) * 15.0D;
                for (int radius = 0; radius <360 ; radius+=15){
                    radius = (int) (radius + r);
                    double x = Math.sin(radius) * dist + VoidHopperEntity.this.getX();
                    double z = Math.cos(radius) * dist  + VoidHopperEntity.this.getZ();
                    double y = VoidHopperEntity.this.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z);
                    if (validPos(x,y,z)){
                        VoidHopperEntity.this.fleePos = new Vec3(x,y,z);
                        return;
                    }
                }
                dist-=3;
            }

        }
        public boolean validPos(double x , double y, double z){
            BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
            return !level().getBlockState(pos.below()).isAir() && level().getBrightness(LightLayer.BLOCK,pos)==0;
        }

        @Override
        public boolean canUse() {
            return VoidHopperEntity.this.fleePos==null && ((VoidHopperEntity.this.getBlessing()==Blessing.NONE &&  (VoidHopperEntity.this.getTarget() != null && VoidHopperEntity.this.distanceToSqr(VoidHopperEntity.this.getTarget()) < 15.0F)) || level().getBrightness(LightLayer.BLOCK,VoidHopperEntity.this.blockPosition())>0);
        }
    }
    public abstract class BlessingGoal extends Goal{
        public int castingTime = 0;
        public int durationTime;
        public int chargeTime;
        public BlessingGoal(int durationTime){
            this.durationTime = durationTime;
            this.chargeTime = this.durationTime/4;
        }

        @Override
        public void start() {
            super.start();
            this.castingTime = 0;
            VoidHopperEntity.this.level().broadcastEntityEvent(VoidHopperEntity.this,(byte) 4);
            level().playSound(null,VoidHopperEntity.this,NRegistry.HOPPER_CHANNEL.get(), SoundSource.HOSTILE,1.0F,1.0F);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.castingTime == this.durationTime){
                finalBlessing();
            }
            if (!VoidHopperEntity.this.level().isClientSide()){
                if (this.chargeBlessing()){
                    if ((this.castingTime % chargeTime)==0){
                        blessing();
                    }
                }
            }



            this.castingTime++;
        }
        public boolean chargeBlessing(){
            return false;
        }
        private void finalBlessing(){
            VoidHopperEntity.this.setBlessing("NONE");
        }
        abstract void blessing();
        abstract Blessing getType();

        @Override
        public boolean canUse() {
            return !VoidHopperEntity.this.hasPose(Pose.DIGGING) && !VoidHopperEntity.this.hasPose(Pose.EMERGING) && VoidHopperEntity.this.getTarget() != null && VoidHopperEntity.this.getBlessing() == getType();
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
        public boolean chargeBlessing() {
            return true;
        }


        @Override
        Blessing getType() {
            return Blessing.DAMNATION;
        }
    }
    public class ArachnophobiaCastingGoal extends BlessingGoal{


        public ArachnophobiaCastingGoal() {
            super(100);
        }

        @Override
        void blessing() {
            VoidHopperEntity.this.criArachnophobia();
        }
        @Override
        public boolean chargeBlessing() {
            return true;
        }


        @Override
        Blessing getType() {
            return Blessing.ARACHNOPHOBIA;
        }
    }
    public enum Blessing{
        NONE,
        SILENT,
        DAMNATION,
        ARACHNOPHOBIA,
        SUMMON_NEEDLE,
        CALL_ALLIES;
    }



}
