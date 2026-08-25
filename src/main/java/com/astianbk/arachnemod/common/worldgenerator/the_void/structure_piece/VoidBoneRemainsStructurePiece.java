package com.astianbk.arachnemod.common.worldgenerator.the_void.structure_piece;

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

    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {

    }
}
