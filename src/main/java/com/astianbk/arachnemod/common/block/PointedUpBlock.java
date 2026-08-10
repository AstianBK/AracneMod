package com.astianbk.arachnemod.common.block;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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
        if (tipDirection == null) {
            return null;
        } else {
            boolean mergeOpposingTips = !context.isSecondaryUseActive();
            DripstoneThickness thickness = calculateDripstoneThickness(level, pos, tipDirection, mergeOpposingTips);
            return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(TIP_DIRECTION, tipDirection)).setValue(THICKNESS, thickness)).setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER));
        }
    }

    private static boolean isPointedDripstoneWithDirection(BlockState blockState, Direction tipDirection) {
        return blockState.is(NRegistry.POINTED_BEDROCK_BLOCK.get()) && blockState.getValue(TIP_DIRECTION) == tipDirection;
    }

    @VisibleForTesting
    public static void maybeTransferFluid(BlockState state, ServerLevel level, BlockPos pos, float randomValue) {
        if (isStalactiteStartPos(state, level, pos)) {
            Optional<FluidInfo> fluidInfo = getFluidAboveStalactite(level, pos, state);
            if (!fluidInfo.isEmpty()) {
                Fluid fluid = ((FluidInfo)fluidInfo.get()).fluid;
                float transferProbability;
                if (fluid == Fluids.WATER) {
                    transferProbability = 0.17578125F;
                } else {
                    transferProbability = 0.05859375F;
                }

                FluidType.DripstoneDripInfo dripInfo = fluid.getFluidType().getDripInfo();
                if (dripInfo != null && !(randomValue >= dripInfo.chance())) {
                    BlockPos stalactiteTipPos = findTip(state, level, pos, 11, false);
                    if (stalactiteTipPos != null) {
                        if (((FluidInfo)fluidInfo.get()).sourceState.is(Blocks.MUD) && fluid == Fluids.WATER) {
                            BlockState newState = Blocks.CLAY.defaultBlockState();
                            level.setBlockAndUpdate(((FluidInfo)fluidInfo.get()).pos, newState);
                            Block.pushEntitiesUp(((FluidInfo)fluidInfo.get()).sourceState, newState, level, ((FluidInfo)fluidInfo.get()).pos);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, ((FluidInfo)fluidInfo.get()).pos, GameEvent.Context.of(newState));
                            level.levelEvent(1504, stalactiteTipPos, 0);
                        } else {
                            BlockPos cauldronPos = findFillableCauldronBelowStalactiteTip(level, stalactiteTipPos, fluid);
                            if (cauldronPos != null) {
                                level.levelEvent(1504, stalactiteTipPos, 0);
                                int fallDistance = stalactiteTipPos.getY() - cauldronPos.getY();
                                int delay = 50 + fallDistance;
                                BlockState cauldronState = level.getBlockState(cauldronPos);
                                level.scheduleTick(cauldronPos, cauldronState.getBlock(), delay);
                            }
                        }
                    }
                }
            }
        }

    }


    private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos fallPos = pos.mutable();

        for(BlockState fallState = state; isStalactite(fallState); fallState = level.getBlockState(fallPos)) {
            FallingBlockEntity entity = FallingBlockEntity.fall(level, fallPos, fallState);
            if (isTip(fallState, true)) {
                int size = Math.max(1 + pos.getY() - fallPos.getY(), 6);
                float damagePerFallDistance = 1.0F * (float)size;
                entity.setHurtsEntities(damagePerFallDistance, 40);
                break;
            }

            fallPos.move(Direction.DOWN);
        }

    }

    @VisibleForTesting
    public static void growStalactiteOrStalagmiteIfPossible(BlockState stalactiteStartState, ServerLevel level, BlockPos stalactiteStartPos, RandomSource random) {
        BlockState rootState = level.getBlockState(stalactiteStartPos.above(1));
        BlockState stateAbove = level.getBlockState(stalactiteStartPos.above(2));
        if (canGrow(rootState, stateAbove)) {
            BlockPos stalactiteTipPos = findTip(stalactiteStartState, level, stalactiteStartPos, 7, false);
            if (stalactiteTipPos != null) {
                BlockState stalactiteTipState = level.getBlockState(stalactiteTipPos);
                if (canDrip(stalactiteTipState) && canTipGrow(stalactiteTipState, level, stalactiteTipPos)) {
                    if (random.nextBoolean()) {
                        grow(level, stalactiteTipPos, Direction.DOWN);
                    } else {
                        growStalagmiteBelow(level, stalactiteTipPos);
                    }
                }
            }
        }

    }

    private static void growStalagmiteBelow(ServerLevel level, BlockPos posAboveStalagmite) {
        BlockPos.MutableBlockPos pos = posAboveStalagmite.mutable();

        for(int i = 0; i < 10; ++i) {
            pos.move(Direction.DOWN);
            BlockState state = level.getBlockState(pos);
            if (!state.getFluidState().isEmpty()) {
                return;
            }

            if (isUnmergedTipWithDirection(state, Direction.UP) && canTipGrow(state, level, pos)) {
                grow(level, pos, Direction.UP);
                return;
            }

            if (isValidPointedDripstonePlacement(level, pos, Direction.UP) && !level.isWaterAt(pos.below())) {
                grow(level, pos.below(), Direction.UP);
                return;
            }

            if (!canDripThrough(level, pos, state)) {
                return;
            }
        }

    }

    private static void grow(ServerLevel level, BlockPos growFromPos, Direction growToDirection) {
        BlockPos targetPos = growFromPos.relative(growToDirection);
        BlockState existingStateAtTargetPos = level.getBlockState(targetPos);
        if (isUnmergedTipWithDirection(existingStateAtTargetPos, growToDirection.getOpposite())) {
            createMergedTips(existingStateAtTargetPos, level, targetPos);
        } else if (existingStateAtTargetPos.isAir() || existingStateAtTargetPos.is(Blocks.WATER)) {
            createDripstone(level, targetPos, growToDirection, DripstoneThickness.TIP);
        }

    }

    private static void createDripstone(LevelAccessor level, BlockPos pos, Direction direction, DripstoneThickness thickness) {
        BlockState state = (BlockState)((BlockState)((BlockState)Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(TIP_DIRECTION, direction)).setValue(THICKNESS, thickness)).setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER));
        level.setBlock(pos, state, 3);
    }

    private static void createMergedTips(BlockState tipState, LevelAccessor level, BlockPos tipPos) {
        BlockPos stalactitePos;
        BlockPos stalagmitePos;
        if (tipState.getValue(TIP_DIRECTION) == Direction.UP) {
            stalagmitePos = tipPos;
            stalactitePos = tipPos.above();
        } else {
            stalactitePos = tipPos;
            stalagmitePos = tipPos.below();
        }

        createDripstone(level, stalactitePos, Direction.DOWN, DripstoneThickness.TIP_MERGE);
        createDripstone(level, stalagmitePos, Direction.UP, DripstoneThickness.TIP_MERGE);
    }


    private static @Nullable BlockPos findTip(BlockState dripstoneState, LevelAccessor level, BlockPos dripstonePos, int maxSearchLength, boolean includeMergedTip) {
        if (isTip(dripstoneState, includeMergedTip)) {
            return dripstonePos;
        } else {
            Direction searchDirection = (Direction)dripstoneState.getValue(TIP_DIRECTION);
            BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> {
                return state.is(NRegistry.POINTED_BEDROCK_BLOCK.get()) && state.getValue(TIP_DIRECTION) == searchDirection;
            };
            return (BlockPos)findBlockVertical(level, dripstonePos, searchDirection.getAxisDirection(), pathPredicate, (dripstone) -> {
                return isTip(dripstone, includeMergedTip);
            }, maxSearchLength).orElse((BlockPos) null);
        }
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

    private static DripstoneThickness calculateDripstoneThickness(LevelReader level, BlockPos pos, Direction tipDirection, boolean mergeOpposingTips) {
        Direction baseDirection = tipDirection.getOpposite();
        BlockState inFrontState = level.getBlockState(pos.relative(tipDirection));
        if (!isPointedDripstoneWithDirection(inFrontState, baseDirection)) {
            if (!isPointedDripstoneWithDirection(inFrontState, tipDirection)) {
                return DripstoneThickness.TIP;
            } else {
                DripstoneThickness inFrontThickness = (DripstoneThickness)inFrontState.getValue(THICKNESS);
                if (inFrontThickness != DripstoneThickness.TIP && inFrontThickness != DripstoneThickness.TIP_MERGE) {
                    BlockState behindState = level.getBlockState(pos.relative(baseDirection));
                    return !isPointedDripstoneWithDirection(behindState, tipDirection) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
                } else {
                    return DripstoneThickness.FRUSTUM;
                }
            }
        } else {
            return !mergeOpposingTips && inFrontState.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
        }
    }

    public static boolean canDrip(BlockState state) {
        return isStalactite(state) && state.getValue(THICKNESS) == DripstoneThickness.TIP && !(Boolean)state.getValue(WATERLOGGED);
    }

    private static boolean canTipGrow(BlockState tipState, ServerLevel level, BlockPos tipPos) {
        Direction growDirection = (Direction)tipState.getValue(TIP_DIRECTION);
        BlockPos growPos = tipPos.relative(growDirection);
        BlockState stateAtGrowPos = level.getBlockState(growPos);
        if (!stateAtGrowPos.getFluidState().isEmpty()) {
            return false;
        } else {
            return stateAtGrowPos.isAir() ? true : isUnmergedTipWithDirection(stateAtGrowPos, growDirection.getOpposite());
        }
    }

    private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, BlockState dripStoneState, int maxSearchLength) {
        Direction tipDirection = (Direction)dripStoneState.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> pathPredicate = (pathPos, state) -> {
            return state.is(Blocks.POINTED_DRIPSTONE) && state.getValue(TIP_DIRECTION) == tipDirection;
        };
        return findBlockVertical(level, pos, tipDirection.getOpposite().getAxisDirection(), pathPredicate, (state) -> {
            return !state.is(Blocks.POINTED_DRIPSTONE);
        }, maxSearchLength);
    }

    private static boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction tipDirection) {
        BlockPos behindPos = pos.relative(tipDirection.getOpposite());
        BlockState behindState = level.getBlockState(behindPos);
        return behindState.isFaceSturdy(level, behindPos, tipDirection) || isPointedDripstoneWithDirection(behindState, tipDirection);
    }

    private static boolean isTip(BlockState state, boolean includeMergedTip) {
        if (!state.is(Blocks.POINTED_DRIPSTONE)) {
            return false;
        } else {
            DripstoneThickness thickness = (DripstoneThickness)state.getValue(THICKNESS);
            return thickness == DripstoneThickness.TIP || includeMergedTip && thickness == DripstoneThickness.TIP_MERGE;
        }
    }

    private static boolean isUnmergedTipWithDirection(BlockState state, Direction tipDirection) {
        return isTip(state, false) && state.getValue(TIP_DIRECTION) == tipDirection;
    }

    private static boolean isStalactite(BlockState state) {
        return isPointedDripstoneWithDirection(state, Direction.DOWN);
    }

    private static boolean isStalagmite(BlockState state) {
        return isPointedDripstoneWithDirection(state, Direction.UP);
    }

    private static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) {
        return isStalactite(state) && !level.getBlockState(pos.above()).is(Blocks.POINTED_DRIPSTONE);
    }



    private static @Nullable BlockPos findFillableCauldronBelowStalactiteTip(Level level, BlockPos stalactiteTipPos, Fluid fluid) {
        return null;
    }

    public static @Nullable BlockPos findStalactiteTipAboveCauldron(Level level, BlockPos cauldronPos) {
        BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> {
            return canDripThrough(level, pos, state);
        };
        return (BlockPos)findBlockVertical(level, cauldronPos, Direction.UP.getAxisDirection(), pathPredicate, PointedDripstoneBlock::canDrip, 11).orElse((BlockPos) null);
    }

    public static Fluid getCauldronFillFluidType(ServerLevel level, BlockPos stalactitePos) {
        return (Fluid)getFluidAboveStalactite(level, stalactitePos, level.getBlockState(stalactitePos)).map((fluidSource) -> {
            return fluidSource.fluid;
        }).filter(PointedUpBlock::canFillCauldron).orElse(Fluids.EMPTY);
    }

    private static Optional<FluidInfo> getFluidAboveStalactite(Level level, BlockPos stalactitePos, BlockState stalactiteState) {
        return !isStalactite(stalactiteState) ? Optional.empty() : findRootBlock(level, stalactitePos, stalactiteState, 11).map((rootPos) -> {
            BlockPos abovePos = rootPos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            Object fluid;
            if (aboveState.is(Blocks.MUD) && !(Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, abovePos)) {
                fluid = Fluids.WATER;
            } else {
                fluid = level.getFluidState(abovePos).getType();
            }

            return new FluidInfo(abovePos, (Fluid)fluid, aboveState);
        });
    }

    private static boolean canFillCauldron(Fluid fluidAbove) {
        return fluidAbove.getFluidType().getDripInfo() != null;
    }

    private static boolean canGrow(BlockState rootState, BlockState aboveState) {
        FluidState fluidState = aboveState.getFluidState();
        return rootState.is(Blocks.DRIPSTONE_BLOCK) && fluidState.is(Fluids.WATER) && fluidState.isSource();
    }

    private static ParticleOptions getDripParticle(Level level, Fluid fluidAbove, BlockPos posAbove) {
        if (fluidAbove.isSame(Fluids.EMPTY)) {
            return (ParticleOptions)level.environmentAttributes().getValue(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, posAbove);
        } else {
            ParticleOptions options = fluidAbove.getFluidType().getDripInfo() != null ? fluidAbove.getFluidType().getDripInfo().dripParticle() : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
            if (options == null) {
                options = (ParticleOptions)level.environmentAttributes().getValue(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, posAbove);
            }

            return (ParticleOptions)options;
        }
    }

    private static Optional<BlockPos> findBlockVertical(LevelAccessor level, BlockPos pos, Direction.AxisDirection axisDirection, BiPredicate<BlockPos, BlockState> pathPredicate, Predicate<BlockState> targetPredicate, int maxSteps) {
        Direction direction = Direction.get(axisDirection, Direction.Axis.Y);
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for(int i = 1; i < maxSteps; ++i) {
            mutablePos.move(direction);
            BlockState state = level.getBlockState(mutablePos);
            if (targetPredicate.test(state)) {
                return Optional.of(mutablePos.immutable());
            }

            if (level.isOutsideBuildHeight(mutablePos.getY()) || !pathPredicate.test(mutablePos, state)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return true;
        } else if (state.isSolidRender()) {
            return false;
        } else if (!state.getFluidState().isEmpty()) {
            return false;
        } else {
            VoxelShape collisionShape = state.getCollisionShape(level, pos);
            return !Shapes.joinIsNotEmpty(Block.column(4.0, 0.0, 16.0), collisionShape, BooleanOp.AND);
        }
    }
    record FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
        FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
            this.pos = pos;
            this.fluid = fluid;
            this.sourceState = sourceState;
        }

        public BlockPos pos() {
            return this.pos;
        }

        public Fluid fluid() {
            return this.fluid;
        }

        public BlockState sourceState() {
            return this.sourceState;
        }
    }


}
