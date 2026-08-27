package com.astianbk.arachnemod.common.worldgenerator.the_void.feature;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.block.LargeTallVeilCrystalBlock;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.common.worldgenerator.the_void.feature_configuration.VoidCrystalFeatureConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class VoidCrystalFeature extends Feature<VoidCrystalFeatureConfiguration> {
    public VoidCrystalFeature(Codec<VoidCrystalFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VoidCrystalFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        BlockPos pos = findPlacementPosition(level, origin, random);
        if (pos == null) {
            return false;
        }
        if (random.nextFloat()>0.15F)return false;

        if (!canPlace(level, pos)) {
            return false;
        }

        if (random.nextFloat() < 0.6F){
            BlockState crystal = NRegistry.VEIL_CRYSTAL_BLOCK.get().defaultBlockState();
            level.setBlock(pos,crystal,3);
        }if (random.nextFloat() < 0.2F){
            BlockState crystal = NRegistry.LARGE_VEIL_CRYSTAL_BLOCK.get().defaultBlockState();
            level.setBlock(pos, crystal.setValue(LargeTallVeilCrystalBlock.THICKNESS, SpeleothemThickness.BASE),3);

            level.setBlock(pos.above(), crystal.setValue(LargeTallVeilCrystalBlock.THICKNESS, SpeleothemThickness.MIDDLE), 3);

            level.setBlock(pos.above(2), crystal.setValue(LargeTallVeilCrystalBlock.THICKNESS, SpeleothemThickness.TIP),3);
        }else {
            BlockState crystal = NRegistry.TALL_VEIL_CRYSTAL_BLOCK.get().defaultBlockState();

            level.setBlock(pos, crystal.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 3);
            level.setBlock(pos.above(), crystal.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
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
