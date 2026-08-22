package com.astianbk.arachnemod.common.worldgenerator;

import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SpeleothemThickness;

import java.util.function.Consumer;

public class BedrockUtils {


    protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel level, BlockPos center, int xzRadius) {
        if (isEmptyOrWaterOrLava(level, center)) {
            return false;
        } else {
            float arcLength = 6.0F;
            float angleIncrement = 6.0F / (float)xzRadius;

            for(float angle = 0.0F; angle < 6.2831855F; angle += angleIncrement) {
                int dx = (int)(Mth.cos((double)angle) * (float)xzRadius);
                int dz = (int)(Mth.sin((double)angle) * (float)xzRadius);
                if (isEmptyOrWaterOrLava(level, center.offset(dx, 0, dz))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isEmptyOrWater(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, net.minecraft.world.level.levelgen.feature.SpeleothemUtils::isEmptyOrWater);
    }

    protected static boolean isEmptyOrWaterOrLava(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, net.minecraft.world.level.levelgen.feature.SpeleothemUtils::isEmptyOrWaterOrLava);
    }

    protected static void buildBaseToTipColumn(Direction direction, int totalLength, boolean mergedTip, Consumer<BlockState> consumer) {
        if (totalLength >= 3) {
            consumer.accept(createPointedDripstone(direction, SpeleothemThickness.BASE));

            for(int i = 0; i < totalLength - 3; ++i) {
                consumer.accept(createPointedDripstone(direction, SpeleothemThickness.MIDDLE));
            }
        }

        if (totalLength >= 2) {
            consumer.accept(createPointedDripstone(direction, SpeleothemThickness.FRUSTUM));
        }

        if (totalLength >= 1) {
            consumer.accept(createPointedDripstone(direction, mergedTip ? SpeleothemThickness.TIP_MERGE : SpeleothemThickness.TIP));
        }

    }

    public static void growPointedDripstone(LevelAccessor level, BlockPos startPos, Direction tipDirection, int height, boolean mergedTip) {
        if (isDripstoneBase(level.getBlockState(startPos.relative(tipDirection.getOpposite())))) {
            BlockPos.MutableBlockPos pos = startPos.mutable();
            buildBaseToTipColumn(tipDirection, height, mergedTip, (state) -> {
                if (state.is(NRegistry.POINTED_BEDROCK_BLOCK.get())) {
                    state = (BlockState)state.setValue(PointedDripstoneBlock.WATERLOGGED, level.isWaterAt(pos));
                }

                level.setBlock(pos, state, 2);
                pos.move(tipDirection);
            });
        }

    }

    public static boolean placeDripstoneBlockIfPossible(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return true;
    }

    private static BlockState createPointedDripstone(Direction direction, SpeleothemThickness thickness) {
        return (BlockState)((BlockState)NRegistry.POINTED_BEDROCK_BLOCK.get().defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, direction)).setValue(PointedDripstoneBlock.THICKNESS, thickness);
    }

    public static boolean isDripstoneBaseOrLava(BlockState state) {
        return isDripstoneBase(state) || state.is(Blocks.LAVA);
    }

    public static boolean isDripstoneBase(BlockState state) {
        return state.is(NRegistry.STONE_BEDROCK_BLOCK);
    }

    public static boolean isEmptyOrWater(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    public static boolean isNeitherEmptyNorWater(BlockState state) {
        return !state.isAir() && !state.is(Blocks.WATER);
    }

    public static boolean isEmptyOrWaterOrLava(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA);
    }
}
