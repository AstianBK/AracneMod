package com.astianbk.arachnemod.common.compendium;

import com.astianbk.arachnemod.TierQuest;
import com.astianbk.arachnemod.common.quests.Quest;
import com.astianbk.arachnemod.common.quests.QuestHunt;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CompendiumEvent extends Compendium{
    public static final MapCodec<CompendiumEvent> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("idEvent").forGetter(CompendiumEvent::getIdEvent),
                            Codec.STRING.fieldOf("idDialog").forGetter(Compendium::getDialog)
                    ).apply(instance, CompendiumEvent::new));
    public String idEvent;
    public CompendiumEvent(String idEvent, String dialog) {
        super(CompendiumType.EVENT, dialog);
        this.idEvent = idEvent;
    }

    public String getIdEvent() {
        return idEvent;
    }
}
