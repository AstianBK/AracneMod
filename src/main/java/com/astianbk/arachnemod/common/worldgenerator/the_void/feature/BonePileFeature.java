package com.astianbk.arachnemod.common.worldgenerator.the_void.feature;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.common.worldgenerator.the_void.feature_configuration.BonePileFeatureConfiguration;
import com.astianbk.arachnemod.common.worldgenerator.the_void.feature_configuration.VoidCrystalFeatureConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

public class BonePileFeature extends Feature<BonePileFeatureConfiguration> {

    // Probabilidad de seguir colocando una vez alcanzado minCount
    private static final float CONTINUE_CHANCE = 0.65F;

    public BonePileFeature(Codec<BonePileFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BonePileFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BonePileFeatureConfiguration config = context.config();

        BlockPos start = findPlacementPosition(level, context.origin());
        if (start == null || !canPlace(level, start)) {
            return false;
        }

        // Primer hueso con eje aleatorio
        Direction.Axis firstAxis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        setBone(level, start, firstAxis);

        int placed = 1;
        int failedAttempts = 0;
        BlockPos current = start;

        while (placed < config.maxCount() && failedAttempts < config.maxAttempts()) {

            // Pasado el mínimo, se decide por probabilidad si se sigue
            if (placed >= config.minCount() && random.nextFloat() > CONTINUE_CHANCE) {
                break;
            }

            // Nueva dirección en cada paso: solo horizontales, nunca arriba ni abajo
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            BlockPos next = findNext(level, current, dir);

            if (next == null) {
                failedAttempts++;
                continue;
            }

            // El hueso queda orientado en el sentido en que avanza el patrón
            setBone(level, next, dir.getAxis());
            current = next;
            placed++;
        }

        return placed >= config.minCount();
    }

    /** Busca el bloque vecino en la dirección dada, siguiendo el relieve (mismo Y, -1 o +1). */
    private BlockPos findNext(WorldGenLevel level, BlockPos from, Direction dir) {
        BlockPos side = from.relative(dir);
        for (int dy : new int[]{0, -1, 1}) {
            BlockPos candidate = side.offset(0, dy, 0);
            if (canPlace(level, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private void setBone(WorldGenLevel level, BlockPos pos, Direction.Axis axis) {
        BlockState bone = NRegistry.BONE_PILE_BLOCK.get().defaultBlockState();
        level.setBlock(pos, bone, 3);
    }

    private BlockPos findPlacementPosition(WorldGenLevel level, BlockPos origin) {
        int x = origin.getX();
        int z = origin.getZ();

        for (int y = 250; y >= 0; y--) {
            BlockPos pos = new BlockPos(x, y, z);
            if (!level.getBlockState(pos).isAir()) {
                return pos.above();
            }
        }
        return null;
    }

    private boolean canPlace(WorldGenLevel level, BlockPos pos) {
        if (!level.ensureCanWrite(pos)) {
            return false; // fuera de los chunks que se pueden escribir
        }
        BlockState below = level.getBlockState(pos.below());
        return level.isEmptyBlock(pos) && below.isFaceSturdy(level, pos.below(), Direction.UP);
    }
}