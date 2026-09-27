package com.tbk.voidweaver.common.compendium;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompendiumManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation,Compendium> Compendiums = new HashMap<>();

    public CompendiumManager() {
        // 1.21.1: (Gson, String directory) en vez de (Codec, FileToIdConverter)
        super(GSON, "compendium");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Compendiums.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation identifier = entry.getKey();
            try {
                Compendium compendium = Compendium.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
                Compendiums.put(identifier, compendium);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading Compendiums {}", identifier, jsonparseexception);
            } catch (Exception exception) {
                LOGGER.error("Error loading Compendium {}", identifier, exception);
            }
        }
    }

    public static List<CompendiumEntity> getCompendiumEntity(){
        List<CompendiumEntity> list = new ArrayList<>();
        for (Compendium Compendium: Compendiums.values()) {
            if(Compendium.getType() == com.tbk.voidweaver.common.compendium.Compendium.CompendiumType.ENTITY){
                list.add((CompendiumEntity) Compendium);
            }
        }
        return list;
    }
    public static Compendium getCompendiumForId(ResourceLocation identifier){
        return Compendiums.getOrDefault(identifier,null);
    }
    public static Map<ResourceLocation,Compendium> getCompendiums() {
        return Compendiums;
    }
}