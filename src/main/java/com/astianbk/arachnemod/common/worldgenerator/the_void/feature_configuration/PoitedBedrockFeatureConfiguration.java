package com.astianbk.arachnemod.common.worldgenerator.feature_configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PoitedBedrockFeatureConfiguration(int minHeight, int maxHeight, int maxAttempts) implements FeatureConfiguration {
    public static final Codec<PoitedBedrockFeatureConfiguration> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.fieldOf("min_height")
                                    .forGetter(PoitedBedrockFeatureConfiguration::minHeight),

                            Codec.INT.fieldOf("max_height")
                                    .forGetter(PoitedBedrockFeatureConfiguration::maxHeight),

                            Codec.INT.fieldOf("max_attempts")
                                    .forGetter(PoitedBedrockFeatureConfiguration::maxAttempts)
                    ).apply(instance, PoitedBedrockFeatureConfiguration::new)
            );
}
