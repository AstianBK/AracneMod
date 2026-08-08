package com.astianbk.aracnemod.common.worldgenerator;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.RandomState;

public class TerrainGenerator {
    public final IslandManager islandManager;

    public TerrainGenerator(long seed) {
        this.islandManager = new IslandManager(seed);
    }

    public void generate(RandomState randomState, ChunkAccess chunk) {

        islandManager.generate(chunk);
    }
}
