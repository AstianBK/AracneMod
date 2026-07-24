package com.astianbk.aracnemod.common.quests;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.QuestsType;
import com.astianbk.aracnemod.TierQuest;
import com.astianbk.aracnemod.server.cap.NerubianCap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class QuestHunt extends Quest{
    public static final MapCodec<QuestHunt> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("title").forGetter(Quest::getTitle),
                            Codec.STRING.fieldOf("description").forGetter(Quest::getDescription),
                            TierQuest.CODEC.fieldOf("tier").forGetter(Quest::getTier),
                            Codec.INT.fieldOf("reputation").forGetter(Quest::getReputation),
                            Codec.INT.fieldOf("xp").forGetter(Quest::getXp),
                            Codec.STRING.fieldOf("entityTypeId").forGetter(QuestHunt::getTargetId),
                            Codec.INT.fieldOf("toHuntEntities").forGetter(QuestHunt::getMaxProgress)
                    ).apply(instance,(tittle,descrip,tier,rep,xp,id,toHunt)->new QuestHunt(tittle,QuestsType.HUNT,descrip,tier,rep,xp,id,toHunt)));
    public String entityTypeId;
    public int toHuntEntities;
    public QuestHunt(String title, QuestsType type, String description, TierQuest tier, int reputation, int xp, String id,int toHunt) {
        super(title, QuestsType.HUNT, description, tier,reputation,xp);
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
        AracneMod.LOGGER.debug("target : "+this.entityTypeId);
        return this.entityTypeId.equals(idTarget);
    }
    public boolean isComplete(NerubianCap cap) {
        return cap.progressQuest==this.toHuntEntities;
    }
}
