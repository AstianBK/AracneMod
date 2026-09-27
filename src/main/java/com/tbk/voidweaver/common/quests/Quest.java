package com.tbk.voidweaver.common.quests;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.QuestsType;
import com.tbk.voidweaver.TierQuest;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
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