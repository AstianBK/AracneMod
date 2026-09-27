package com.tbk.voidweaver.server.cap;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    public int MAX_FALL_BLOCk_FOR_TICK = 20;

    public void tick(Level level){
        if (this.flash){
            oldTick = tick;
            if (tick>=600){
                flash = false;
            }else {
                tick++;
            }
        }else if (this.bedrockfall){
            if (!level.isClientSide()){
                if (level.getRandom().nextFloat()<0.2){
                    int fall = 0;
                    for (Player player : level.players()){
                        if (fall == MAX_FALL_BLOCk_FOR_TICK)break;
                        for (int i = 0 ; i < 3 ; i++){
                            FallingBlockEntity entity = FallingBlockEntity.fall(level,new BlockPos((int) (player.getRandomX(40)),300, (int)(  player.getRandomZ(40))),NRegistry.BEDROCK_TRANSPARENT_BLOCK.get().defaultBlockState());
                            level.addFreshEntity(entity);
                            fall++;
                        }
                    }
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
            }
        }
    }

    public void startFlash(Level level){
        flash = true;
        oldTick = 0;
        tick = 0;
        checkTick = 0;
        SoundEvent event = soundFlash[level.getRandom().nextInt(0,soundFlash.length-1)];
        level.players().forEach(player -> {
            if (!level.isClientSide()){
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    arachneAttachment.checkCompendiumEvents((ServerPlayer) player, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"flash"),null);
                });
            }
            level.playSound(null,player,event,SoundSource.AMBIENT,10.0F,1.0F);
        });
    }
    public void stopFlash(Level level){
        flash = false;
        oldShakeTime = 0;
        shakeTime = 0;
        bedrockfallTime = 0;
        checkTick = 0;
    }
    public void stopBedrockFall(Level level){
        bedrockfall = false;
        oldShakeTime = 0;
        shakeTime = 0;
        bedrockfallTime = 0;
        checkTick = 0;
    }
    public void startBedrockFall(Level level){
        bedrockfall = true;
        oldShakeTime = 0;
        shakeTime = 0;
        bedrockfallTime = 0;
        checkTick = 0;

        level.players().forEach(player -> {
            level.playSound(null,player,NRegistry.BEDROCKFALL.get(), SoundSource.AMBIENT,5.0F,1.0F);
            if (!level.isClientSide()){
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> arachneAttachment.checkCompendiumEvents((ServerPlayer) player, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"bedrockfall"),null));
            }
        });
    }

    public float getIntensityFlash(float partial) {
        float t = Mth.clamp(Mth.lerp(partial, oldTick, tick) / 600.0F, 0.0F, 1.0F);
        return Mth.sin(t * Mth.PI) ;
    }

    public float getIntensityShake(float partial) {
        float t = Mth.clamp(Mth.lerp(partial, oldShakeTime, shakeTime) / 600.0F, 0.0F, 1.0F);
        return Mth.sin(t * Mth.PI) ;
    }

    public static class TheVoidSerializer
            implements IAttachmentSerializer<CompoundTag, TheVoidAttachment> {

        public static final StreamCodec<RegistryFriendlyByteBuf, TheVoidAttachment> STREAM_CODEC =
                new StreamCodec<>() {

                    @Override
                    public void encode(
                            RegistryFriendlyByteBuf buf,
                            TheVoidAttachment attachment
                    ) {
                        buf.writeInt(attachment.nextCheck);
                        buf.writeInt(attachment.checkTick);

                        buf.writeBoolean(attachment.flash);
                        buf.writeInt(attachment.tick);
                        buf.writeInt(attachment.oldTick);

                        buf.writeBoolean(attachment.bedrockfall);
                        buf.writeInt(attachment.shakeTime);
                        buf.writeInt(attachment.oldShakeTime);
                        buf.writeInt(attachment.bedrockfallTime);
                    }

                    @Override
                    public TheVoidAttachment decode(
                            RegistryFriendlyByteBuf buf
                    ) {
                        TheVoidAttachment attachment = new TheVoidAttachment();

                        attachment.nextCheck = buf.readInt();
                        attachment.checkTick = buf.readInt();

                        attachment.flash = buf.readBoolean();
                        attachment.tick = buf.readInt();
                        attachment.oldTick = buf.readInt();

                        attachment.bedrockfall = buf.readBoolean();
                        attachment.shakeTime = buf.readInt();
                        attachment.oldShakeTime = buf.readInt();
                        attachment.bedrockfallTime = buf.readInt();

                        return attachment;
                    }
                };

        @Override
        public TheVoidAttachment read(
                IAttachmentHolder holder,
                CompoundTag tag,
                HolderLookup.Provider provider
        ) {
            TheVoidAttachment attachment = new TheVoidAttachment();

            attachment.nextCheck =
                    tag.getInt("nextCheck");

            attachment.checkTick =
                    tag.getInt("checkTick");

            attachment.flash =
                    tag.getBoolean("flash");

            attachment.tick =
                    tag.getInt("tick");

            attachment.oldTick =
                    tag.getInt("oldTick");

            attachment.bedrockfall =
                    tag.getBoolean("bedrockfall");

            attachment.shakeTime =
                    tag.getInt("shakeTime");

            attachment.oldShakeTime =
                    tag.getInt("oldShakeTime");

            attachment.bedrockfallTime =
                    tag.getInt("bedrockfallTime");

            return attachment;
        }

        @Override
        public CompoundTag write(
                TheVoidAttachment attachment,
                HolderLookup.Provider provider
        ) {
            CompoundTag tag = new CompoundTag();

            tag.putInt(
                    "nextCheck",
                    attachment.nextCheck
            );

            tag.putInt(
                    "checkTick",
                    attachment.checkTick
            );

            tag.putBoolean(
                    "flash",
                    attachment.flash
            );

            tag.putInt(
                    "tick",
                    attachment.tick
            );

            tag.putInt(
                    "oldTick",
                    attachment.oldTick
            );

            tag.putBoolean(
                    "bedrockfall",
                    attachment.bedrockfall
            );

            tag.putInt(
                    "shakeTime",
                    attachment.shakeTime
            );

            tag.putInt(
                    "oldShakeTime",
                    attachment.oldShakeTime
            );

            tag.putInt(
                    "bedrockfallTime",
                    attachment.bedrockfallTime
            );

            return tag;
        }
    }
}
