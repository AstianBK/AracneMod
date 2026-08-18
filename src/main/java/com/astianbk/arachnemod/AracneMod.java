package com.astianbk.arachnemod;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

import java.util.OptionalDouble;
import java.util.OptionalInt;

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

        NeoForge.EVENT_BUS.addListener(this::skyRender);
        NeoForge.EVENT_BUS.addListener(this::clientLevel);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    public void skyRender(ExtractLevelRenderStateEvent event){
        if(event.getLevel().dimension()==NRegistry.THE_VOID){
            event.getRenderState().skyRenderState.skybox = DimensionType.Skybox.NONE;
        }
    }
    public void clientLevel(LevelTickEvent.Post event){
        event.getLevel().getData(NRegistry.THE_VOID_ATTACHMENT).tick(event.getLevel());
    }





}
