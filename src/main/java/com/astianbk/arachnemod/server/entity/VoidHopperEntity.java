package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class VoidHopperEntity extends Monster {

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

    public VoidHopperEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE,10.0F)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MAX_HEALTH, 15.0);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,false));
        this.goalSelector.addGoal(5,new LookAtPlayerGoal(this, Player.class,30.0F));
        this.goalSelector.addGoal(3,new SilentCastingGoal());
        this.goalSelector.addGoal(4,new DamnationCastingGoal());
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
                }
            }
        }
        if (this.emergeAnimationTimer>0){
            this.emergeAnimationTimer--;
            if (this.emergeAnimationTimer==0){
                this.setPose(Pose.STANDING);

            }
        }
        if (this.prevBlessing==Blessing.NONE){
            if (this.sleepCastingTime > 0){
                this.sleepCastingTime--;
            }
            if (this.sleepCastingTime==0){
                this.prevBlessing = random.nextFloat() > 0.5F ? Blessing.SILENT : Blessing.DAMNATION;
                this.sleepCastingTime = 200;
            }
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
        if (living instanceof Player player){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                int size = arachneAttachment.hexes.size();
                if (size==3){
                    arachneAttachment.clearHexes(player);
                    living.addEffect(new MobEffectInstance(NRegistry.SILENT,500,0));
                }else  {
                    arachneAttachment.addHex(level(), player);
                }
            });
        }
    }

    public void addDamnationHex(LivingEntity living){
        if (living instanceof Player player){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                int size = arachneAttachment.hexes.size();
                if (size==3){
                    arachneAttachment.clearHexes(player);
                    living.addEffect(new MobEffectInstance(MobEffects.DARKNESS,250,0));
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,250,0));
                }else  {
                    arachneAttachment.addHex(level(), player);
                }
            });
        }
    }
    public void criSilent(){
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30.0F))){
            if (!living.is(EntityTypeTags.ARTHROPOD)){
                addMarkSilent(living);
            }
        }
    }
    public void criDamnation(){
        for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class,this.getHitbox().inflate(30.0F))){
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
                    this.idleResetTimer = 60;
                    this.diggingAnimationTimer = 60;
                    this.flee.start(this.tickCount);
                    break;
            }
        }

        super.onSyncedDataUpdated(accessor);
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
            return !level().getBlockState(pos.below()).isAir();
        }

        @Override
        public boolean canUse() {
            return VoidHopperEntity.this.getTarget() != null && VoidHopperEntity.this.distanceToSqr(VoidHopperEntity.this.getTarget()) < 15.0F;
        }
    }
    public abstract class BlessingGoal extends Goal{
        public int castingTime = 0;
        public int durationTime = 0;
        public int chargeTime = 0;
        public BlessingGoal(int durationTime){
            this.durationTime = durationTime;
            this.chargeTime = this.durationTime/4;
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
            if (!VoidHopperEntity.this.level().isClientSide()){
                if (this.chargeBlessing()){
                    if ((this.castingTime % chargeTime)==0){
                        AracneMod.LOGGER.info("castingTime : {}",this.castingTime);
                        blessing();
                    }
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
            VoidHopperEntity.this.prevBlessing =Blessing.NONE;
        }
        abstract void blessing();
        abstract Blessing getType();

        @Override
        public boolean canUse() {
            return !VoidHopperEntity.this.hasPose(Pose.DIGGING) && !VoidHopperEntity.this.hasPose(Pose.EMERGING) && VoidHopperEntity.this.getTarget() != null && VoidHopperEntity.this.prevBlessing == getType();
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
    public enum Blessing{
        NONE,
        SILENT,
        DAMNATION,
        SUMMON_NEEDLE,
        CALL_ALLIES;
    }
}
