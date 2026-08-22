package com.astianbk.arachnemod.server.cap;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
        }else {
            checkTick++;
            if (checkTick>=200){
                startFlash(level);
                checkTick=0;
            }
        }
    }
    public void startFlash(Level level){
        flash = true;
        oldTick = 0;
        tick = 0;
        SoundEvent event = soundFlash[level.getRandom().nextInt(0,soundFlash.length-1)];
        level.players().forEach(player -> level.playLocalSound(player,event,SoundSource.AMBIENT,2.0F,1.0F));
    }
    public float getIntensityFlash(float partial) {
        float t = Mth.clamp(Mth.lerp(partial, oldTick, tick) / 200.0F, 0.0F, 1.0F);

        return Mth.sin(t * Mth.PI) ;
    }
    public static class TheVoidSerializer implements IAttachmentSerializer<TheVoidAttachment> {

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
