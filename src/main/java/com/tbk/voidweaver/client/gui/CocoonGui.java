package com.tbk.voidweaver.client.gui;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CocoonGui implements LayeredDraw.Layer{
    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/cocoonshield/cocoon_overlay.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int height = guiGraphics.guiHeight();
        int width = guiGraphics.guiWidth();
        Player player = Minecraft.getInstance().player;
        assert player != null;
        if (!player.isCreative() && !player.isSpectator()){
            ArachneAttachment.get(player).ifPresent(arachnePlayer->{
                if (arachnePlayer.isCocoon){
                    guiGraphics.blit(LOCATION, (int) 0, (int) 0,0,0,width,height,width,height);
                }
            });
        }

    }
}
