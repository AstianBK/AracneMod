package com.astianbk.arachnemod.common.quests;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.TierQuest;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class QuestHunt extends Quest{
    public static final MapCodec<QuestHunt> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("title").forGetter(Quest::getTitle),
                            TierQuest.CODEC.fieldOf("tier").forGetter(Quest::getTier),
                            Codec.INT.fieldOf("reputation").forGetter(Quest::getReputation),
                            Codec.INT.fieldOf("xp").forGetter(Quest::getXp),
                            Codec.STRING.fieldOf("entityTypeId").forGetter(QuestHunt::getTargetId),
                            Codec.INT.fieldOf("toHuntEntities").forGetter(QuestHunt::getMaxProgress)
                    ).apply(instance, QuestHunt::new));
    public String entityTypeId;
    public int toHuntEntities;
    public QuestHunt(String title, TierQuest tier, int reputation, int xp, String id,int toHunt) {
        super(title, QuestsType.HUNT, tier,reputation,xp);
        this.entityTypeId = id;
        this.toHuntEntities = toHunt;
    }
    public String getTargetId(){
        return this.entityTypeId;
    }
    public int getMaxProgress(){
        return this.toHuntEntities;
    }

    public boolean canAddProgress(String idTarget) {
        return this.entityTypeId.equals(idTarget);
    }
    public boolean isComplete(ArachneAttachment cap) {
        return cap.progressQuest==this.toHuntEntities;
    }
}
