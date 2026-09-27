package com.tbk.voidweaver.common.worldgenerator.the_void.feature;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.common.worldgenerator.the_void.BedrockUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;

import java.util.Optional;
import java.util.OptionalInt;

public class PoitedBedrockFeature extends Feature<PointedDripstoneConfiguration> {

    public PoitedBedrockFeature(Codec<PointedDripstoneConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PointedDripstoneConfiguration> context) {

        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        PointedDripstoneConfiguration config = context.config();
        RandomSource random = context.random();

        if (!BedrockUtils.isEmptyOrWater(level, origin)) {
            return false;
        }

        // Colocamos siempre la formación principal
        this.placeCrystal(level, random, origin, config);

        // Intentamos expandirla horizontalmente
        if (random.nextFloat() < config.chanceOfDirectionalSpread) {

            Direction direction = random.nextBoolean()
                    ? Direction.NORTH
                    : Direction.SOUTH;

            if (random.nextBoolean()) {
                direction = random.nextBoolean()
                        ? Direction.EAST
                        : Direction.WEST;
            }

            BlockPos pos = origin.relative(direction);

            this.placeCrystal(level, random, pos, config);

            // Segunda expansión
            if (random.nextFloat() < config.chanceOfSpreadRadius2) {
                pos = pos.relative(direction);
                this.placeCrystal(level, random, pos, config);

                // Tercera expansión
                if (random.nextFloat() < config.chanceOfSpreadRadius3) {
                    pos = pos.relative(direction);
                    this.placeCrystal(level, random, pos, config);
                }
            }
        }

        return true;
    }

    private void placeCrystal(
            WorldGenLevel level,
            RandomSource random,
            BlockPos pos,
            PointedDripstoneConfiguration config
    ) {

        if (!BedrockUtils.isEmptyOrWater(level, pos)) {
            return;
        }

        Direction.Axis axis = random.nextBoolean()
                ? Direction.Axis.X
                : Direction.Axis.Z;

        int height = 1;

        // Probabilidad de que sea una formación más alta
        if (random.nextFloat() < config.chanceOfTallerDripstone) {
            height += random.nextInt(1, 3);
        }

        BlockState crystal = NRegistry.POINTED_BEDROCK_BLOCK
                .get()
                .defaultBlockState();

        for (int i = 0; i < height; i++) {

            BlockPos targetPos = switch (axis) {
                case X -> pos.offset(i, 0, 0);
                case Y -> pos.offset(0, i, 0);
                case Z -> pos.offset(0, 0, i);
            };

            if (!BedrockUtils.isEmptyOrWater(level, targetPos)) {
                break;
            }

            level.setBlock(
                    targetPos,
                    crystal.setValue(
                            RotatedPillarBlock.AXIS,
                            axis
                    ),
                    3
            );
        }
    }
}