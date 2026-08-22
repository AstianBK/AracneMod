package com.astianbk.arachnemod.client.gui;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.dialogs.Dialog;
import com.astianbk.arachnemod.common.dialogs.DialogsManager;
import com.astianbk.arachnemod.common.quests.Quest;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class ArachneSpeechGui implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {

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
            for (String s : arachneAttachment.bufferText){
                guiGraphics.centeredText(mc.font, Component.literal(s), j1+45, k1-10-10*e,ARGB.color(255,0,0));
                e++;
            }
            guiGraphics.centeredText(mc.font, Component.literal(arachneAttachment.text), j1+45, k1,ARGB.color(255,0,0));
        });
    }


}
