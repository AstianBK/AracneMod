package com.astianbk.aracnemod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import org.jspecify.annotations.Nullable;

public class PointedUpBlock extends PointedDripstoneBlock {
    public PointedUpBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction defaultTipDirection = context.getNearestLookingVerticalDirection().getOpposite();
        Direction tipDirection = calculateTipDirection(level, pos, defaultTipDirection);
        if (tipDirection == Direction.DOWN){
            return null;
        }
        return super.getStateForPlacement(context);
    }

    private static @Nullable Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction defaultTipDirection) {
        Direction tipDirection;
        if (isValidPointedDripstonePlacement(level, pos, defaultTipDirection)) {
            tipDirection = defaultTipDirection;
        } else {
            if (!isValidPointedDripstonePlacement(level, pos, defaultTipDirection.getOpposite())) {
                return null;
            }

            tipDirection = defaultTipDirection.getOpposite();
        }

        return tipDirection;
    }
    private static boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction tipDirection) {
        BlockPos behindPos = pos.relative(tipDirection.getOpposite());
        BlockState behindState = level.getBlockState(behindPos);
        return behindState.isFaceSturdy(level, behindPos, tipDirection) || isPointedDripstoneWithDirection(behindState, tipDirection);
    }

    private static boolean isPointedDripstoneWithDirection(BlockState blockState, Direction tipDirection) {
        return blockState.is(Blocks.POINTED_DRIPSTONE) && blockState.getValue(TIP_DIRECTION) == tipDirection;
    }
}
