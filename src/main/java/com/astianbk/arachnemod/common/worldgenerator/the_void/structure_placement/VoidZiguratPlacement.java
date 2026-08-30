package com.astianbk.arachnemod.common.worldgenerator.the_void.structure_placement;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;
import java.util.Random;

public class VoidZiguratPlacement extends StructurePlacement {

    public static final MapCodec<VoidZiguratPlacement> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    placementCodec(instance)
                            .and(
                                    Codec.INT.fieldOf("cell_size")
                                            .forGetter(p -> p.cellSize)
                            )
                            .apply(instance, (vec3i, frequencyReductionMethod, aFloat, integer, exclusionZone, integer2) -> new VoidZiguratPlacement(vec3i,frequencyReductionMethod,aFloat,exclusionZone,integer2)));

    public final int cellSize;

    public VoidZiguratPlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, Optional<ExclusionZone> exclusionZone, int cellSize) {
        super(locateOffset, frequencyReductionMethod, frequency,123456789, exclusionZone);
        this.cellSize = cellSize;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int chunkX, int chunkZ) {
        int blockX = chunkX * 16 + 8;
        int blockZ = chunkZ * 16 + 8;

        int cellX = Math.floorDiv(blockX, cellSize);
        int cellZ = Math.floorDiv(blockZ, cellSize);

        long cellSeed = structureState.getLevelSeed() + cellX * 341873128712L + cellZ * 132897987541L;

        Random random = new Random(cellSeed);

        int islandX = cellX * cellSize + random.nextInt(cellSize);

        int islandZ = cellZ * cellSize + random.nextInt(cellSize);

        int islandChunkX = Math.floorDiv(islandX, 16);
        int islandChunkZ = Math.floorDiv(islandZ, 16);

        return islandChunkX == chunkX && islandChunkZ == chunkZ;
    }

    @Override
    public StructurePlacementType<?> type() {
        return NRegistry.VOID_PLACEMENT.get();
    }
}