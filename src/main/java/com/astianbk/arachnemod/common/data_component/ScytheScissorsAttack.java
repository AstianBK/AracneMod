package com.astianbk.arachnemod.common.data_component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ScytheScissorsAttack(int damage, int animationDuration, boolean prepare) {
    public static final Codec<ScytheScissorsAttack> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("damage").forGetter(ScytheScissorsAttack::damage),
                    Codec.INT.fieldOf("animation_duration").forGetter(ScytheScissorsAttack::animationDuration),
                    Codec.BOOL.fieldOf("prepare").forGetter(ScytheScissorsAttack::prepare)
            ).apply(instance, ScytheScissorsAttack::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ScytheScissorsAttack> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    ScytheScissorsAttack::damage,
                    ByteBufCodecs.INT,
                    ScytheScissorsAttack::animationDuration,
                    ByteBufCodecs.BOOL,
                    ScytheScissorsAttack::prepare,
                    ScytheScissorsAttack::new
            );

}
