package com.tbk.voidweaver.client.gui;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.dialogs.Dialog;
import com.tbk.voidweaver.common.dialogs.DialogsManager;
import com.tbk.voidweaver.common.quests.Quest;
import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.player.Player;

public class ArachneSpeechGui implements LayeredDraw.Layer {

    @Override
    public void render(GuiGraphics guiGraphics, net.minecraft.client.DeltaTracker deltaTracker) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) {
            return;
        }

        ArachneAttachment.get(player).ifPresent(arachneAttachment -> {

            int height = guiGraphics.guiHeight();
            int width = guiGraphics.guiWidth();

            int i = width / 2 - 140;
            int j1 = i + 101;
            int k1 = height - 58;

            int e = 0;

            for (String s : arachneAttachment.bufferText) {
                guiGraphics.drawCenteredString(
                        mc.font,
                        Component.literal(s),
                        j1 + 45,
                        k1 - 10 - 10 * e,
                        0xFFFF0000
                );

                e++;
            }

            guiGraphics.drawCenteredString(mc.font, Component.literal(arachneAttachment.text), j1 + 45, k1, 0xFFFF0000);
        });
    }
}