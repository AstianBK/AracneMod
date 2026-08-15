package com.astianbk.arachnemod.mixin;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.AracneModClient;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.client.IRenderableSection;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin{
    @Shadow
     LevelRenderState levelRenderState;
    @Shadow
    private ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections;

    @Inject(method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4fc;)V", at = @At("HEAD"))
    private void addSkyMixin(FrameGraphBuilder frame, CameraRenderState cameraState, GpuBufferSlice skyFog, Matrix4fc modelViewMatrix, CallbackInfo ci) {
        if (levelRenderState.skyRenderState.skybox == DimensionType.Skybox.NONE && Minecraft.getInstance().player.level().dimension().equals(NRegistry.THE_VOID)){
            NeoForge.EVENT_BUS.post(new RenderLevelStageEvent.AfterSky(((LevelRenderer)((Object)this)), this.levelRenderState, (PoseStack)null, RenderSystem.getModelViewMatrix(), this.visibleSections));
        }
    }
}
