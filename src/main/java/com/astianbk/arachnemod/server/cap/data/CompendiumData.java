package com.astianbk.arachnemod.server.cap.data;

import com.astianbk.arachnemod.common.compendium.Compendium;
import com.astianbk.arachnemod.common.compendium.CompendiumManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

public class CompendiumData {
    public static final Codec<CompendiumData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("identifier").forGetter(data -> data.identifier),
                    Codec.BOOL.fieldOf("unlock").forGetter(data -> data.unlock)
            ).apply(instance, (identifier, unlock) -> new CompendiumData(identifier, CompendiumManager.getCompendiumForId(identifier), unlock)));

    public Identifier identifier;
    public Compendium compendium;
    public boolean unlock;
    public CompendiumData(Identifier identifier, Compendium compendium, boolean unlock){
        this.identifier = identifier;
        this.compendium=compendium;
        this.unlock = unlock;
    }

    public CompendiumData(FriendlyByteBuf buf){
        this.identifier = buf.readIdentifier();
        this.compendium = CompendiumManager.getCompendiumForId(this.identifier);
        this.unlock = buf.readBoolean();
    }
    public void save(FriendlyByteBuf buf){
        buf.writeIdentifier(this.identifier);
        buf.writeBoolean(this.unlock);
    }

    @Override
    public String toString() {
        return "Identifier :"+this.identifier +" Unlock :"+unlock + " Compendium :"+compendium;
    }
}
