package com.astianbk.arachnemod.client.screen;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.quests.Quest;
import com.astianbk.arachnemod.server.cap.NerubianCap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;

public class IdolScreen extends Screen {
    protected static final Identifier[] FRAMES_SPEECH = new Identifier[]{
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_0.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_1.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_2.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_3.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_4.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_5.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_6.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_7.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_8.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_9.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_10.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_11.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_speech_12.png"),
    };
    protected static final Identifier[] FRAMES_BACKGROUND = new Identifier[]{
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_0.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_1.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_2.png"),
            Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/gui/weaver_background_3.png")
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
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        Minecraft mc = Minecraft.getInstance();
        if(mc.player==null)return;
        int height = graphics.guiHeight();
        int width = graphics.guiWidth();
        Player player = mc.player;
        int i = width / 2 -140;
        int j1 =  i + 101;
        int k1 = height - 58 ;

        float xExtra = -60;
        float yExtra = -155;

        float centerX = (j1 + xExtra);
        float centerY = (k1 + yExtra);

        int indexSpeech = (int) ((0.25F*(player.tickCount+ a)) % 13.0F);
        int indexBackground = (int) ((0.1F*(player.tickCount+ a)) % 4.0F);

        Identifier locationBackground = FRAMES_BACKGROUND[indexBackground];
//                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,locationBackground, 0, 0, width, height);

        Identifier locationSpeech = FRAMES_SPEECH[indexSpeech];
        graphics.blit(RenderPipelines.GUI_TEXTURED,locationSpeech, (int) centerX, (int) centerY,0.0F,0.0F,194,194,194, 194);
        if (currentQuest != null){
            int textWidth = font.width(this.text);

            graphics.text(font, this.text, j1 - textWidth / 2 +15, k1, ARGB.white(1.0F));
        }else {
            refreshQuest(player);
        }
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
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (!this.completeText){
            if (currentQuest !=null){
                this.text = currentQuest.getTitle();
                this.completeText = true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void refreshQuest(Player player) {
        NerubianCap.get(player).ifPresent(cap->{
            currentQuest = cap.currentQuest;
        });
    }
}
