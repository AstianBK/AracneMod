package com.astianbk.arachnemod.server.cap;


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.Events;
import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.common.compendium.*;
import com.astianbk.arachnemod.common.dialogs.Dialog;
import com.astianbk.arachnemod.common.dialogs.DialogsManager;
import com.astianbk.arachnemod.common.quests.Quest;
import com.astianbk.arachnemod.common.quests.QuestManager;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.data.BlessingData;
import com.astianbk.arachnemod.server.cap.data.CompendiumData;
import com.astianbk.arachnemod.server.cap.data.CooldownData;
import com.astianbk.arachnemod.server.network.PacketHandlerParticle;
import com.astianbk.arachnemod.server.network.PacketPlayDialog;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.*;


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
    ServerBossEvent event =  Util.make(new ServerBossEvent(UUID.randomUUID(), Component.literal("this.getDisplayName()"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS), e -> e.setDarkenScreen(false));
    public String currentDialog = null;
    public int index = 0;
    public List<String> bufferText = new ArrayList<>();
    public String text = "";
    public boolean completeText = false;
    public int time = 0;
    public int cocoonTime = 0;
    public final Map<QuestsType,String[]> DIALOGS_FOR_TYPE = Map.of(QuestsType.HUNT,new String[]{"arachnemod:arachne_quest_kill_complete1","arachnemod:arachne_quest_kill_complete2","arachnemod:arachne_quest_kill_complete3","arachnemod:arachne_quest_kill_complete4","arachnemod:arachne_quest_kill_complete5","arachnemod:arachne_quest_kill_complete6"},
            QuestsType.COLLECT,new String[]{"arachnemod:arachne_quest_collect_complete1","arachnemod:arachne_quest_collect_complete2","arachnemod:arachne_quest_collect_complete3","arachnemod:arachne_quest_collect_complete4","arachnemod:arachne_quest_collect_complete5","arachnemod:arachne_quest_collect_complete6"});
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

        if (player.level().dimension() == NRegistry.THE_VOID){
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
                                player.hurtServer(serverPlayer.level(),serverPlayer.damageSources().fellOutOfWorld(),3.0F);
                                checkCompendiumEvents(serverPlayer, Identifier.fromNamespaceAndPath(AracneMod.MODID,"darkness"),null);
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

        }

        if (player.level().dimension() == NRegistry.THE_DEPTH){
            if (!player.level().isClientSide()){
                if (player.getY()>=250){
                    Level level = player.level();
                    ServerLevel serverLevel = ((ServerLevel)level).getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("arachnemod", "void")));
                    player.teleport(new TeleportTransition(serverLevel,new Vec3(player.getX(),2,player.getZ()), Vec3.ZERO,0.0F,0.0F,(entity)->{
                        if (entity instanceof ServerPlayer serverPlayer){
                            serverLevel.setBlock(new BlockPos((int) player.getX(),0, (int) player.getZ()), Blocks.DEEPSLATE.defaultBlockState(),3);
                            ArachneAttachment.get(serverPlayer).ifPresent(arachneAttachment -> {

                            });
                        }
                    }));
                }
            }
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
                                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,60,2));
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
        this.updateText(player);
        for (BlessingData data : blessingData){
            if (data.hasCooldown){
                data.cooldownData.tick();
            }
        }
        return flag;
    }

    public void acceptQuest(ServerPlayer player,Quest quest){
        this.currentQuest = quest;
        this.timeQuest = quest.getType() == QuestsType.HUNT ? 24000 : 3600;
        PacketDistributor.sendToPlayer(player,new PacketPlayDialog(Identifier.parse(quest.getDescription()),player.getId()));
        this.progressQuest = 0;
    }

    public void failQuest(ServerPlayer serverPlayer){
        if (this.currentQuest != null){
            PacketDistributor.sendToPlayer(serverPlayer,new PacketPlayDialog(Identifier.parse(currentQuest.getDialogFail()),serverPlayer.getId()));
            currentReputation= Math.max(0,currentReputation-10);
            this.currentQuest = null;
        }
    }

    public void completeQuest(ServerPlayer serverPlayer){
        if (this.currentQuest != null){
            if (this.currentQuest.getType() == QuestsType.COLLECT){
                Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(currentQuest.getTargetId()));
                serverPlayer.getInventory().clearOrCountMatchingItems((itemStack -> itemStack.is(item)),currentQuest.getMaxProgress(),serverPlayer.inventoryMenu.getCraftSlots());
            }
            playDialog(Identifier.parse(DIALOGS_FOR_TYPE.get(currentQuest.getType())[serverPlayer.getRandom().nextInt(0,6)]));
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

        for (Mob spider : player.level().getEntitiesOfClass(Mob.class, searchBox, (e)->e.is(EntityTypeTags.ARTHROPOD))) {
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
        Dialog dialog = DialogsManager.getDialog().get(Identifier.parse(currentDialog));

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
            SoundEvent event1 = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(dialog.sounds().get(player.level().getRandom().nextInt(0,dialog.sounds().size()))));
            Minecraft.getInstance().getSoundManager().play(new EntityBoundSoundInstance(event1, SoundSource.NEUTRAL, 1.5F, 1.0F, player,player.level().getRandom().nextLong()));
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
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_ANTI_FALL,new CooldownData(24000),15,false,true));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_FANG,new CooldownData(0),40,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_ALLIE,new CooldownData(0),50,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_INFECTION,new CooldownData(0),70,false,false));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_PROTECTION,new CooldownData(200),90,false,true));
        this.blessingData.add(new BlessingData(BlessingData.BlessingType.ARACHNE_FORM,new CooldownData(0),100,false,false));
    }

    private void initCompendiumData() {
        for (Map.Entry<Identifier,Compendium> entry : CompendiumManager.getCompendiums().entrySet()){
            this.compendiumData.add(new CompendiumData(entry.getKey(),entry.getValue(),false));
        }
    }
    public boolean isCompleteCompendium(Identifier id){
        for (CompendiumData data : compendiumData){
            if (data.identifier.toString().equals(id.toString())) {
                return data.unlock;
            }
        }
        return false;
    }
    public void checkCompendiumEvents(ServerPlayer player,Identifier id, Action action){
        for (CompendiumData data : compendiumData){
            if (!data.unlock) {
                if (action == null){
                    if (data.compendium.getType() == Compendium.CompendiumType.EVENT){
                        CompendiumEvent compendiumEvent = (CompendiumEvent) data.compendium;
                        if (compendiumEvent.idEvent.equals(id.toString())){
                            data.unlock = true;
                            playDialog(Identifier.parse(compendiumEvent.getDialog()));
                            player.syncData(NRegistry.ARACNE);
                        }
                    }
                }else {
                    if (data.compendium.getType() == Compendium.CompendiumType.ENTITY){
                        CompendiumEntity compendiumEvent = (CompendiumEntity) data.compendium;

                        if (compendiumEvent.action == action){
                            if (compendiumEvent.idEntity.equals(id.toString())){
                                data.unlock = true;
                                playDialog(Identifier.parse(compendiumEvent.getDialog()));
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
        Item itemQuest = BuiltInRegistries.ITEM.get(Identifier.parse(this.currentQuest.getTargetId())).get().value();

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



    public void load(ValueInput tag) {
        itemTransformDrop = tag.getBooleanOr("drop",false);
        transformComplete = tag.getBooleanOr("transform",false);
        progressQuest = tag.getIntOr("progress",0);
        currentReputation = tag.getIntOr("reputation",0);
        timeQuest = tag.getIntOr("timeQuest",0);

        currentQuest = QuestManager.getQuestForTittle(tag.getStringOr("quest"," "));
        hexes = new ArrayList<>(tag.read("hexes", Hex.CODEC.listOf()).orElseGet(List::of));
        timeHex = tag.getIntOr("timeHex",0);

        compendiumData = new ArrayList<>(tag.read("compendiumData",CompendiumData.CODEC.listOf()).orElseGet(List::of));
        if (tag.getInt("teleportX").isPresent()){
            teleportBack = new BlockPos(tag.getIntOr("teleportX",0),tag.getIntOr("teleportY",0),tag.getIntOr("teleportZ",0));
        }
        blessingData = new ArrayList<>(tag.read("blessingData",BlessingData.CODEC.listOf()).orElseGet(List::of));
    }

    public static Optional<ArachneAttachment> get(Player player){
        return Optional.of(player.getData(NRegistry.ARACNE.get()));
    }

    public void playDialog(Identifier identifier) {
        if (!DialogsManager.getDialog().containsKey(identifier))return;
        if (this.currentDialog != null){
            Dialog dialog = DialogsManager.getDialog().get(Identifier.parse(currentDialog));
            for (String id : dialog.sounds()){
                Minecraft.getInstance().getSoundManager().stop(Identifier.parse(id),SoundSource.AMBIENT);
            }
            this.text = "";
            this.index = 0;
            this.completeText = false;
            this.bufferText.clear();
        }
        this.currentDialog = identifier.toString();

    }

    public static class NerubianCapSerializer implements IAttachmentSerializer<ArachneAttachment> {
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
                        return attachment;
                    }
                };

        @Override
        public ArachneAttachment read(IAttachmentHolder holder, ValueInput input) {
            ArachneAttachment cap = new ArachneAttachment();
            cap.load(input);
            return cap;
        }


        @Override
        public boolean write(ArachneAttachment attachment, ValueOutput output) {
            output.putBoolean("drop", attachment.itemTransformDrop);
            output.putBoolean("transform", attachment.transformComplete);
            output.putInt("progress", attachment.progressQuest);
            output.putInt("reputation", attachment.currentReputation);
            output.putInt("timeQuest", attachment.timeQuest);

            output.store("hexes",Hex.CODEC.listOf(), attachment.hexes);
            if (attachment.currentQuest != null) {
                output.putString("quest", attachment.currentQuest.getTitle());
            }

            output.putInt("timeHex", attachment.timeHex);
            output.store("compendiumData",CompendiumData.CODEC.listOf(),attachment.compendiumData);
            if (attachment.teleportBack!=null){
                output.putInt("teleportX",attachment.teleportBack.getX());
                output.putInt("teleportY",attachment.teleportBack.getY());
                output.putInt("teleportZ",attachment.teleportBack.getZ());
            }
            output.store("blessingData",BlessingData.CODEC.listOf(),attachment.blessingData);
            return true;
        }
    }

    public enum Hex {

        HEX_0(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_0.png")),
        HEX_1(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_1.png")),
        HEX_2(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_2.png")),
        HEX_3(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/hex/hex_3.png"));
        private final Identifier location;
        public static final Codec<Hex> CODEC = Codec.STRING.xmap(Hex::valueOf, Hex::name);

        Hex(Identifier identifier) {
            this.location = identifier;
        }

        public Identifier getLocation() {
            return location;
        }
    }
}
