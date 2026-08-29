package com.astianbk.arachnemod.server.cap.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class BlessingData {
    public static final Codec<BlessingData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.STRING.fieldOf("type").forGetter(data -> data.type.name()),
                    CooldownData.CODEC.fieldOf("cooldown").forGetter(data -> data.cooldownData),
                    Codec.INT.fieldOf("reputation").forGetter(data -> data.reputation),
                    Codec.BOOL.fieldOf("unlock").forGetter(data -> data.unlock),
                    Codec.BOOL.fieldOf("has_cooldown").forGetter(data -> data.hasCooldown)
            ).apply(instance, (type, cooldownData,reputation, unlock, hasCooldown) -> new BlessingData(BlessingType.valueOf(type), cooldownData,reputation, unlock, hasCooldown)));
    public CooldownData cooldownData;
    public BlessingType type;
    public int reputation;
    public boolean unlock;
    public boolean hasCooldown;
    public BlessingData(BlessingType type,CooldownData cooldownData,int reputation,boolean unlock,boolean hasCooldown){
        this.type =  type;
        this.cooldownData = cooldownData;
        this.reputation = reputation;
        this.unlock = unlock;
        this.hasCooldown = hasCooldown;
    }
    public BlessingData(FriendlyByteBuf buf){
        this.type = buf.readEnum(BlessingType.class);
        this.cooldownData = new CooldownData(buf);
        this.reputation = buf.readInt();
        this.unlock = buf.readBoolean();
        this.hasCooldown = buf.readBoolean();
    }
    public void save(FriendlyByteBuf buf){
        buf.writeEnum(this.type);
        this.cooldownData.save(buf);
        buf.writeInt(this.reputation);
        buf.writeBoolean(this.unlock);
        buf.writeBoolean(this.hasCooldown);
    }
    public boolean isUnlock(){
        return this.hasCooldown ? this.unlock && cooldownData.finishCooldown() : this.unlock;
    }
    public void tick(){
        this.cooldownData.tick();
    }
    public enum BlessingType{
        ARACHNE_MOVE,
        ARACHNE_ANTI_FALL,
        ARACHNE_FANG,
        ARACHNE_ALLIE,
        ARACHNE_INFECTION,
        ARACHNE_PROTECTION,
        ARACHNE_FORM;
    }
}
