package com.astianbk.arachnemod.common.compendium;

import com.astianbk.arachnemod.TierQuest;
import com.astianbk.arachnemod.common.quests.QuestCollect;
import com.astianbk.arachnemod.common.quests.QuestHunt;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.serialization.Codec;


public abstract class Compendium {
    public static final Codec<Compendium> CODEC =
            CompendiumType.CODEC.dispatch(
                    "type",
                    Compendium::getType,
                    type -> switch (type) {
                        case ENTITY -> CompendiumEntity.CODEC;
                        case EVENT -> CompendiumEvent.CODEC;
                    }
            );
    protected String dialog;
    protected CompendiumType type;

    public Compendium(CompendiumType type,String dialog) {
        this.dialog = dialog;
        this.type = type;

    }

    public String getDialog() {
        return dialog;
    }

    public CompendiumType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Compendium{" +
                "dialog='" + dialog + '\'' +
                ", type=" + type +
                '}';
    }

    public enum CompendiumType{
        ENTITY,
        EVENT;
        public static final Codec<CompendiumType> CODEC = Codec.STRING.xmap(CompendiumType::valueOf, CompendiumType::name);

    }
}