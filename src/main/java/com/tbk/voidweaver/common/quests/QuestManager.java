package com.tbk.voidweaver.common.quests;

import com.tbk.voidweaver.QuestsType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestManager extends SimpleJsonResourceReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new Gson();

    private static final List<Quest> quests = Lists.newArrayList();

    public QuestManager() {
        super(GSON, "quest");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        quests.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            try {
                Quest quest = Quest.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, entry.getValue()).getOrThrow();

                quests.add(quest);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading quest {}", resourceLocation, exception);
            } catch (Exception exception) {
                LOGGER.error("Error loading quest {}", resourceLocation, exception);
            }
        }
    }

    public static List<Quest> getQuestForType(QuestsType type) {

        List<Quest> list = new ArrayList<>();

        for (Quest quest : quests) {
            if (quest.getType() == type) {
                list.add(quest);
            }
        }

        return list;
    }

    public static Quest getQuestForTittle(String tittle) {

        for (Quest quest : quests) {
            if (quest.getTitle().equals(tittle)) {
                return quest;
            }
        }

        return null;
    }

    public static List<Quest> getQuests() {
        return quests;
    }
}