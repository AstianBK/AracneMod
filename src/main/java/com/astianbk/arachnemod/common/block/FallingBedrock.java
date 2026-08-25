package com.astianbk.arachnemod.common.block;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.common.worldgenerator.the_void.BedrockUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FallingBedrock extends FallingBlock {
    public static final MapCodec<FallingBedrock> CODEC = simpleCodec(FallingBedrock::new);

    public FallingBedrock(Properties properties) {
        super(properties);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {
        BlockState belowState = level.getBlockState(pos.below());
        level.setBlock(pos,Blocks.AIR.defaultBlockState(),3);
        if (belowState.getBlock() instanceof PointedUpBlock){
            PointedUpBlock.grow((ServerLevel) level,pos.below(), Direction.UP);
        }else if (belowState.is(NRegistry.LARGE_VEIL_CRYSTAL_BLOCK) || belowState.is(NRegistry.VEIL_CRYSTAL_BLOCK) || belowState.is(NRegistry.TALL_VEIL_CRYSTAL_BLOCK)){
            level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(),3);
        }else {
            level.setBlock(pos,NRegistry.POINTED_BEDROCK_BLOCK.get().defaultBlockState().setValue(PointedUpBlock.TIP_DIRECTION,Direction.UP),3);
        }
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        entity.setHurtsEntities(2.0F, 40);
    }
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }
    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 0;
    }
}
