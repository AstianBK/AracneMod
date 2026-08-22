package com.astianbk.arachnemod.common.dialogs;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogsManager extends SimpleJsonResourceReloadListener<Dialog> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Identifier,Dialog> quests = new HashMap<>();

    public DialogsManager() {
        super(Dialog.CODEC.codec(), new FileToIdConverter("dialogs", ".json"));
    }

    @Override
    protected void apply(Map<Identifier, Dialog> identifierQuestMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        quests.clear();
        for (Map.Entry<Identifier, Dialog> entry : identifierQuestMap.entrySet()) {
            Identifier Identifier = entry.getKey();
            try {
                Dialog quest = entry.getValue();
                quests.put(entry.getKey(),quest);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading quests {}", Identifier, jsonparseexception);
            }
        }
    }


    public static Map<Identifier,Dialog> getDialog() {
        return quests;
    }
}
