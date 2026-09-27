package com.tbk.voidweaver.server.cap;


import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.Events;
import com.tbk.voidweaver.QuestsType;
import com.tbk.voidweaver.common.compendium.*;
import com.tbk.voidweaver.common.dialogs.Dialog;
import com.tbk.voidweaver.common.dialogs.DialogsManager;
import com.tbk.voidweaver.common.quests.Quest;
import com.tbk.voidweaver.common.quests.QuestManager;
import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.server.cap.data.BlessingData;
import com.tbk.voidweaver.server.cap.data.CompendiumData;
import com.tbk.voidweaver.server.cap.data.CooldownData;
import com.tbk.voidweaver.server.network.PacketHandlerParticle;
import com.tbk.voidweaver.server.network.PacketPlayDialog;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;

import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.*;


/*
 * ADAPTADO A NEOFORGE 1.21.1
 *
 * Cambios respecto a la versión original (escrita para 1.21.5+):
 *
 * 1) Serialización de attachments:
 *      - ValueInput / ValueOutput NO existen en 1.21.1 (llegaron en 1.21.5).
 *      - IAttachmentSerializer<D, T> ahora se implementa como
 *        IAttachmentSerializer<CompoundTag, ArachneAttachment>.
 *      - read(IAttachmentHolder, CompoundTag, HolderLookup.Provider)
 *      - write(ArachneAttachment, HolderLookup.Provider) -> CompoundTag
 *      - Las listas basadas en Codec (Hex, CompendiumData, BlessingData) se
 *        codifican/decodifican manualmente contra NbtOps.INSTANCE.
 *
 * 2) Teletransporte entre dimensiones:
 *      - TeleportTransition es de 1.21.2+. En 1.21.1 el teletransporte de
 *        dimensión es síncrono: se usa
 *        ServerPlayer#teleportTo(ServerLevel, x, y, z, Set<RelativeMovement>, yaw, pitch)
 *        y el código que iba en el callback se ejecuta justo después, en línea.
 *
 * 3) Daño a entidades:
 *      - El split hurtServer()/hurtClient() es de 1.21.2+. En 1.21.1 se usa
 *        LivingEntity#hurt(DamageSource, float).
 *
 * 4) Movimiento conocido del jugador:
 *      - Entity#getKnownSpeed() es parte de la reescritura de movimiento de
 *        1.21.2+. En 1.21.1 se sustituye por Entity#getDeltaMovement().
 *
 * IMPORTS a ajustar en tu archivo real (quita los de ValueInput/ValueOutput,
 * TeleportTransition, y agrega):
 *   import net.minecraft.nbt.CompoundTag;
 *   import net.minecraft.nbt.NbtOps;
 *   import net.minecraft.core.HolderLookup;
 *   import net.minecraft.world.entity.RelativeMovement;
 *   import java.util.Set;
 *   import net.minecraft.server.MinecraftServer;
 */

public class ArachneAttachment {
    public AnimationState idle = new AnimationState();
    public AnimationState crouching = new AnimationState();
    public AnimationState attack = new AnimationState();
    public AnimationState use = new AnimationState();
    public AnimationState block = new AnimationState();
    public AnimationState burrow = new AnimationState();
    public AnimationState swim = new AnimationState();
    public boolean isCocoon = false;
    public boolean transformComplete = false;
    public boolean itemTransformDrop = false;
    public Quest currentQuest=null;
    public int speechTime=0;
    public int speechTimeO=0;
    public int timeQuest=0;
    public int progressQuest=0;
    public int idleTimer = 0;
    public int timeHex = 0;
    public int currentReputation = 0;
    public int previousTimesChanged = 0;
    public boolean isDirty = false;
    public int timeDarkness = 0;
    public int prevTimeDarkness = 0;
    public boolean prevIsDark = false;
    public BlockPos teleportBack = null;
    public List<Hex> hexes = new ArrayList<>();
    public List<CompendiumData> compendiumData=new ArrayList<>();
    public List<BlessingData> blessingData = new ArrayList<>();
    ServerBossEvent event =  Util.make(new ServerBossEvent(Component.literal("this.getDisplayName()"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS), e -> e.setDarkenScreen(false));
    public String currentDialog = null;
    public int index = 0;
    public List<String> bufferText = new ArrayList<>();
    public String text = "";
    public boolean completeText = false;
    public int time = 0;
    public int cocoonTime = 0;
    public int scissorAttackTime = 0;
    public boolean scissorAttack = false;
    public boolean runningWithHelmet = false;
    public Map<Entity, Long> recentRunningHelmetEnemies = new HashMap<>();
    public final float CONE_ANGLE_SCISSORS = 60.0F;
    public final double RANGE_SCISSORS = 5.5D;
    public final Map<QuestsType,String[]> DIALOGS_FOR_TYPE = Map.of(QuestsType.HUNT,new String[]{"voidweaver:arachne_quest_kill_complete1","voidweaver:arachne_quest_kill_complete2","voidweaver:arachne_quest_kill_complete3","voidweaver:arachne_quest_kill_complete4","voidweaver:arachne_quest_kill_complete5","voidweaver:arachne_quest_kill_complete6"},
            QuestsType.COLLECT,new String[]{"voidweaver:arachne_quest_collect_complete1","voidweaver:arachne_quest_collect_complete2","voidweaver:arachne_quest_collect_complete3","voidweaver:arachne_quest_collect_complete4","voidweaver:arachne_quest_collect_complete5","voidweaver:arachne_quest_collect_complete6"});
    public String getTimeInMinuteAndSeconds(){
        int seconds = this.timeQuest/20;
        int minutes = seconds/60;
        seconds = seconds % 60;
        String sSeconds = seconds>9 ? String.valueOf(seconds) : "0"+seconds;
        String sMinutes = minutes>9 ? String.valueOf(minutes) : "0"+minutes;
        return sMinutes+":"+sSeconds;
    }

    public boolean tick(Player player){
        boolean flag = true;
        if(player instanceof ServerPlayer serverPlayer){
            boolean questActive = this.currentQuest!=null;
            event.setVisible(questActive);
            if(questActive){
                if(!event.getPlayers().contains(serverPlayer)){
                    event.addPlayer(serverPlayer);
                }

                Component component = Component.literal(this.currentQuest.getTitle() + " - "+ getTimeInMinuteAndSeconds());

                event.setName(component);
                BossEvent.BossBarColor color = getColorForQuestType();

                event.setColor(color);
                event.setProgress((float) this.progressQuest/this.currentQuest.getMaxProgress());

                if(!this.currentQuest.isComplete(this) && this.timeQuest--<=0){
                    failQuest(serverPlayer);
                    this.progressQuest = 0;
                    this.timeQuest = 0;
                }
            }else {
                event.removeAllPlayers();
            }

            if(this.isDirty){
                this.isDirty = false;
            }

            if (this.timeHex > 0){
                this.timeHex--;
                if (this.timeHex == 0){
                    clearHexes(player);
                }
            }
        }

        if (player.level().dimension() == NRegistry.THE_VOID || player.level().dimension() == NRegistry.THE_DEPTH){
            if (!player.isCreative() && !player.isSpectator()){
                this.prevTimeDarkness = this.timeDarkness;

                boolean isDark = player.level().getLightEngine().getRawBrightness(player.blockPosition(),15)==0.0F && !player.level().getData(NRegistry.THE_VOID_ATTACHMENT).flash;

                if (prevIsDark != isDark){
                    prevIsDark = isDark;
                }
                if (prevIsDark){
                    this.timeDarkness++;
                    if (this.timeDarkness > 100){
                        if (player.tickCount%20 == 0){
                            if (player instanceof ServerPlayer serverPlayer){
                                // 1.21.1: hurtServer()/hurtClient() no existen todavía, se usa hurt(DamageSource, float)
                                player.hurt(serverPlayer.damageSources().fellOutOfWorld(),3.0F);
                                checkCompendiumEvents(serverPlayer, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"darkness"),null);
                            }
                        }
                        this.timeDarkness = 100;
                    }
                }else {
                    timeDarkness = 0;
                }
            }


            if (!player.level().isClientSide()){
                if (player.getY()<=0){
                    Events.teleportToTheDepth(player.position(),player.level(),player);
                }
            }
            if (player.level().dimension() == NRegistry.THE_DEPTH){
                if (!player.level().isClientSide()){
                    if (player.getY()>=250){
                        Level level = player.level();
                        MinecraftServer server = ((ServerLevel) level).getServer();
                        ServerLevel serverLevel = server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("voidweaver", "void")));
                        // 1.21.1: TeleportTransition (con callback) no existe; el teletransporte
                        // entre dimensiones es síncrono, así que se llama a teleportTo y el
                        // código del callback original se ejecuta directamente después.
                        if (serverLevel != null && player instanceof ServerPlayer serverPlayer){
                            double x = player.getX();
                            double z = player.getZ();
                            serverPlayer.teleportTo(serverLevel, x, 2, z, Set.of(), player.getYRot(), player.getXRot());
                            serverLevel.setBlock(new BlockPos((int) x,0, (int) z), Blocks.DEEPSLATE.defaultBlockState(),3);
                            ArachneAttachment.get(serverPlayer).ifPresent(arachneAttachment -> {

                            });
                        }
                    }
                }
            }
        }else {
            this.prevTimeDarkness = 0;
            timeDarkness = 0;
        }
        Inventory inventory = player.getInventory();
        if (inventory.getTimesChanged() != previousTimesChanged) {
            previousTimesChanged = inventory.getTimesChanged();
            this.refreshQuest(player);
        }
        if (isCocoon){
            flag = false;
            if (!player.level().isClientSide()){
                if (this.cocoonTime>0){
                    this.cocoonTime--;
                    if (this.cocoonTime%5==0){
                        player.heal(1.0F);
                        int food = player.getFoodData().getFoodLevel();
                        player.getFoodData().setFoodLevel(Math.max(1,food-1));
                    }
                    if (this.cocoonTime==0){
                        isCocoon = false;
                        player.level().getEntities(player,player.getBoundingBox().inflate(5)).forEach(e->{
                            Vec3 direction = player.position().subtract(e.position()).normalize().scale(1.25F);
                            e.push(-direction.x,0.4F,-direction.z);
                            if (e instanceof LivingEntity living){
                                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,60,2));
                            }
                        });
                        PacketDistributor.sendToPlayer((ServerPlayer) player,new PacketHandlerParticle(0,player.blockPosition()));
                        player.syncData(NRegistry.ARACNE);

                    }
                }
            }
        }


        if(player.level().isClientSide()){

            this.speechTimeO = this.speechTime;

            if(this.speechTime>0){
                this.speechTime--;
            }

            if(this.idleTimer<=0){
                this.idle.start(player.tickCount);
                this.idleTimer = 60;
            }else {
                this.idleTimer--;
            }

        }

        if (this.scissorAttack){
            if (this.scissorAttackTime>0){
                this.scissorAttackTime--;
                if (this.scissorAttackTime == 6){
                    if (!player.level().isClientSide()){
                        boolean isHurtAnyEntity= false;
                        for (Entity living :  player.level().getEntities(player, player.getBoundingBox().inflate(RANGE_SCISSORS),
                                living -> {
                                    if (!(living instanceof LivingEntity)) {
                                        return false;
                                    }
                                    if (player.distanceToSqr(living) < 1F){
                                        return true;
                                    }

                                    double dx = living.getX() - player.getX();
                                    double dz = living.getZ() - player.getZ();

                                    double distanceSqr = dx * dx + dz * dz;

                                    if (distanceSqr < 1.0E-7) {
                                        return true;
                                    }

                                    double angleToEntity = Math.toDegrees(Math.atan2(dz, dx)) - 90.0D;

                                    double angleDifference = Mth.wrapDegrees((float)(angleToEntity - player.getYRot()));

                                    return Math.abs(angleDifference) <= CONE_ANGLE_SCISSORS;
                                })){
                            // 1.21.1: hurt(DamageSource, float) en lugar de hurtServer(ServerLevel, DamageSource, float)
                            if (((LivingEntity) living).hurt(player.damageSources().generic(),15.0F)){
                                isHurtAnyEntity = true;
                            }
                        }
                        if (isHurtAnyEntity){
                            if (player.getMainHandItem().has(DataComponents.DAMAGE)){
                                int damage = player.getMainHandItem().getDamageValue();
                                player.getMainHandItem().set(DataComponents.DAMAGE,Math.max(0,damage-5));
                            }
                        }
                        player.level().playSound(null,player, SoundEvents.UI_STONECUTTER_TAKE_RESULT,SoundSource.PLAYERS,1.0F,1.0F);
                    }
                }
                if (this.scissorAttackTime == 10 && player.level().isClientSide()){
                    float yaw = (float) (player.getYRot()/180.0F * Math.PI - Math.PI/2.0f);

                    for (float currentYaw = (float) (yaw-Math.toRadians(30.0F)); currentYaw < yaw+Math.toRadians(30.0F) ; currentYaw+=0.0872664626F){
                        float sin = Mth.sin(currentYaw);
                        float cos = Mth.cos(currentYaw);
                        player.level().addParticle(ParticleTypes.SWEEP_ATTACK,player.getX() -cos * RANGE_SCISSORS,player.getY() + 1.0F,player.getZ() -sin * RANGE_SCISSORS,0.0F,0.0F,0.0F);
                    }
                }


                if (this.scissorAttackTime == 0){
                    this.scissorAttack = false;
                }
                if (this.scissorAttackTime==18){
                    float yaw = (float) (player.getYRot()/180.0F * Math.PI - Math.PI/2.0f);
                    float sin = Mth.sin(yaw);
                    float cos = Mth.cos(yaw);
                    player.setDeltaMovement(new Vec3(-cos*0.85f,0.2F,-sin*0.85F));
                }

            }
        }
        if (!player.level().isClientSide()){
            this.tickRunningHelmet(player);
        }
        this.updateText(player);
        for (BlessingData data : blessingData){
            if (data.hasCooldown){
                data.tick();
            }
        }
        return flag;
    }
    private void runningHelmetAttack(Player player) {
        double range = 0.5D;
        float coneAngle = 35.0F;

        Vec3 look = player.getLookAngle();
        // 1.21.1: getKnownSpeed() (reescritura de movimiento de 1.21.2+) no existe;
        // se usa getDeltaMovement() como aproximación de la velocidad actual.
        Vec3 attackerMotion = player.getDeltaMovement().scale(20.0F);
        double attackerSpeedProjection = look.dot(attackerMotion);

        attackerSpeedProjection = Math.max(0.0D, attackerSpeedProjection);

        double finalAttackerSpeedProjection = attackerSpeedProjection;
        player.level().getEntities(player, player.getBoundingBox().inflate(range), entity -> {
                    if (!(entity instanceof LivingEntity living)) {
                        return false;
                    }

                    if (living == player) {
                        return false;
                    }

                    if (wasRecentlyHit(living)) {
                        return false;
                    }

                    Vec3 direction = living.position().subtract(player.position());
                    direction = new Vec3(direction.x, 0, direction.z);

                    if (direction.lengthSqr() < 1.0E-6D) {
                        return false;
                    }

                    direction = direction.normalize();

                    Vec3 horizontalLook = new Vec3(look.x, 0, look.z);

                    if (horizontalLook.lengthSqr() < 1.0E-6D) {
                        return false;
                    }

                    horizontalLook = horizontalLook.normalize();

                    double dot = horizontalLook.dot(direction);
                    double angle = Math.toDegrees(
                            Math.acos(Mth.clamp(dot, -1.0D, 1.0D))
                    );

                    return angle <= coneAngle;
                }
        ).forEach(entity -> {
            LivingEntity living = (LivingEntity) entity;

            Vec3 targetMotion = living.getDeltaMovement();
            double targetSpeedProjection = look.dot(targetMotion);

            double relativeSpeed = Math.max(0.0D, finalAttackerSpeedProjection - targetSpeedProjection);
            float damage = (float) (relativeSpeed * 1.5D);

            if (damage <= 0.0F) {
                return;
            }

            // 1.21.1: hurt(DamageSource, float) en lugar de hurtServer(ServerLevel, DamageSource, float)
            living.hurt(player.damageSources().playerAttack(player), damage);

            this.recentRunningHelmetEnemies.put(entity, player.level().getGameTime());
        });
    }
    private void tickRunningHelmet(Player player) {
        boolean active = isRunningWithHelmet(player);
        if (active) {
            this.runningHelmetAttack(player);
            this.runningWithHelmet = true;
        } else {
            this.runningWithHelmet = false;
        }
    }
    private boolean isRunningWithHelmet(Player player) {

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

        if (!helmet.is(NRegistry.NEEDLE_HELMET.get())) {
            return false;
        }

        // 1.21.1: getKnownSpeed() no existe; se usa getDeltaMovement()
        Vec3 motion = player.getDeltaMovement();

        double horizontalSpeedSqr = motion.x * motion.x + motion.z * motion.z;
        return horizontalSpeedSqr > 0.9F ;
    }
    public void acceptQuest(ServerPlayer player,Quest quest){
        this.currentQuest = quest;
        this.timeQuest = quest.getType() == QuestsType.HUNT ? 24000 : 36000;
        PacketDistributor.sendToPlayer(player,new PacketPlayDialog(getAcceptQuestForType(quest.getType()),player.getId()));
        this.progressQuest = 0;
    }
    public ResourceLocation getFailQuest(){
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"arachne_quest_fail") ;
    }
    public ResourceLocation getAcceptQuestForType(QuestsType type){
        return type == QuestsType.COLLECT ? ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"arachne_quest_collect") : ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"arachne_quest_kill");
    }
    public void failQuest(ServerPlayer serverPlayer){
        if (this.currentQuest != null){
            PacketDistributor.sendToPlayer(serverPlayer,new PacketPlayDialog(getFailQuest(),serverPlayer.getId()));
            currentReputation = Math.max(0,currentReputation-10);
            this.currentQuest = null;
        }
    }

    public void completeQuest(ServerPlayer serverPlayer){
        if (this.currentQuest != null){
            if (this.currentQuest.getType() == QuestsType.COLLECT){
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(currentQuest.getTargetId()));
                serverPlayer.getInventory().clearOrCountMatchingItems((itemStack -> itemStack.is(item)),currentQuest.getMaxProgress(),serverPlayer.inventoryMenu.getCraftSlots());
            }
            playDialog(ResourceLocation.parse(DIALOGS_FOR_TYPE.get(currentQuest.getType())[serverPlayer.getRandom().nextInt(0,6)]));
            setCurrentReputation(serverPlayer,currentReputation + currentQuest.getReputation());
            this.currentQuest = null;
            serverPlayer.syncData(NRegistry.ARACNE);
        }
    }

    public void setCurrentReputation(ServerPlayer serverPlayer,int reputation){
        currentReputation= Math.min(100,reputation);
    }



    public static float getSpiderCrosshairAmount(Player player, double maxDistance) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();

        AABB searchBox = player.getBoundingBox().expandTowards(look.scale(maxDistance)).inflate(2.0);

        Mob closestSpider = null;
        double closestAngle = Double.MAX_VALUE;

        for (Mob spider : player.level().getEntitiesOfClass(Mob.class, searchBox, (e)->e.getType().is(EntityTypeTags.ARTHROPOD))) {
            Vec3 target = spider.getBoundingBox().getCenter().subtract(eyePos).normalize();

            double dot = look.dot(target);
            double angle = 1.0 - dot;

            if (angle < closestAngle) {
                closestAngle = angle;
                closestSpider = spider;
            }
        }

        if (closestSpider == null)
            return 0.0F;


        double maxAngle = Math.toRadians(15.0);

        double angle = Math.acos(Mth.clamp(1.0 - closestAngle, -1.0, 1.0));

        return (float) Mth.clamp(1.0 - angle / maxAngle, 0.0, 1.0);
    }

    private void updateText(Player player) {
        if (this.currentDialog == null)return;
        Dialog dialog = DialogsManager.getDialog().get(ResourceLocation.parse(currentDialog));

        if (time>0){
            time--;
            if (time==0){
                if (dialog.answers().size()==index){
                    this.bufferText.clear();
                }else {
                    if (this.bufferText.size()==2){
                        this.bufferText.removeLast();
                        this.bufferText.addFirst(text);
                    }else {
                        this.bufferText.addFirst(text);
                    }
                }
                text="";
            }
            return;
        }
        if (completeText) {
            return;
        }

        if (dialog.answers().size()==index){
            completeText = true;
            return;
        }
        String targetText = dialog.answers().get(index);
        if (player.tickCount % 3 != 0) {

            return;
        }
        if (text.length() < targetText.length()) {
            text += targetText.charAt(text.length());
        }

        if (text.length() >= targetText.length()) {
            index++;
            time = 20;
        }

        if (player.level().isClientSide()){
            SoundEvent event1 = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(dialog.sounds().get(player.level().getRandom().nextInt(0,dialog.sounds().size()))));
            Minecraft.getInstance().getSoundManager().play(new EntityBoundSoundInstance(event1, SoundSource.NEUTRAL, 1.0F, 1.0F, player,player.level().getRandom().nextLong()));
        }

    }

    public void setTeleportBackPos(BlockPos pos){
        this.teleportBack = pos;
    }
    public float getAnimDarkness (float partialTick){
        return (Mth.lerp(partialTick,(float)prevTimeDarkness,(float)timeDarkness)) / 100.0F;
    }

    public void copyFrom(ArachneAttachment cap){
        this.transformComplete = cap.transformComplete;
        if(this.currentQuest!=null){
            this.currentQuest = null;
            this.currentReputation -= Math.max(this.currentReputation/2,0);
        }
        cap.event.setVisible(false);
        cap.event.removeAllPlayers();
        this.event.setVisible(false);
        this.event.removeAllPlayers();
    }

    public void init(){
        if (this.compendiumData.isEmpty()){
            this.initCompendiumData();
        }
        if (this.blessingData.isEmpty()){
            this.initBlessingData();
        }
    }

    public void startBlessingCooldown(BlessingData.BlessingType type){
        for (BlessingData data : this.blessingData){
            if (data.type == type){
                data.cooldownData.startCooldown();
            }
        }
    }
    public boolean blessingIsActive(BlessingData.BlessingType type){
        for (BlessingData data : this.blessingData){
            if (data.type == type){
                return data.isUnlock();
            }
        }
        return false;
    }

    private void initBlessingData() {
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_MOVE,new CooldownData(0),5,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_ANTI_FALL,new CooldownData(100),15,false,true));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_FANG,new CooldownData(0),40,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_ALLIE,new CooldownData(0),50,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_INFECTION,new CooldownData(0),70,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_PROTECTION,new CooldownData(200),90,false,true));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_FORM,new CooldownData(0),100,false,false));
    }

    private void initCompendiumData() {
        for (Map.Entry<ResourceLocation,Compendium> entry : CompendiumManager.getCompendiums().entrySet()){
            this.compendiumData.add(new CompendiumData(entry.getKey(),entry.getValue(),false));
        }
    }
    public boolean isCompleteCompendium(ResourceLocation id){
        for (CompendiumData data : compendiumData){
            if (data.identifier.toString().equals(id.toString())) {
                return data.unlock;
            }
        }
        return false;
    }
    public void checkCompendiumEvents(ServerPlayer player,ResourceLocation id, Action action){
        for (CompendiumData data : compendiumData){
            if (!data.unlock) {
                if (action == null){
                    if (data.compendium.getType() == Compendium.CompendiumType.EVENT){
                        CompendiumEvent compendiumEvent = (CompendiumEvent) data.compendium;
                        if (compendiumEvent.idEvent.equals(id.toString())){
                            data.unlock = true;
                            playDialog(ResourceLocation.parse(compendiumEvent.getDialog()));
                            player.syncData(NRegistry.ARACNE);
                        }
                    }
                }else {
                    if (data.compendium.getType() == Compendium.CompendiumType.ENTITY){
                        CompendiumEntity compendiumEvent = (CompendiumEntity) data.compendium;

                        if (compendiumEvent.action == action){
                            if (compendiumEvent.idEntity.equals(id.toString())){
                                data.unlock = true;
                                playDialog(ResourceLocation.parse(compendiumEvent.getDialog()));
                                player.syncData(NRegistry.ARACNE);
                            }
                        }
                    }
                }
            }
        }
    }
    public void addHex(Level level,Player player){
        if (hexes.size()==3)return;
        hexes.add(Hex.values()[level.getRandom().nextInt(0,3)]);

        this.timeHex = 100;
        player.syncData(NRegistry.ARACNE);
    }
    public void clearHexes(Player player){
        hexes.clear();
        player.syncData(NRegistry.ARACNE);
    }
    public void refreshQuest(Player player){
        if(this.currentQuest==null || this.currentQuest.getType() != QuestsType.COLLECT)return;
        Item itemQuest = BuiltInRegistries.ITEM.get(ResourceLocation.parse(this.currentQuest.getTargetId())).asItem();

        int countItem = player.getInventory().countItem(itemQuest);

        this.progressQuest = Math.min(countItem,this.currentQuest.getMaxProgress());
        player.syncData(NRegistry.ARACNE);
    }

    private BossEvent.BossBarColor getColorForQuestType() {
        switch (this.currentQuest.getType()){
            case HUNT -> {
                return BossEvent.BossBarColor.RED;
            }
            case COLLECT -> {
                return BossEvent.BossBarColor.GREEN;
            }
        }
        return BossEvent.BossBarColor.WHITE;
    }


    /**
     * 1.21.1: reemplaza al antiguo load(ValueInput tag). Ahora recibe
     * CompoundTag + HolderLookup.Provider (necesario para decodificar los
     * campos basados en Codec).
     */
    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        itemTransformDrop = tag.getBoolean("drop");
        transformComplete = tag.getBoolean("transform");
        progressQuest = tag.getInt("progress");
        currentReputation = tag.getInt("reputation");
        timeQuest = tag.getInt("timeQuest");

        currentQuest = QuestManager.getQuestForTittle(tag.contains("quest") ? tag.getString("quest") : " ");

        if (tag.contains("hexes")) {
            hexes = new ArrayList<>(Hex.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("hexes")).result().orElseGet(List::of));
        } else {
            hexes = new ArrayList<>();
        }
        timeHex = tag.getInt("timeHex");

        if (tag.contains("compendiumData")) {
            compendiumData = new ArrayList<>(CompendiumData.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("compendiumData")).result().orElseGet(List::of));
        } else {
            compendiumData = new ArrayList<>();
        }

        if (tag.contains("teleportX")) {
            teleportBack = new BlockPos(tag.getInt("teleportX"), tag.getInt("teleportY"), tag.getInt("teleportZ"));
        }

        if (tag.contains("blessingData")) {
            blessingData = new ArrayList<>(BlessingData.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("blessingData")).result().orElseGet(List::of));
        } else {
            blessingData = new ArrayList<>();
        }
    }
    private boolean wasRecentlyHit(Entity entity) {

        Long time = recentRunningHelmetEnemies.get(entity);

        return time != null && entity.level().getGameTime() - time < 10;
    }
    public static Optional<ArachneAttachment> get(Player player){
        return Optional.of(player.getData(NRegistry.ARACNE.get()));
    }

    public void playDialog(ResourceLocation identifier) {
        if (!DialogsManager.getDialog().containsKey(identifier))return;
        if (this.currentDialog!=null){
            if(this.currentDialog.equals(identifier.toString()))return;
            Dialog dialog = DialogsManager.getDialog().get(ResourceLocation.parse(currentDialog));
            for (String id : dialog.sounds()){
                Minecraft.getInstance().getSoundManager().stop(ResourceLocation.parse(id),SoundSource.AMBIENT);
            }
            this.text = "";
            this.index = 0;
            this.completeText = false;
            this.bufferText.clear();
        }
        this.currentDialog = identifier.toString();
    }

    /**
     * 1.21.1: IAttachmentSerializer<D, T> se implementa con D = CompoundTag,
     * ya que ValueInput/ValueOutput todavía no existen en esta versión.
     */
    public static class NerubianCapSerializer implements IAttachmentSerializer<CompoundTag, ArachneAttachment> {
        public static final StreamCodec<RegistryFriendlyByteBuf, ArachneAttachment> STREAM_CODEC =
                new StreamCodec<>() {
                    @Override
                    public void encode(RegistryFriendlyByteBuf buf, ArachneAttachment attachment) {
                        buf.writeInt(attachment.hexes.size());

                        for (Hex hex : attachment.hexes) {
                            buf.writeEnum(hex);
                        }

                        buf.writeInt(attachment.timeHex);
                        buf.writeInt(attachment.currentReputation);
                        buf.writeInt(attachment.progressQuest);
                        buf.writeInt(attachment.timeQuest);
                        buf.writeBoolean(attachment.currentQuest!=null);
                        if (attachment.currentQuest!=null){
                            buf.writeUtf(attachment.currentQuest.getTitle());
                        }
                        buf.writeInt(attachment.compendiumData.size());
                        for (CompendiumData data : attachment.compendiumData){
                            data.save(buf);
                        }
                        buf.writeBoolean(attachment.currentDialog!=null);
                        if (attachment.currentDialog!=null){
                            buf.writeUtf(attachment.currentDialog);
                            buf.writeInt(attachment.index);
                        }
                        buf.writeBoolean(attachment.teleportBack!=null);
                        if (attachment.teleportBack!=null){
                            buf.writeBlockPos(attachment.teleportBack);
                        }
                        buf.writeInt(attachment.blessingData.size());
                        for (BlessingData data : attachment.blessingData){
                            data.save(buf);
                        }
                        buf.writeUtf(attachment.text);
                        buf.writeInt(attachment.bufferText.size());
                        for (String s : attachment.bufferText){
                            buf.writeUtf(s);
                        }
                        buf.writeBoolean(attachment.isCocoon);
                        buf.writeInt(attachment.cocoonTime);
                        buf.writeInt(attachment.timeDarkness);
                        buf.writeInt(attachment.previousTimesChanged);
                        buf.writeInt(attachment.scissorAttackTime);
                        buf.writeBoolean(attachment.scissorAttack);
                    }

                    @Override
                    public ArachneAttachment decode(RegistryFriendlyByteBuf buf) {
                        ArachneAttachment attachment = new ArachneAttachment();

                        int size = buf.readInt();

                        for (int i = 0; i < size; i++) {
                            attachment.hexes.add(buf.readEnum(Hex.class));
                        }

                        attachment.timeHex = buf.readInt();
                        attachment.currentReputation = buf.readInt();
                        attachment.progressQuest = buf.readInt();
                        attachment.timeQuest = buf.readInt();
                        if (buf.readBoolean()){
                            attachment.currentQuest = QuestManager.getQuestForTittle(buf.readUtf());
                        }
                        int compendiumSize = buf.readInt();
                        for (int i = 0; i < compendiumSize ; i++){
                            attachment.compendiumData.add(new CompendiumData(buf));
                        }
                        if (buf.readBoolean()){
                            attachment.currentDialog = buf.readUtf();
                            attachment.index = buf.readInt();
                        }
                        if (buf.readBoolean()){
                            attachment.teleportBack = buf.readBlockPos();
                        }
                        int blessingSize = buf.readInt();
                        for (int i = 0; i< blessingSize ; i++){
                            attachment.blessingData.add(new BlessingData(buf));
                        }
                        attachment.text = buf.readUtf();
                        int bufferSize = buf.readInt();
                        for (int i = 0; i < bufferSize ; i++){
                            attachment.bufferText.add(buf.readUtf());
                        }
                        attachment.isCocoon = buf.readBoolean();
                        attachment.cocoonTime = buf.readInt();
                        attachment.timeDarkness = buf.readInt();
                        attachment.prevTimeDarkness = attachment.timeDarkness;
                        attachment.previousTimesChanged = buf.readInt();
                        attachment.scissorAttackTime = buf.readInt();
                        attachment.scissorAttack = buf.readBoolean();
                        return attachment;
                    }
                };

        @Override
        public ArachneAttachment read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            ArachneAttachment cap = new ArachneAttachment();
            cap.load(tag, provider);
            return cap;
        }

        @Override
        public CompoundTag write(ArachneAttachment attachment, HolderLookup.Provider provider) {
            CompoundTag output = new CompoundTag();
            output.putBoolean("drop", attachment.itemTransformDrop);
            output.putBoolean("transform", attachment.transformComplete);
            output.putInt("progress", attachment.progressQuest);
            output.putInt("reputation", attachment.currentReputation);
            output.putInt("timeQuest", attachment.timeQuest);

            Hex.CODEC.listOf().encodeStart(NbtOps.INSTANCE, attachment.hexes)
                    .result().ifPresent(t -> output.put("hexes", t));

            if (attachment.currentQuest != null) {
                output.putString("quest", attachment.currentQuest.getTitle());
            }

            output.putInt("timeHex", attachment.timeHex);

            CompendiumData.CODEC.listOf().encodeStart(NbtOps.INSTANCE, attachment.compendiumData)
                    .result().ifPresent(t -> output.put("compendiumData", t));

            if (attachment.teleportBack!=null){
                output.putInt("teleportX",attachment.teleportBack.getX());
                output.putInt("teleportY",attachment.teleportBack.getY());
                output.putInt("teleportZ",attachment.teleportBack.getZ());
            }

            BlessingData.CODEC.listOf().encodeStart(NbtOps.INSTANCE, attachment.blessingData)
                    .result().ifPresent(t -> output.put("blessingData", t));

            return output;
        }
    }

    public enum Hex {

        HEX_0(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_0.png")),
        HEX_1(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_1.png")),
        HEX_2(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_2.png")),
        HEX_3(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_3.png"));
        private final ResourceLocation location;
        public static final Codec<Hex> CODEC = Codec.STRING.xmap(Hex::valueOf, Hex::name);

        Hex(ResourceLocation identifier) {
            this.location = identifier;
        }

        public ResourceLocation getLocation() {
            return location;
        }
    }
}