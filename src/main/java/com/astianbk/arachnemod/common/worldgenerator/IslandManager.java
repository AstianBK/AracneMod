package com.astianbk.arachnemod.common.worldgenerator;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.common.worldgenerator.density.IslandSource;
import com.astianbk.arachnemod.common.worldgenerator.density.BridgeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class IslandManager {
    private final WorldNoise noise;
    private long seed;
    public IslandManager(long seed) {
        this.seed = seed;
        this.noise = new WorldNoise(seed);
    }

    public void generate(ChunkAccess chunk) {

        List<IslandSource> islands = getNearbyIslands(chunk.getPos());

        generateTerrain(chunk, islands);

        generateRoof(chunk);
    }
    public List<IslandSource> getNearbyIslands(ChunkPos chunkPos) {

        List<IslandSource> islands = new ArrayList<>();

        int cellSize = 256;

        int chunkCenterX = chunkPos.getMiddleBlockX();
        int chunkCenterZ = chunkPos.getMiddleBlockZ();

        int cellX = Math.floorDiv(chunkCenterX, cellSize);
        int cellZ = Math.floorDiv(chunkCenterZ, cellSize);

        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int z = cellZ - 1; z <= cellZ + 1; z++) {
                Random random = new Random(seed*x+seed*z);

                double centerX = x * cellSize + random.nextInt(cellSize);
                double centerZ = z * cellSize + random.nextInt(cellSize);

                double centerY = 200 + random.nextFloat() * 50;

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


        List<BridgeSource> bridges = createBridges(islands);

        for (BridgeSource bridge : bridges) {
            generateBridge(chunk, bridge);
        }
    }

    private void generateRoof(ChunkAccess chunk) {

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {

                pos.set(minX + localX, 399, minZ + localZ);
                chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState());

            }
        }
    }
    private void generateBridge(ChunkAccess chunk, BridgeSource bridge) {

        ChunkPos pos = chunk.getPos();

        int chunkMinX = pos.getMinBlockX();
        int chunkMaxX = chunkMinX + 15;

        int chunkMinZ = pos.getMinBlockZ();
        int chunkMaxZ = chunkMinZ + 15;

        double r = bridge.radius + 4;

        double minBX = Math.min(bridge.from.x, bridge.to.x) - r;
        double maxBX = Math.max(bridge.from.x, bridge.to.x) + r;

        double minBZ = Math.min(bridge.from.z, bridge.to.z) - r;
        double maxBZ = Math.max(bridge.from.z, bridge.to.z) + r;

        double minBY = Math.min(bridge.from.y, bridge.to.y) - r;
        double maxBY = Math.max(bridge.from.y, bridge.to.y) + r;

        int minX = (int)Math.max(chunkMinX, Math.floor(minBX));
        int maxX = (int)Math.min(chunkMaxX, Math.ceil(maxBX));

        int minZ = (int)Math.max(chunkMinZ, Math.floor(minBZ));
        int maxZ = (int)Math.min(chunkMaxZ, Math.ceil(maxBZ));

        int minY = (int)Math.max(chunk.getMinY(), Math.floor(minBY));
        int maxY = (int)Math.min(250, Math.ceil(maxBY));

        for (int x = minX; x <= maxX; x++) {

            for (int z = minZ; z <= maxZ; z++) {

                for (int y = minY; y <= maxY; y++) {

                    if (bridge.sample(x, y, z) > 0) {
                        chunk.setBlockState(new BlockPos(x, y, z), NRegistry.STONE_BEDROCK_BLOCK.get().defaultBlockState());
                    }
                }
            }
        }
    }
    private List<BridgeSource> createBridges(List<IslandSource> islands) {

        List<BridgeSource> bridges = new ArrayList<>();
        Set<Long> connected = new HashSet<>();

        int maxConnections = 20;
        double max = 300.0;

        for (int i = 0; i < islands.size(); i++) {

            IslandSource island = islands.get(i);

            List<IslandDistance> nearest = new ArrayList<>();

            for (int j = 0; j < islands.size(); j++) {

                if (i == j)
                    continue;

                IslandSource other = islands.get(j);

                double distance = distance(island, other);

                if (distance <= max) {
                    nearest.add(new IslandDistance(other, distance));
                }
            }

            nearest.sort(Comparator.comparingDouble(IslandDistance::distance));

            int connections = Math.min(maxConnections, nearest.size());
            int bridgedY = 240;
            for (int k = 0; k < connections; k++) {

                IslandSource other = nearest.get(k).island();

                long key = connectionKey(island, other);

                if (connected.add(key)) {
                    bridges.add(new BridgeSource(new Vec3(island.centerX,bridgedY,island.centerZ), new Vec3(other.centerX,bridgedY,other.centerZ), 10, noise));
                    bridgedY = Math.max(0,bridgedY - 50);
                }
            }
        }

        return bridges;
    }
    private double distance(IslandSource a, IslandSource b) {

        double dx = a.centerX - b.centerX;
        double dy = a.centerY - b.centerY;
        double dz = a.centerZ - b.centerZ;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private long connectionKey(IslandSource a, IslandSource b) {

        int ha = System.identityHashCode(a);
        int hb = System.identityHashCode(b);

        if (ha > hb) {
            int tmp = ha;
            ha = hb;
            hb = tmp;
        }

        return (((long) ha) << 32) | (hb & 0xffffffffL);
    }

    private record IslandDistance(IslandSource island, double distance) {}
}
