package com.astianbk.arachnemod.common.quests;

import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.TierQuest;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class QuestCollect extends Quest{
    public static final MapCodec<QuestCollect> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("title").forGetter(Quest::getTitle),
                            TierQuest.CODEC.fieldOf("tier").forGetter(Quest::getTier),
                            Codec.INT.fieldOf("reputation").forGetter(Quest::getReputation),
                            Codec.INT.fieldOf("xp").forGetter(Quest::getXp),
                            Codec.STRING.fieldOf("itemId").forGetter(QuestCollect::getTargetId),
                            Codec.INT.fieldOf("toCollect").forGetter(QuestCollect::getMaxProgress)
                    ).apply(instance, QuestCollect::new)
            );
    public String itemId;
    public int toCollect;
    public QuestCollect(String title, TierQuest tier, int reputation, int xp, String itemId,int toCollect) {
        super(title, QuestsType.COLLECT, tier,reputation,xp);
        this.itemId = itemId;
        this.toCollect = toCollect;
    }
    public String getTargetId(){
        return this.itemId;
    }
    public int getMaxProgress(){
        return this.toCollect;
    }
    public boolean canAddProgress(String idTarget) {
        return this.itemId.equals(idTarget);
    }

    public boolean isComplete(ArachneAttachment cap) {
        return cap.progressQuest==this.toCollect;
    }
}
