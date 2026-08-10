package com.astianbk.aracnemod.server.cap;


import com.astianbk.aracnemod.QuestsType;
import com.astianbk.aracnemod.common.quests.Quest;
import com.astianbk.aracnemod.common.quests.QuestManager;
import com.astianbk.aracnemod.common.registry.NRegistry;
import com.astianbk.aracnemod.server.network.PacketNerubianData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.UUID;


public class NerubianCap {
    public AnimationState idle = new AnimationState();
    public AnimationState crouching = new AnimationState();
    public AnimationState attack = new AnimationState();
    public AnimationState use = new AnimationState();
    public AnimationState block = new AnimationState();
    public AnimationState burrow = new AnimationState();
    public AnimationState swim = new AnimationState();
    public boolean transformComplete = false;
    public boolean itemTransformDrop = false;
    public Quest currentQuest=null;
    public int speechTime=0;
    public int speechTimeO=0;
    public int timeQuest=0;
    public int progressQuest=0;
    public int idleTimer = 0;
    public int currentReputation = 0;
    public int previousTimesChanged = 0;
    public boolean isDirty = false;
    ServerBossEvent event =  Util.make(
            new ServerBossEvent(UUID.randomUUID(), Component.literal("this.getDisplayName()"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS),
            e -> e.setDarkenScreen(false));

    public String getTimeInMinuteAndSeconds(){
        int seconds = this.timeQuest/20;
        int minutes = seconds/60;
        seconds = seconds % 60;
        String sSeconds = seconds>9 ? String.valueOf(seconds) : "0"+seconds;
        String sMinutes = minutes>9 ? String.valueOf(minutes) : "0"+minutes;
        return sMinutes+":"+sSeconds;
    }

    public void tick(Player player){
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
                    this.currentQuest = null;
                    this.currentReputation -= 10;
                    this.progressQuest = 0;
                    this.timeQuest = 0;
                }
            }else {
                event.removeAllPlayers();
            }

            if(this.isDirty){
                PacketDistributor.sendToPlayer(serverPlayer,new PacketNerubianData(this.save()));
                this.isDirty = false;
            }

            if (player.tickCount%100 == 0){
                if (player.level().dimension() == NRegistry.THE_VOID){
                    if (player.level().getLightEngine().getRawBrightness(player.blockPosition(),0)==0.0F){
                        player.hurtServer(serverPlayer.level(),serverPlayer.damageSources().magic(),5.0F);
                    }
                }
            }
        }
        Inventory inventory = player.getInventory();
        if (inventory.getTimesChanged() != previousTimesChanged) {
            previousTimesChanged = inventory.getTimesChanged();
            this.refreshQuest(player);
        }

        if(player.level().isClientSide()){
            this.speechTimeO = this.speechTime;

            if(this.speechTime>0){
                this.speechTime--;
            }
            if(this.idleTimer<=0){
                this.idle.start(player.tickCount);
                this.idleTimer = 20;
            }else {
                this.idleTimer--;
            }

            this.crouching.animateWhen(player.isCrouching(),player.tickCount);
            //this.attack.animateWhen(player.getAttackAnim(1.0F)>0,player.tickCount);
            this.swim.animateWhen(player.isSwimming(),player.tickCount);
            this.block.animateWhen(player.getUseItem().getItem() instanceof ShieldItem,player.tickCount);
        }
    }

    public void copyFrom(NerubianCap cap){
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
        this.isDirty = true;
    }

    public boolean isNerubian(){
        return this.transformComplete;
    }

    public float getAnimSpeech(float partialTick){
        return Mth.lerp(partialTick,this.speechTimeO,this.speechTime) / 160.0F;
    }

    public void refreshQuest(Player player){
        if(this.currentQuest==null || this.currentQuest.getType() != QuestsType.COLLECT)return;
        Item itemQuest = BuiltInRegistries.ITEM.get(Identifier.parse(this.currentQuest.getTargetId())).get().value();
        int countItem = 0;
        for(int i = 0 ; i < player.getInventory().getContainerSize() ; i++){
            ItemStack item = player.getInventory().getItem(i);
            if(item.is(itemQuest)){
                countItem += item.getCount();
            }
        }
        this.progressQuest = Math.min(countItem,this.currentQuest.getMaxProgress());
    }

    private BossEvent.BossBarColor getColorForQuestType() {
        switch (this.currentQuest.getType()){
            case HUNT -> {
                return BossEvent.BossBarColor.RED;
            }
            case COLLECT -> {
                return BossEvent.BossBarColor.GREEN;
            }
            case SACRIFICE -> {
                return BossEvent.BossBarColor.PURPLE;

            }
        }
        return BossEvent.BossBarColor.WHITE;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("drop", itemTransformDrop);
        tag.putBoolean("transform", transformComplete);
        tag.putInt("progress", progressQuest);
        tag.putInt("reputation", currentReputation);
        tag.putInt("timeQuest", timeQuest);

        if(currentQuest != null) {
            tag.putString("quest", currentQuest.getTitle());
        }

        return tag;
    }

    public void load(ValueInput tag) {
        itemTransformDrop = tag.getBooleanOr("drop",false);
        transformComplete = tag.getBooleanOr("transform",false);
        progressQuest = tag.getIntOr("progress",0);
        currentReputation = tag.getIntOr("reputation",0);
        timeQuest = tag.getIntOr("timeQuest",0);

        currentQuest = QuestManager.getQuestForTittle(tag.getStringOr("quest"," "));
    }

    public static Optional<NerubianCap> get(Player player){
        return Optional.of(player.getData(NRegistry.ARACNE.get()));
    }
    public static class NerubianCapSerializer implements IAttachmentSerializer<NerubianCap> {

        @Override
        public NerubianCap read(IAttachmentHolder holder, ValueInput input) {
            NerubianCap cap = new NerubianCap();
            cap.load(input);
            return cap;
        }

        @Override
        public boolean write(NerubianCap attachment, ValueOutput output) {
            output.putBoolean("drop", attachment.itemTransformDrop);
            output.putBoolean("transform", attachment.transformComplete);
            output.putInt("progress", attachment.progressQuest);
            output.putInt("reputation", attachment.currentReputation);
            output.putInt("timeQuest", attachment.timeQuest);

            if (attachment.currentQuest != null) {
                output.putString("quest", attachment.currentQuest.getTitle());
            }

            return true;
        }
    }
}
