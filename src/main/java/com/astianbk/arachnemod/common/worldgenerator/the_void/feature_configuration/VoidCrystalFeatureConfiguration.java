package com.astianbk.arachnemod.common.worldgenerator.feature_configuration;

import com.astianbk.arachnemod.common.worldgenerator.feature.VoidCrystalFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VoidCrystalFeatureConfiguration(int minHeight, int maxHeight, int maxAttempts) implements FeatureConfiguration {
    public static final Codec<VoidCrystalFeatureConfiguration> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.fieldOf("min_height")
                                    .forGetter(VoidCrystalFeatureConfiguration::minHeight),

                            Codec.INT.fieldOf("max_height")
                                    .forGetter(VoidCrystalFeatureConfiguration::maxHeight),

                            Codec.INT.fieldOf("max_attempts")
                                    .forGetter(VoidCrystalFeatureConfiguration::maxAttempts)
                    ).apply(instance, VoidCrystalFeatureConfiguration::new)
            );
}
