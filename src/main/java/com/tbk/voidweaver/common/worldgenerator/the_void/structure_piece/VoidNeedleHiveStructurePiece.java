package com.tbk.voidweaver.common.worldgenerator.the_void.structure_piece;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.registry.NRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

public class VoidNeedleHiveStructurePiece extends TemplateStructurePiece {


    public VoidNeedleHiveStructurePiece(int genDepth, StructureTemplateManager structureTemplateManager, StructurePlaceSettings placeSettings, BlockPos position) {
        super(NRegistry.VOID_NEEDLE_HIVE_PIECE.get(), genDepth, structureTemplateManager, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"needle_hive"), "voidweaver:needle_hive", placeSettings, position);
        this.templatePosition = position.offset(-this.template.getSize().getX() / 4, 1, -this.template.getSize().getZ() / 4);

        this.boundingBox = this.template.getBoundingBox(new StructurePlaceSettings(), this.templatePosition);
    }

    public VoidNeedleHiveStructurePiece(CompoundTag tag, StructureTemplateManager structureTemplateManager) {
        super(NRegistry.VOID_NEEDLE_HIVE_PIECE.get(),tag,structureTemplateManager,location -> new StructurePlaceSettings());
    }

    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {

    }
}
