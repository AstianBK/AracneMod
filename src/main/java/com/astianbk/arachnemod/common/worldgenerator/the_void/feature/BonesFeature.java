package com.astianbk.arachnemod.common.worldgenerator.the_void.feature;

import com.astianbk.arachnemod.common.worldgenerator.the_void.feature_configuration.VoidCrystalFeatureConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

public class BonesFeature extends Feature<VoidCrystalFeatureConfiguration> {

    public BonesFeature(Codec<VoidCrystalFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VoidCrystalFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        if (random.nextFloat() > 0.15F)return false;
        BlockPos origin = context.origin();

        BlockPos pos = findPlacementPosition(level, origin, random);
        if (pos == null) {
            return false;
        }

        if (!canPlace(level, pos)) {
            return false;
        }
        if (random.nextFloat() > 0.5F){
            Direction.Axis axis = random.nextFloat()<0.5F? Direction.Axis.X:Direction.Axis.Z;
            BlockState crystal = Blocks.BONE_BLOCK.defaultBlockState();
            Vec3 offset = axis.getPositive().getUnitVec3();
            level.setBlock(pos, crystal.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X), 3);

            for (int i = 0 ; i < random.nextInt(3,5); i++){
                level.setBlock(pos.offset((int) (offset.x*i),0, (int) (offset.z *i)), crystal.setValue(RotatedPillarBlock.AXIS, axis), 3);
            }

        }else {

            BlockState crystal = Blocks.BONE_BLOCK.defaultBlockState();
            level.setBlock(pos,crystal,3);
            for (int i = 0 ; i < random.nextInt(3,5);i++){
                level.setBlock(pos.above(),crystal,3);
            }
        }

        return true;
    }

    private BlockPos findPlacementPosition(WorldGenLevel level, BlockPos origin, RandomSource random) {
        int x = origin.getX();
        int z = origin.getZ();

        int minY = 0;
        int maxY = 250;

        for (int y = maxY; y >= minY; y--) {

            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);

            if (!state.isAir()) {
                return pos.above();
            }
        }

        return null;
    }

    private boolean canPlace(WorldGenLevel level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());

        return level.isEmptyBlock(pos) && below.isFaceSturdy(level, pos.below(), Direction.UP);
    }
}
