package com.astianbk.arachnemod.server.cap;

import com.astianbk.arachnemod.AracneMod;
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
    public void tick(){
        if (this.flash){
            oldTick = tick;
            if (tick>=200){
                flash = false;
            }else {
                tick++;
            }
        }else {
            checkTick++;
            if (checkTick>=2600){
                startFlash();
                checkTick=0;
            }
        }
    }
    public void startFlash(){
        flash = true;
        oldTick = 0;
        tick = 0;
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
