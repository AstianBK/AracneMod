package com.astianbk.arachnemod.mixin;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class LightmapRenderStateExtractorMixin {
    @Shadow
    private boolean needsUpdate;
    @Shadow
    private GameRenderer renderer;
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private float blockLightFlicker;

    @Shadow
    private float calculateDarknessScale(LivingEntity camera, float darknessGamma, float partialTickTime) {
        return 0;
    }

    @Inject(method = "extract", at = @At("HEAD"),cancellable = true)
    private void aracnemod$voidFlash(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        ci.cancel();
        renderState.needsUpdate = this.needsUpdate;
        if (this.needsUpdate) {
            ClientLevel level = this.minecraft.level;
            LocalPlayer player = this.minecraft.player;
            if (level != null && player != null) {
                ProfilerFiller profiler = Profiler.get();
                profiler.push("lightmap");
                Camera camera = this.renderer.mainCamera();

                renderState.blockFactor = this.blockLightFlicker + 1.4F;
                renderState.blockLightTint = ARGB.vector3fFromRGB24((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.BLOCK_LIGHT_TINT, partialTicks));
                renderState.skyFactor = (Float)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, partialTicks);
                renderState.skyLightColor = ARGB.vector3fFromRGB24((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, partialTicks));
                EndFlashState endFlashState = level.endFlashState();
                float intensity;

                if (endFlashState != null && !(Boolean)this.minecraft.options.hideLightningFlash().get()) {
                    intensity = endFlashState.getIntensity(partialTicks);
                    if (this.minecraft.gui.hud.getBossOverlay().shouldCreateWorldFog()) {
                        renderState.skyFactor += intensity / 3.0F;
                    } else {
                        renderState.skyFactor += intensity;
                    }
                }else {
                    float f = minecraft.level.getData(NRegistry.THE_VOID_ATTACHMENT.get()).getIntensityFlash(partialTicks);
                    renderState.skyFactor += f;

                }

                renderState.ambientColor = ARGB.vector3fFromRGB24((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, partialTicks));
                intensity = ((Double)this.minecraft.options.gamma().get()).floatValue();
                float darknessEffectScaleOption = ((Double)this.minecraft.options.darknessEffectScale().get()).floatValue();
                float darknessEffectBrightnessModifier = player.getEffectBlendFactor(MobEffects.DARKNESS, partialTicks) * darknessEffectScaleOption;
                renderState.brightness = Math.max(0.0F,  intensity-darknessEffectBrightnessModifier );
                renderState.darknessEffectScale = this.calculateDarknessScale(player, darknessEffectBrightnessModifier, partialTicks) * darknessEffectScaleOption;
                float waterVision = player.getWaterVision();
                if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                    renderState.nightVisionEffectIntensity = GameRenderer.nightVisionScale(player, partialTicks);
                } else if (waterVision > 0.0F && player.hasEffect(MobEffects.CONDUIT_POWER)) {
                    renderState.nightVisionEffectIntensity = waterVision;
                } else {
                    renderState.nightVisionEffectIntensity = 0.0F;
                }

                renderState.nightVisionColor = ARGB.vector3fFromRGB24((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.NIGHT_VISION_COLOR, partialTicks));
                renderState.bossOverlayWorldDarkening = this.renderer.bossOverlayWorldDarkening(partialTicks);
                profiler.pop();
                this.needsUpdate = false;
            }
        }
    }
}