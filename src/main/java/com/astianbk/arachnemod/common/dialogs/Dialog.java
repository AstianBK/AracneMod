package com.astianbk.arachnemod.common.dialogs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;

public record Dialog(java.util.List<String> answers, java.util.List<String> sounds) {
    public static final MapCodec<Dialog> CODEC = RecordCodecBuilder.mapCodec(dialogInstance ->
            dialogInstance.group(Codec.STRING.listOf().fieldOf("Answers").forGetter((dialog)-> dialog.answers)
                    ,Codec.STRING.listOf().fieldOf("Sounds").forGetter(dialog -> dialog.sounds))
                    .apply(dialogInstance, Dialog::new));
}
