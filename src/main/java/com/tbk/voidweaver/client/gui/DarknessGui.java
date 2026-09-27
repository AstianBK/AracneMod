package com.tbk.voidweaver.client.gui;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class DarknessGui implements LayeredDraw.Layer {
    public static final ResourceLocation[] LOCATIONS = {
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_0.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_1.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_2.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_3.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_4.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_5.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_6.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_7.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_8.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/darkness/void_darkness_9.png")
    };

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int height = guiGraphics.guiHeight();
        int width = guiGraphics.guiWidth();
        Player player = Minecraft.getInstance().player;
        assert player != null;
        if (!player.isCreative() && !player.isSpectator()){
            ArachneAttachment.get(player).ifPresent(arachnePlayer->{
                float alpha = arachnePlayer.getAnimDarkness(deltaTracker.getGameTimeDeltaTicks());
                int index = (int) (((player.tickCount * 0.3F + deltaTracker.getGameTimeDeltaTicks()) % 10 ));
                guiGraphics.blit(LOCATIONS[index], (int) 0, (int) 0,0,0,width,height,width,height);
            });
        }

    }
}
