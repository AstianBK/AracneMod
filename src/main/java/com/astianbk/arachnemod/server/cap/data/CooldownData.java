package com.astianbk.arachnemod.server.cap.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class CooldownData {
    public static final Codec<CooldownData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(data -> data.cooldown),
                    Codec.INT.fieldOf("max_time").forGetter(data -> data.maxTime)
            ).apply(instance, CooldownData::new)
    );
    public int cooldown;
    public int maxTime;
    public CooldownData(int maxTime){
        this.cooldown = 0;
        this.maxTime = maxTime;
    }
    public CooldownData(FriendlyByteBuf buf){
        this.cooldown = buf.readInt();
        this.maxTime = buf.readInt();
    }

    public CooldownData(int cooldown,int maxTime) {
        this.cooldown = cooldown;
        this.maxTime =maxTime;
    }

    public void save(FriendlyByteBuf buf){
        buf.writeInt(this.cooldown);
        buf.writeInt(this.maxTime);
    }


    public void tick(){
        if (this.cooldown > 0){
            this.cooldown--;
        }
    }

    public void startCooldown(){
        this.cooldown = maxTime;
    }

    public boolean finishCooldown(){
        return this.cooldown==0;
    }
}
