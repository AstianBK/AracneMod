package com.astianbk.arachnemod.server.cap;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

public class TheVoidAttachment {
    public int nextCheck = 5000;
    public int checkTick = 0;
    public boolean flash = false;
    public int tick = 0;
    public int oldTick = 0;
    public boolean bedrockfall = false;
    public int shakeTime = 0;
    public int oldShakeTime = 0;
    public int bedrockfallTime = 0;
    public net.minecraft.sounds.SoundEvent[] soundFlash = {
            NRegistry.AMBIENCE_0.get(),
            NRegistry.AMBIENCE_1.get(),
            NRegistry.AMBIENCE_2.get(),
            NRegistry.AMBIENCE_3.get()
    };

    public void tick(Level level){


        if (this.flash){
            oldTick = tick;
            if (tick>=200){
                flash = false;
            }else {
                tick++;
            }
        }else if (this.bedrockfall){
            if (!level.isClientSide()){
                if (level.getRandom().nextFloat()<0.2){
                    level.players().forEach(player -> {
                        for (int i = 0 ; i < 3 ; i++){
                            FallingBlockEntity entity = FallingBlockEntity.fall(level,new BlockPos((int) (player.getRandomX(40)),300, (int)(  player.getRandomZ(40))),NRegistry.BEDROCK_TRANSPARENT_BLOCK.get().defaultBlockState());
                            level.addFreshEntity(entity);
                        }
                    });
                }
            }
            oldShakeTime = shakeTime;
            if (this.shakeTime<600){
                this.shakeTime++;
            }
            this.bedrockfallTime++;
            if (this.bedrockfallTime>=600){
                this.bedrockfall = false;
            }
        }else {
            checkTick++;
            if (checkTick>=6000){
                if (!level.isClientSide()){
                    if(level.getRandom().nextFloat()<0.64F){
                        startFlash(level);
                    }else {
                        startBedrockFall(level);
                    }
                    level.syncData(NRegistry.THE_VOID_ATTACHMENT);
                }
                checkTick=0;
            }
        }
    }
    public void startFlash(Level level){
        flash = true;
        oldTick = 0;
        tick = 0;
        SoundEvent event = soundFlash[level.getRandom().nextInt(0,soundFlash.length-1)];
        level.players().forEach(player -> {
            level.playLocalSound(player,event,SoundSource.AMBIENT,2.0F,1.0F);
            if (!level.isClientSide()){
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    arachneAttachment.checkCompendiumEvents((ServerPlayer) player, Identifier.fromNamespaceAndPath(AracneMod.MODID,"flash"),null);
                });
            }
        });
    }
    public void startBedrockFall(Level level){
        bedrockfall = true;
        oldShakeTime = 0;
        shakeTime = 0;
        bedrockfallTime = 0;
        level.players().forEach(player -> {
            level.playLocalSound(player,NRegistry.BEDROCKFALL.get(), SoundSource.AMBIENT,2.0F,1.0F);
            if (!level.isClientSide()){
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> arachneAttachment.checkCompendiumEvents((ServerPlayer) player, Identifier.fromNamespaceAndPath(AracneMod.MODID,"bedrockfall"),null));
            }
        });

    }
    public float getIntensityFlash(float partial) {
        float t = Mth.clamp(Mth.lerp(partial, oldTick, tick) / 200.0F, 0.0F, 1.0F);
        return Mth.sin(t * Mth.PI) ;
    }
    public float getIntensityShake(float partial) {
        float t = Mth.clamp(Mth.lerp(partial, oldShakeTime, shakeTime) / 600.0F, 0.0F, 1.0F);
        return Mth.sin(t * Mth.PI) ;
    }
    public static class TheVoidSerializer implements IAttachmentSerializer<TheVoidAttachment> {
        public static final StreamCodec<RegistryFriendlyByteBuf, TheVoidAttachment> STREAM_CODEC =
                new StreamCodec<>() {

                    @Override
                    public void encode(RegistryFriendlyByteBuf buf, TheVoidAttachment attachment) {
                        buf.writeBoolean(attachment.bedrockfall);
                        buf.writeBoolean(attachment.flash);
                        buf.writeInt(attachment.bedrockfallTime);
                        buf.writeInt(attachment.tick);
                        buf.writeInt(attachment.shakeTime);
                        buf.writeInt(attachment.oldShakeTime);
                    }

                    @Override
                    public TheVoidAttachment decode(RegistryFriendlyByteBuf buf) {
                        TheVoidAttachment attachment = new TheVoidAttachment();

                        attachment.bedrockfall = buf.readBoolean();
                        attachment.flash = buf.readBoolean();
                        attachment.bedrockfallTime = buf.readInt();
                        attachment.tick = buf.readInt();
                        attachment.shakeTime = buf.readInt();
                        attachment.oldShakeTime = buf.readInt();

                        return attachment;
                    }
                };
        @Override
        public TheVoidAttachment read(IAttachmentHolder holder, ValueInput input) {
            TheVoidAttachment cap = new TheVoidAttachment();
            
            return cap;
        }

        @Override
        public boolean write(TheVoidAttachment attachment, ValueOutput output) {

            return true;
        }
    }
}
