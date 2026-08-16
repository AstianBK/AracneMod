package com.astianbk.arachnemod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TallVeilCrystalBlock extends DoublePlantBlock {
    public TallVeilCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return true;
        } else {
            BlockState belowState = level.getBlockState(pos.below());
            if (state.getBlock() != this) {
                return true;
            } else {
                return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
            }
        }
    }
}
