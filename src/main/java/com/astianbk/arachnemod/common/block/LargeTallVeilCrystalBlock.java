package com.astianbk.arachnemod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;

public class LargeTallVeilCrystalBlock extends Block {
    public static final EnumProperty<SpeleothemThickness> THICKNESS = BlockStateProperties.SPELEOTHEM_THICKNESS;

    public LargeTallVeilCrystalBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(THICKNESS, SpeleothemThickness.BASE));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (level.isClientSide()) return;

        if (state.getValue(THICKNESS) != SpeleothemThickness.BASE) return;

        BlockPos middlePos = pos.above();
        BlockPos tipPos = middlePos.above();

        if (!canReplace(level, middlePos) || !canReplace(level, tipPos)) {
            level.destroyBlock(pos, true);
            return;
        }

        level.setBlock(middlePos, defaultBlockState().setValue(THICKNESS, SpeleothemThickness.MIDDLE), 3);

        level.setBlock(tipPos, defaultBlockState().setValue(THICKNESS, SpeleothemThickness.TIP),3);
    }

    private boolean canReplace(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        return state.isAir() || state.canBeReplaced();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        SpeleothemThickness thickness = state.getValue(THICKNESS);

        switch (thickness) {

            case BASE -> {
                BlockState middle = level.getBlockState(pos.above());
                BlockState tip = level.getBlockState(pos.above(2));

                return isThickness(middle, SpeleothemThickness.MIDDLE) && isThickness(tip, SpeleothemThickness.TIP);
            }

            case MIDDLE -> {
                BlockState base = level.getBlockState(pos.below());
                BlockState tip = level.getBlockState(pos.above());

                return isThickness(base, SpeleothemThickness.BASE) && isThickness(tip, SpeleothemThickness.TIP);
            }

            case TIP -> {
                BlockState middle = level.getBlockState(pos.below());
                BlockState base = level.getBlockState(pos.below(2));

                return isThickness(middle, SpeleothemThickness.MIDDLE) && isThickness(base, SpeleothemThickness.BASE);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isThickness(BlockState state, SpeleothemThickness thickness) {
        return state.is(this) && state.getValue(THICKNESS) == thickness;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(THICKNESS);
    }
}
