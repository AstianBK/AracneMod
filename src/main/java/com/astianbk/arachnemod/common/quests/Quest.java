package com.astianbk.arachnemod.common.quests;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.TierQuest;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.serialization.Codec;


public abstract class Quest {
    public static final Codec<Quest> CODEC =
            QuestsType.CODEC.dispatch(
                    "type",
                    Quest::getType,
                    type -> switch (type) {
                        case HUNT -> QuestHunt.CODEC;
                        case COLLECT -> QuestCollect.CODEC;
                    }
            );
    protected String title;
    protected QuestsType type;
    protected TierQuest tier;
    protected int reputation;
    protected int xp;
    public Quest(String title, QuestsType type, TierQuest tier, int reputation, int xp) {
        this.title = title;
        this.type = type;


        this.tier = tier;
        this.reputation = reputation;
        this.xp = xp;
    }
    public int getXp(){
        return this.xp;
    }

    public int getReputation() {
        return reputation;
    }

    public String getTargetId(){
        return null;
    }

    public String getTitle() {
        return title;
    }

    public QuestsType getType() {
        return this.type;
    }



    public TierQuest getTier(){
        return this.tier;
    }

    public int getMaxProgress(){
        return 0;
    }


    public boolean canAddProgress(String idTarget) {
        return false;
    }
    public boolean isComplete(ArachneAttachment cap) {
        return false;
    }
}