package com.astianbk.arachnemod.common.compendium;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompendiumManager extends SimpleJsonResourceReloadListener<Compendium> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Identifier,Compendium> Compendiums = new HashMap<>();

    public CompendiumManager() {
        super(Compendium.CODEC, new FileToIdConverter("compendium", ".json"));
    }

    @Override
    protected void apply(Map<Identifier, Compendium> identifierCompendiumMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Compendiums.clear();
        for (Map.Entry<Identifier, Compendium> entry : identifierCompendiumMap.entrySet()) {
            Identifier identifier = entry.getKey();
            try {
                Compendium compendium = entry.getValue();
                Compendiums.put(identifier, compendium);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading Compendiums {}", identifier, jsonparseexception);
            }
        }
    }

    public static List<CompendiumEntity> getCompendiumEntity(){
        List<CompendiumEntity> list = new ArrayList<>();
        for (Compendium Compendium: Compendiums.values()) {
            if(Compendium.getType() == com.astianbk.arachnemod.common.compendium.Compendium.CompendiumType.ENTITY){
                list.add((CompendiumEntity) Compendium);
            }
        }
        return list;
    }
    public static Compendium getCompendiumForId(Identifier identifier){
        return Compendiums.getOrDefault(identifier,null);
    }
    public static Map<Identifier,Compendium> getCompendiums() {
        return Compendiums;
    }
}
