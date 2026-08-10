package com.astianbk.arachnemod.common.quests;

import com.astianbk.arachnemod.QuestsType;
import com.astianbk.arachnemod.TierQuest;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class QuestSacrifice extends Quest{
    public static final MapCodec<QuestSacrifice> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("title").forGetter(Quest::getTitle),
                            Codec.STRING.fieldOf("description").forGetter(Quest::getDescription),
                            TierQuest.CODEC.fieldOf("tier").forGetter(Quest::getTier),
                            Codec.INT.fieldOf("reputation").forGetter(Quest::getReputation),
                            Codec.INT.fieldOf("xp").forGetter(Quest::getXp),
                            Codec.STRING.fieldOf("entityTypeId").forGetter(QuestSacrifice::getEntityTypeId)
                    ).apply(instance,(title,description,tier,rep,xp,id)->new QuestSacrifice(title,QuestsType.SACRIFICE,description,tier,rep,xp,id))
            );
    public String entityTypeId;
    public QuestSacrifice(String title, QuestsType type, String description, TierQuest tier, int reputation, int xp, String entityId) {
        super(title, QuestsType.COLLECT, description, tier,reputation,xp);
        this.entityTypeId = entityId;
    }

    public String getEntityTypeId() {
        return entityTypeId;
    }

    public boolean canAddProgress(String idTarget) {
        return this.entityTypeId.equals(idTarget);
    }
    public int getMaxProgress(){
        return 1;
    }
}
