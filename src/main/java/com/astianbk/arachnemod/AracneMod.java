package com.astianbk.arachnemod;

import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AracneMod.MODID)
public class AracneMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "arachnemod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public AracneMod(IEventBus modEventBus, ModContainer modContainer) {


        NRegistry.ATTACHMENTS.register(modEventBus);
        NRegistry.CHUNK_GENERATORS.register(modEventBus);
        NRegistry.BLOCKS.register(modEventBus);
        NRegistry.ITEMS.register(modEventBus);
        NRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        NRegistry.ENTITY_TYPES.register(modEventBus);
        NRegistry.EFFECTS.register(modEventBus);
        // Register the item to a creative tab

        NeoForge.EVENT_BUS.addListener(this::skyRender);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    public void skyRender(ExtractLevelRenderStateEvent event){
        event.getRenderState().skyRenderState.skybox = DimensionType.Skybox.NONE;
    }

}
