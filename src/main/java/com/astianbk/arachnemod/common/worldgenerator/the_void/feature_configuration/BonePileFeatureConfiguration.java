package com.astianbk.arachnemod.common.worldgenerator.the_void.feature_configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record BonePileFeatureConfiguration(int minCount, int maxCount, int maxAttempts) implements FeatureConfiguration {
    public static final Codec<BonePileFeatureConfiguration> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.fieldOf("min_count")
                                    .forGetter(BonePileFeatureConfiguration::minCount),

                            Codec.INT.fieldOf("max_count")
                                    .forGetter(BonePileFeatureConfiguration::maxCount),

                            Codec.INT.fieldOf("max_attempts")
                                    .forGetter(BonePileFeatureConfiguration::maxAttempts)
                    ).apply(instance, BonePileFeatureConfiguration::new)
            );
}
