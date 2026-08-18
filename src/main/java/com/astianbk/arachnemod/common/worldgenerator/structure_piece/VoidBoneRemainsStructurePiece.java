package com.astianbk.arachnemod.common.worldgenerator.structure_piece;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class VoidBoneRemainsStructurePiece extends TemplateStructurePiece {


    public VoidBoneRemainsStructurePiece(int genDepth, StructureTemplateManager structureTemplateManager, StructurePlaceSettings placeSettings, BlockPos position) {
        super(NRegistry.VOID_BONE_REMAINS_PIECE.get(), genDepth, structureTemplateManager, Identifier.fromNamespaceAndPath(AracneMod.MODID,"bone_remains"), "arachnemod:bone_remains", placeSettings, position);
        this.templatePosition = position.offset(-this.template.getSize().getX() / 4, 1, -this.template.getSize().getZ() / 4);
        this.boundingBox = this.template.getBoundingBox(new StructurePlaceSettings(), this.templatePosition);
    }

    public VoidBoneRemainsStructurePiece(CompoundTag tag, StructureTemplateManager structureTemplateManager) {
        super(NRegistry.VOID_BONE_REMAINS_PIECE.get(),tag,structureTemplateManager,location -> new StructurePlaceSettings());
    }
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
        AracneMod.LOGGER.info(
                "ZIGURAT: pieceBB={} chunkBB={} templatePos={} referencePos={} chunk={}",
                this.boundingBox,
                chunkBB,
                this.templatePosition,
                referencePos,
                chunkPos
        );
        AracneMod.LOGGER.info(
                "TEMPLATE: size={}x{}x{}",
                this.template.getSize().getX(),
                this.template.getSize().getY(),
                this.template.getSize().getZ()
        );
        this.placeSettings.setBoundingBox(chunkBB);
        this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);

        AracneMod.LOGGER.info(
                "PLACE: chunk={} chunkBB={} pieceBB={} templatePos={}",
                chunkPos,
                chunkBB,
                this.boundingBox,
                this.templatePosition
        );
        if (this.template.placeInWorld(level, this.templatePosition, referencePos, this.placeSettings, random, 2)) {

        }

    }
    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {

    }
}
