package com.tbk.voidweaver.client.screen;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.quests.Quest;
import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class IdolScreen extends Screen {
    protected static final ResourceLocation[] FRAMES_SPEECH = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_0.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_1.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_2.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_3.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_4.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_5.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_6.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_7.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_8.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_9.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_10.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_11.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_12.png"),
    };
    protected static final ResourceLocation[] FRAMES_BACKGROUND = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_0.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_1.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_2.png"),
            ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_3.png")
    };
    public String text = "";
    public Quest currentQuest=null;
    public boolean completeText = false;
    public IdolScreen(Quest quest) {
        super(Component.empty());
        this.currentQuest = quest;
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player==null)return;
        int height = graphics.guiHeight();
        int width = graphics.guiWidth();
        Player player = mc.player;
        int i = width / 2 -140;
        int j1 =  i + 101;
        int k1 = height - 58 ;
        if (currentQuest != null){
            int textWidth = font.width(this.text);

            graphics.drawString(font, this.text, j1 - textWidth / 2 +15, k1, 16777215);
        }else {
            refreshQuest(player);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.completeText){
            Player player = Minecraft.getInstance().player;
            if (currentQuest !=null){
                if (player.tickCount % 5 == 0){
                    String text = currentQuest.getTitle();
                    int k = 0;
                    for (char c : text.toCharArray()){
                        if (this.text.toCharArray().length == k){
                            this.text+=c;
                            break;
                        }
                        k++;
                        if (this.text.length() == text.length()){
                            this.completeText = true;
                        }
                    }
                }
                if (player.tickCount % 15 == 0){
                    minecraft.getSoundManager().play(new SimpleSoundInstance(SoundEvents.ENDERMAN_AMBIENT, SoundSource.AMBIENT,1.5F,1F,player.getRandom(),player.blockPosition()));
                }
            }
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.completeText){
            if (currentQuest !=null){
                this.text = currentQuest.getTitle();
                this.completeText = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    private void refreshQuest(Player player) {
        ArachneAttachment.get(player).ifPresent(cap->{
            currentQuest = cap.currentQuest;
        });
    }
}
