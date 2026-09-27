package com.tbk.voidweaver.server.cap.data;

import com.tbk.voidweaver.common.compendium.Compendium;
import com.tbk.voidweaver.common.compendium.CompendiumManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CompendiumData {
    public static final Codec<CompendiumData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(data -> data.identifier),
                    Codec.BOOL.fieldOf("unlock").forGetter(data -> data.unlock)
            ).apply(instance, (identifier, unlock) -> new CompendiumData(identifier, CompendiumManager.getCompendiumForId(identifier), unlock)));

    public ResourceLocation identifier;
    public Compendium compendium;
    public boolean unlock;
    public CompendiumData(ResourceLocation identifier, Compendium compendium, boolean unlock){
        this.identifier = identifier;
        this.compendium=compendium;
        this.unlock = unlock;
    }

    public CompendiumData(FriendlyByteBuf buf){
        this.identifier = buf.readResourceLocation();
        this.compendium = CompendiumManager.getCompendiumForId(this.identifier);
        this.unlock = buf.readBoolean();
    }
    public void save(FriendlyByteBuf buf){
        buf.writeResourceLocation(this.identifier);
        buf.writeBoolean(this.unlock);
    }

    @Override
    public String toString() {
        return "ResourceLocation :"+this.identifier +" Unlock :"+unlock + " Compendium :"+compendium;
    }
}
