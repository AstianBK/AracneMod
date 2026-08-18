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

public class IdolSpeechGui implements GuiLayer {
    public static final Identifier LOCATION = Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness.png");

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        int height = guiGraphics.guiHeight();
        int width = guiGraphics.guiWidth();
        Player player = Minecraft.getInstance().player;
        assert player != null && !player.isCreative() && !player.isSpectator();
        ArachneAttachment.get(player).ifPresent(arachnePlayer->{

            float alpha = arachnePlayer.getAnimDarkness(deltaTracker.getGameTimeDeltaTicks());
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,LOCATION, (int) 0, (int) 0,0,0,width,height,width,height,ARGB.white(alpha));
        });

    }
}
