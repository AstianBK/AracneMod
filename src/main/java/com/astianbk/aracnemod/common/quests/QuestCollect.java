package com.astianbk.aracnemod.common.quests;

import com.astianbk.aracnemod.QuestsType;
import com.astianbk.aracnemod.TierQuest;
import com.astianbk.aracnemod.server.cap.NerubianCap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class QuestCollect extends Quest{
    public static final MapCodec<QuestCollect> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("title").forGetter(Quest::getTitle),
                            Codec.STRING.fieldOf("description").forGetter(Quest::getDescription),
                            TierQuest.CODEC.fieldOf("tier").forGetter(Quest::getTier),
                            Codec.INT.fieldOf("reputation").forGetter(Quest::getReputation),
                            Codec.INT.fieldOf("xp").forGetter(Quest::getXp),
                            Codec.STRING.fieldOf("itemId").forGetter(QuestCollect::getTargetId),
                            Codec.INT.fieldOf("toCollect").forGetter(QuestCollect::getMaxProgress)
                    ).apply(instance, (title,description,tier,reputation,xp,itemId,toCollect)->new QuestCollect(title,QuestsType.COLLECT,description,tier,reputation,xp,itemId,toCollect))
            );
    public String itemId;
    public int toCollect;
    public QuestCollect(String title, QuestsType type, String description, TierQuest tier, int reputation, int xp, String itemId,int toCollect) {
        super(title, QuestsType.COLLECT, description, tier,reputation,xp);
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

    public boolean isComplete(NerubianCap cap) {
        return cap.progressQuest==this.toCollect;
    }
}
