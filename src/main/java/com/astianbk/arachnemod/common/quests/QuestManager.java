package com.astianbk.arachnemod.common.quests;

import com.astianbk.arachnemod.QuestsType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class QuestManager extends SimpleJsonResourceReloadListener<Quest> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Quest> quests = Lists.newArrayList();

    public QuestManager() {
        super(
                Quest.CODEC,
                new FileToIdConverter("quest", ".json")
        );
    }

    @Override
    protected void apply(Map<Identifier, Quest> identifierQuestMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        quests.clear();
        for (Map.Entry<Identifier, Quest> entry : identifierQuestMap.entrySet()) {
            Identifier Identifier = entry.getKey();
            try {

                Quest quest = entry.getValue();
                quests.add(quest);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading quests {}", Identifier, jsonparseexception);
            }
        }
    }


    public static Quest getQuestForTittle(String tittle){
        for (Quest quest: quests) {
            if(quest.getTitle().equals(tittle)){
                return quest;
            }
        }
        return null;
    }
    public static List<Quest> getQuests() {
        return quests;
    }
}
