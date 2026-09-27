package com.tbk.voidweaver.common.compendium;

import com.tbk.voidweaver.TierQuest;
import com.tbk.voidweaver.common.quests.QuestCollect;
import com.tbk.voidweaver.common.quests.QuestHunt;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
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