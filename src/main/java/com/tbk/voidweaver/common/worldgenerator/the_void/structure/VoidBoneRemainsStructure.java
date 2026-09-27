package com.tbk.voidweaver.common.worldgenerator.the_void.structure;

import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.common.worldgenerator.the_void.structure_piece.VoidBoneRemainsStructurePiece;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Random;

public class VoidBoneRemainsStructure extends Structure {

    public static final MapCodec<VoidBoneRemainsStructure> CODEC = simpleCodec(VoidBoneRemainsStructure::new);

    public VoidBoneRemainsStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {

        Vec3 island = getIslandForChunk(generationContext.chunkPos(), generationContext.seed());

        if (island == null) {
            return Optional.empty();
        }

        BlockPos pos = BlockPos.containing(island);


        return Optional.of(new GenerationStub(pos, builder -> {
            builder.addPiece(new VoidBoneRemainsStructurePiece(0,generationContext.structureTemplateManager(),new StructurePlaceSettings(), pos));
        }));
    }
    @Override
    public StructureType<?> type() {
        return NRegistry.BONE_REMAINS.get();
    }
    public Vec3 getIslandForChunk(ChunkPos chunkPos, long seed) {

        int cellSize = 256;

        int chunkCenterX = chunkPos.getMiddleBlockX();
        int chunkCenterZ = chunkPos.getMiddleBlockZ();

        int cellX = Math.floorDiv(chunkCenterX, cellSize);
        int cellZ = Math.floorDiv(chunkCenterZ, cellSize);

        long islandSeed = seed + cellX * 341873128712L + cellZ * 132897987541L;
        Random random = new Random(islandSeed);

        double centerX = cellX * cellSize + random.nextInt(cellSize);

        double centerZ = cellZ * cellSize + random.nextInt(cellSize);

        double centerY = 250;

        int islandChunkX = Math.floorDiv((int) centerX, 16);
        int islandChunkZ = Math.floorDiv((int) centerZ, 16);

        if (islandChunkX != chunkPos.x || islandChunkZ != chunkPos.z) {
            return null;
        }
        Random structureType = new Random(islandSeed ^ 0x5DEECE66DL);
        if (structureType.nextInt(0,4)!=1)return null;

        return new Vec3(centerX, centerY, centerZ);
    }
}
