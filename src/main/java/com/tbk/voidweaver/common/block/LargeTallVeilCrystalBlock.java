package com.tbk.voidweaver.common.block;

import com.tbk.voidweaver.AracneMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;

public class LargeTallVeilCrystalBlock extends Block {
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;

    public LargeTallVeilCrystalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(THICKNESS, DripstoneThickness.BASE));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }



    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (level.isClientSide()) return;

        if (state.getValue(THICKNESS) != DripstoneThickness.BASE) return;

        BlockPos middlePos = pos.above();
        BlockPos tipPos = middlePos.above();

        if (!canReplace(level, middlePos) || !canReplace(level, tipPos)) {
            level.destroyBlock(pos, true);
            return;
        }

        level.setBlock(middlePos, defaultBlockState().setValue(THICKNESS, DripstoneThickness.MIDDLE), 3);

        level.setBlock(tipPos, defaultBlockState().setValue(THICKNESS, DripstoneThickness.TIP),3);
    }

    private boolean canReplace(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        return state.isAir() || state.canBeReplaced();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
    }

    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DripstoneThickness thickness = state.getValue(THICKNESS);
        switch (thickness) {
            case BASE -> {
                BlockState middle = level.getBlockState(pos.above());
                BlockState tip = level.getBlockState(pos.above(2));

                return isThickness(middle, DripstoneThickness.MIDDLE) && isThickness(tip, DripstoneThickness.TIP);
            }

            case MIDDLE -> {
                BlockState base = level.getBlockState(pos.below());
                BlockState tip = level.getBlockState(pos.above());

                return isThickness(base, DripstoneThickness.BASE) && isThickness(tip, DripstoneThickness.TIP);
            }

            case TIP -> {
                BlockState middle = level.getBlockState(pos.below());
                BlockState base = level.getBlockState(pos.below(2));

                return isThickness(middle, DripstoneThickness.MIDDLE) && isThickness(base, DripstoneThickness.BASE);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isThickness(BlockState state, DripstoneThickness thickness) {
        return state.is(this) && state.getValue(THICKNESS) == thickness;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(THICKNESS);
    }
}
