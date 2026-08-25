package com.astianbk.arachnemod.common.worldgenerator.the_void.structure_piece;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Iterator;

public class VoidZiguratStructurePiece extends TemplateStructurePiece {

    public int type = 0;
    public VoidZiguratStructurePiece( int genDepth,int type, StructureTemplateManager structureTemplateManager, StructurePlaceSettings placeSettings, BlockPos position) {
        super(NRegistry.VOID_ZIGURAT_PIECE.get(), genDepth, structureTemplateManager, Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_zigurat_"+type), "arachnemod:void_zigurat_"+type, placeSettings, position);
        this.templatePosition = position.offset(-this.template.getSize().getX() / 2, 1, -this.template.getSize().getZ() / 2);
        this.boundingBox = this.template.getBoundingBox(new StructurePlaceSettings(), this.templatePosition);
    }

    public VoidZiguratStructurePiece(CompoundTag tag, StructureTemplateManager structureTemplateManager) {
        super(NRegistry.VOID_ZIGURAT_PIECE.get(),tag,structureTemplateManager,location -> new StructurePlaceSettings());
    }

    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {

    }
}
