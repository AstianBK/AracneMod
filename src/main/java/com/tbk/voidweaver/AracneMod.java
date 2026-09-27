package com.tbk.voidweaver;

import com.tbk.voidweaver.common.registry.NRegistry;

import net.minecraft.world.level.dimension.DimensionType;

import net.neoforged.neoforge.event.tick.LevelTickEvent;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;


@Mod(AracneMod.MODID)
public class AracneMod {
    public static final String MODID = "arachnemod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public AracneMod(IEventBus modEventBus, ModContainer modContainer) {

        NRegistry.ATTACHMENTS.register(modEventBus);
        NRegistry.BLOCK_ENTITY_TYPE.register(modEventBus);
        NRegistry.CHUNK_GENERATORS.register(modEventBus);
        NRegistry.BLOCKS.register(modEventBus);
        NRegistry.ITEMS.register(modEventBus);
        NRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        NRegistry.STRUCTURE_TYPE.register(modEventBus);
        NRegistry.ENTITY_TYPES.register(modEventBus);
        NRegistry.EFFECTS.register(modEventBus);
        NRegistry.PIECES.register(modEventBus);
        NRegistry.FEATURE.register(modEventBus);
        NRegistry.STRUCTURE_PLACEMENT_TYPE.register(modEventBus);
        NRegistry.SOUNDS.register(modEventBus);
        NRegistry.ENTITY_DATA_SERIALIZER_DEFERRED_REGISTER.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::clientLevel);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    public void clientLevel(LevelTickEvent.Post event){
        if (event.getLevel().dimension() == NRegistry.THE_VOID ){
            event.getLevel().getData(NRegistry.THE_VOID_ATTACHMENT).tick(event.getLevel());
        }
    }

}
