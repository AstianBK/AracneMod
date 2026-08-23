package com.astianbk.arachnemod.common.worldgenerator.the_depths;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.common.worldgenerator.the_void.TerrainGenerator;
import com.astianbk.arachnemod.common.worldgenerator.the_void.WorldNoise;
import com.astianbk.arachnemod.common.worldgenerator.the_void.density.BridgeSource;
import com.astianbk.arachnemod.common.worldgenerator.the_void.density.IslandSource;
import com.astianbk.arachnemod.common.worldgenerator.the_void.density.SpikesSource;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class TheDepthsChunkGenerator extends ChunkGenerator {
    public static final MapCodec<TheDepthsChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            BiomeSource.CODEC.fieldOf("biome_source")
                                    .forGetter(generator -> generator.biomeSource)
                    ).apply(instance, TheDepthsChunkGenerator::new));
    public TheDepthsChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);

    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long l, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess) {

    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {

    }



    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        long seed = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getSeed();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {

                pos.set(minX + localX, 0, minZ + localZ);
                chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState());

            }
        }
        generateTerrain(chunk,getNearbyIslands(chunk.getPos(),seed,new WorldNoise(seed)));
        return CompletableFuture.completedFuture(chunk);
    }

    public List<IslandSource> getNearbyIslands(ChunkPos chunkPos, long seed, WorldNoise noise) {

        List<IslandSource> islands = new ArrayList<>();

        int cellSize = 256;

        int chunkCenterX = chunkPos.getMiddleBlockX();
        int chunkCenterZ = chunkPos.getMiddleBlockZ();

        int cellX = Math.floorDiv(chunkCenterX, cellSize);
        int cellZ = Math.floorDiv(chunkCenterZ, cellSize);

        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int z = cellZ - 1; z <= cellZ + 1; z++) {
                Random random = new Random(seed + x * 341873128712L + z * 132897987541L);


                double centerX = x * cellSize + random.nextInt(cellSize);
                double centerZ = z * cellSize + random.nextInt(cellSize);

                double centerY = 250;

                double radius = 30 + random.nextInt(25);

                islands.add(new IslandSource(centerX, centerY, centerZ, radius,noise));
            }
        }
        return islands;
    }

    private void generateTerrain(ChunkAccess chunk, List<IslandSource> islands) {

        ChunkPos pos = chunk.getPos();

        int chunkMinX = pos.getMinBlockX();
        int chunkMaxX = chunkMinX + 15;

        int chunkMinZ = pos.getMinBlockZ();
        int chunkMaxZ = chunkMinZ + 15;

        int maxY = 250;

        for (IslandSource island : islands) {
            double r = island.radius;

            int minX = (int)Math.max(chunkMinX, Math.floor(island.centerX - r));
            int maxX = (int)Math.min(chunkMaxX, Math.ceil(island.centerX + r));

            int minZ = (int)Math.max(chunkMinZ, Math.floor(island.centerZ - r));
            int maxZ = (int)Math.min(chunkMaxZ, Math.ceil(island.centerZ + r));

            int y0 = 0;
            int y1 = (int)Math.min(maxY, island.centerY + 40);

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int y = y0; y <= y1; y++) {
                        if (island.sample(x, y, z) > 0) {
                            chunk.setBlockState(new BlockPos(x, y, z), NRegistry.STONE_BEDROCK_BLOCK.get().defaultBlockState());
                        }
                    }
                }
            }
        }


    }

    @Override
    public int getGenDepth() {
        return 400;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        return 252;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {

        int minY = getMinY();
        int height = getGenDepth();

        BlockState[] states = new BlockState[height];

        Arrays.fill(states, Blocks.AIR.defaultBlockState());

        return new NoiseColumn(minY, states);
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {

    }
}
