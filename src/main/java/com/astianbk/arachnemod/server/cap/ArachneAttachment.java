package com.astianbk.arachnemod.server.cap;


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.common.dialogs.Dialog;
import com.astianbk.arachnemod.common.dialogs.DialogsManager;
import com.astianbk.arachnemod.common.quests.Quest;
import com.astianbk.arachnemod.common.quests.QuestManager;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.NeoForge;
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
    ServerBossEvent event =  Util.make(new ServerBossEvent(UUID.randomUUID(), Component.literal("this.getDisplayName()"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS), e -> e.setDarkenScreen(false));
    public String currentDialog = null;
    public int index = 0;
    public List<String> bufferText = new ArrayList<>();
    public String text = "";
    public boolean completeText = false;
    public int time = 0;

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
                        }
                    }
                    this.timeDarkness = 100;
                }
            }else {
                timeDarkness = 0;
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
            this.updateText(player);
        }

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
            time = 30;
        }
        SoundEvent event1 = BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse(dialog.sounds().get(player.level().getRandom().nextInt(0,dialog.sounds().size()))));

        Minecraft.getInstance().getSoundManager().play(new EntityBoundSoundInstance(event1, SoundSource.NEUTRAL, 1.5F, 1.0F, player,player.level().getRandom().nextLong()));

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
        this.isDirty = true;
    }

    public boolean isNerubian(){
        return this.transformComplete;
    }

    public float getAnimSpeech(float partialTick){
        return Mth.lerp(partialTick,this.speechTimeO,this.speechTime) / 160.0F;
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



    public void load(ValueInput tag) {
        itemTransformDrop = tag.getBooleanOr("drop",false);
        transformComplete = tag.getBooleanOr("transform",false);
        progressQuest = tag.getIntOr("progress",0);
        currentReputation = tag.getIntOr("reputation",0);
        timeQuest = tag.getIntOr("timeQuest",0);

        currentQuest = QuestManager.getQuestForTittle(tag.getStringOr("quest"," "));
        hexes = new ArrayList<>(tag.read("hexes", Hex.CODEC.listOf()).orElseGet(List::of));
        timeHex = tag.getIntOr("timeHex",0);
    }

    public static Optional<ArachneAttachment> get(Player player){
        return Optional.of(player.getData(NRegistry.ARACNE.get()));
    }

    public void playDialog(Identifier identifier) {
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
                            AracneMod.LOGGER.info("addHex :{}",hex);
                        }

                        buf.writeInt(attachment.timeHex);
                    }

                    @Override
                    public ArachneAttachment decode(RegistryFriendlyByteBuf buf) {ArachneAttachment attachment = new ArachneAttachment();

                        int size = buf.readInt();

                        for (int i = 0; i < size; i++) {
                            attachment.hexes.add(buf.readEnum(Hex.class));
                            AracneMod.LOGGER.info("addHex :{}",attachment.hexes);
                        }

                        attachment.timeHex = buf.readInt();

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
