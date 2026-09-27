package com.tbk.voidweaver.common.dialogs;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogsManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation,Dialog> quests = new HashMap<>();

    public DialogsManager() {
        super(GSON, "dialogs");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        quests.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            try {
                Dialog dialog = Dialog.CODEC.codec().parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
                quests.put(resourceLocation, dialog);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading quests {}", resourceLocation, jsonparseexception);
            } catch (Exception exception) {
                LOGGER.error("Error loading dialog {}", resourceLocation, exception);
            }
        }
    }


    public static Map<ResourceLocation,Dialog> getDialog() {
        return quests;
    }
}

