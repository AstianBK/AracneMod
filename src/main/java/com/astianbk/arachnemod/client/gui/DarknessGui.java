package com.astianbk.arachnemod.client.gui;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class DarknessGui implements GuiLayer {
    public static final Identifier[] LOCATIONS = {
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_0.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_1.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_2.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_3.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_4.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_5.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_6.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_7.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_8.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_9.png")
    };

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        int height = guiGraphics.guiHeight();
        int width = guiGraphics.guiWidth();
        Player player = Minecraft.getInstance().player;
        assert player != null;
        if (!player.isCreative() && !player.isSpectator()){
            ArachneAttachment.get(player).ifPresent(arachnePlayer->{
                float alpha = arachnePlayer.getAnimDarkness(deltaTracker.getGameTimeDeltaTicks());
                int index = (int) (((player.tickCount * 0.3F + deltaTracker.getGameTimeDeltaTicks()) % 10 ));
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,LOCATIONS[index], (int) 0, (int) 0,0,0,width,height,width,height,ARGB.white(alpha));
            });
        }

    }
}
