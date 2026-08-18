package com.astianbk.arachnemod.common.registry;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.ArachneIdolBlockEntity;
import com.astianbk.arachnemod.common.block.GenerateFogBlock;
import com.astianbk.arachnemod.common.block.PointedUpBlock;
import com.astianbk.arachnemod.common.block.ArachneIdolBlock;
import com.astianbk.arachnemod.common.block.TallVeilCrystalBlock;
import com.astianbk.arachnemod.common.effect.DamnationHexEffect;
import com.astianbk.arachnemod.common.effect.SilentHexEffect;
import com.astianbk.arachnemod.common.effect.SilentEffect;
import com.astianbk.arachnemod.common.items.VoidKnightArmorItem;
import com.astianbk.arachnemod.common.items.VoidMaterial;
import com.astianbk.arachnemod.common.worldgenerator.VoidChunkGenerator;
import com.astianbk.arachnemod.common.worldgenerator.feature.BonesFeature;
import com.astianbk.arachnemod.common.worldgenerator.feature.PoitedBedrockFeature;
import com.astianbk.arachnemod.common.worldgenerator.feature.VoidCrystalFeature;
import com.astianbk.arachnemod.common.worldgenerator.feature_configuration.VoidCrystalFeatureConfiguration;
import com.astianbk.arachnemod.common.worldgenerator.structure.VoidBoneRemainsStructure;
import com.astianbk.arachnemod.common.worldgenerator.structure.VoidNeedleHiveStructure;
import com.astianbk.arachnemod.common.worldgenerator.structure.VoidZiguratStructure;
import com.astianbk.arachnemod.common.worldgenerator.structure_piece.VoidBoneRemainsStructurePiece;
import com.astianbk.arachnemod.common.worldgenerator.structure_piece.VoidNeedleHiveStructurePiece;
import com.astianbk.arachnemod.common.worldgenerator.structure_piece.VoidZiguratStructurePiece;
import com.astianbk.arachnemod.common.worldgenerator.structure_placement.VoidZiguratPlacement;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.entity.*;
import com.astianbk.arachnemod.server.cap.TheVoidAttachment;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.*;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public class NRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,AracneMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AracneMod.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AracneMod.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AracneMod.MODID);
    public static final DeferredRegister<Feature<?>> FEATURE = DeferredRegister.create(Registries.FEATURE,AracneMod.MODID);
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE,AracneMod.MODID);
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPE = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT,AracneMod.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT,AracneMod.MODID);
    public static final DeferredHolder<SoundEvent, SoundEvent> VOID_AMBIENCE =
            SOUNDS.register("ambience_loop", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(AracneMod.MODID, "ambience_loop")));
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENCE_0 =
            SOUNDS.register("ambience_0", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(AracneMod.MODID, "ambience_0")));
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENCE_1 =
            SOUNDS.register("ambience_1", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(AracneMod.MODID, "ambience_1")));
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENCE_2 =
            SOUNDS.register("ambience_2", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(AracneMod.MODID, "ambience_2")));
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENCE_3 =
            SOUNDS.register("ambience_3", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(AracneMod.MODID, "ambience_3")));

    public static final Supplier<AttachmentType<TheVoidAttachment>> THE_VOID_ATTACHMENT =
            ATTACHMENTS.register(
                    "the_void_attachment",
                    () -> AttachmentType.builder(TheVoidAttachment::new).serialize(new TheVoidAttachment.TheVoidSerializer()).copyOnDeath().build());

    public static final Supplier<AttachmentType<ArachneAttachment>> ARACNE =
            ATTACHMENTS.register(
                    "aracne",
                    () -> AttachmentType.builder(ArachneAttachment::new).serialize(new ArachneAttachment.NerubianCapSerializer()).sync(ArachneAttachment.NerubianCapSerializer.STREAM_CODEC).copyOnDeath().build());
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AracneMod.MODID);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AracneMod.MODID);
    public static final DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE,AracneMod.MODID);
    public static final DeferredHolder<MobEffect, SilentEffect> SILENT = EFFECTS.register("silent",SilentEffect::new);
    public static final DeferredHolder<MobEffect, SilentHexEffect> SILENT_HEX = EFFECTS.register("silent_hex", SilentHexEffect::new);
    public static final DeferredHolder<MobEffect, DamnationHexEffect> DAMNATION_HEX = EFFECTS.register("damnation_hex", DamnationHexEffect::new);

    public static final DeferredBlock<Block> WEAVER_IDOL_BLOCK = BLOCKS.registerBlock("arachne_idol", (ArachneIdolBlock::new));
    public static final DeferredBlock<Block> BEDCRUST_BLOCK = BLOCKS.registerBlock("bedcrust", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> BEDSLAG_BLOCK = BLOCKS.registerBlock("bedslag", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> BEDSTONE_BLOCK = BLOCKS.registerBlock("bedstone", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> CHISELED_BEDROCK_BLOCK = BLOCKS.registerBlock("chiseled_bedrock", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> COBBLED_BEDROCK_BLOCK = BLOCKS.registerBlock("cobbled_bedrock", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> BRICKED_BEDROCK_BLOCK = BLOCKS.registerBlock("bricked_bedrock", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> STONE_BEDROCK_BLOCK = BLOCKS.registerBlock("stone_bedrock", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> SLATED_BEDROCK_BLOCK = BLOCKS.registerBlock("slated_bedrock", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> CRACKED_BEDROCK_BLOCK = BLOCKS.registerBlock("cracked_bedrock", properties -> new Block(properties.strength(-1.0F, 3600000.0F)));
    public static final DeferredBlock<Block> VOID_WEB_BLOCK = BLOCKS.registerBlock("void_web", (properties -> new WebBlock(properties.noOcclusion().noCollision().strength(-1.0F, 3600000.0F))));

    public static final DeferredBlock<Block> BEDROCK_TRANSPARENT_BLOCK = BLOCKS.registerBlock("bedrock_transparent", (properties)->new TransparentBlock(properties.strength(-1.0F, 3600000.0F).noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor((s,e,s1)->false).isSuffocating((s,e,s1)->false).isViewBlocking((s,e,s1)->false)));

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ArachneIdolBlockEntity>> ARACHNE_IDOL_BLOCK_ENTITY = BLOCK_ENTITY_TYPE.register("arachne_idol_block_entity", () -> new BlockEntityType<>(ArachneIdolBlockEntity::new, Set.of(WEAVER_IDOL_BLOCK.get())));
    public static final DeferredBlock<Block> POINTED_BEDROCK_BLOCK = BLOCKS.registerBlock("pointed_bedrock", (properties)->new PointedUpBlock(properties
            .mapColor(MapColor.TERRACOTTA_BROWN)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM)
            .noOcclusion()
            .sound(SoundType.POINTED_DRIPSTONE)
            .randomTicks()
            .strength(1.5F, 3.0F)
            .dynamicShape()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion()));

    public static final DeferredBlock<Block> VEIL_CRYSTAL_BLOCK = BLOCKS.registerBlock("veil_crystal",(p)->new Block(p.lightLevel(statex -> 3).noOcclusion()));

    public static final DeferredBlock<Block> TALL_VEIL_CRYSTAL_BLOCK = BLOCKS.registerBlock("tall_veil_crystal",(p)->new TallVeilCrystalBlock(p.lightLevel(statex -> 9).noOcclusion()));
    public static final DeferredBlock<Block> COCOONCHEST_BLOCK = BLOCKS.registerBlock("cocoonchest",(p)->new Block(p.noOcclusion()));
    public static final DeferredItem<BlockItem> WEAVER_IDOL_ITEM = ITEMS.registerSimpleBlockItem("arachne_idol_item",WEAVER_IDOL_BLOCK);
    public static final DeferredItem<BlockItem> BEDCRUST_ITEM = ITEMS.registerSimpleBlockItem("bedcrust_item",BEDCRUST_BLOCK);
    public static final DeferredItem<BlockItem> BEDSLAG_ITEM = ITEMS.registerSimpleBlockItem("bedslag_item",BEDSLAG_BLOCK);
    public static final DeferredItem<BlockItem> BEDSTONE_ITEM = ITEMS.registerSimpleBlockItem("bedstone_item",BEDSTONE_BLOCK);
    public static final DeferredItem<BlockItem> CRACKED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("cracked_bedrock_item",CRACKED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> CHISELED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("chiseled_bedrock_item",CHISELED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> COBBLED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("cobbled_bedrock_item",COBBLED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> BRICKED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("bricked_bedrock_item",BRICKED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> STONE_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("stone_bedrock_item",STONE_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> SLATED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("slated_bedrock_item",SLATED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> VEIL_CRYSTAL_ITEM = ITEMS.registerSimpleBlockItem("veil_crystal_item",VEIL_CRYSTAL_BLOCK);
    public static final DeferredItem<BlockItem> TALL_VEIL_CRYSTAL_ITEM = ITEMS.registerSimpleBlockItem("tall_veil_crystal_item",TALL_VEIL_CRYSTAL_BLOCK);
    public static final DeferredItem<BlockItem> VOID_WEB_ITEM = ITEMS.registerSimpleBlockItem("void_web_item",VOID_WEB_BLOCK);

    public static final DeferredItem<BlockItem> COCOONCHEST_ITEM = ITEMS.registerSimpleBlockItem("cocoonchest_item",COCOONCHEST_BLOCK);

    public static final DeferredItem<BlockItem> POINTED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("pointed_bedrock_item",POINTED_BEDROCK_BLOCK);

    public static final DeferredItem<Item> VOID_HELMET = ITEMS.registerItem("void_helmet",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.HELMET).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_helmet" )))));

    public static final DeferredItem<Item> VOID_CHESTPLATE = ITEMS.registerItem("void_chestplate",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.CHESTPLATE).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_chestplate" )))));
    public static final DeferredItem<Item> VOID_LEGGINGS = ITEMS.registerItem("void_leggings",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.LEGGINGS).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_leggings" )))));
    public static final DeferredItem<Item> VOID_BOOTS = ITEMS.registerItem("void_boots",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.BOOTS).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_boots" )))));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arachnemod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> WEAVER_IDOL_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WEAVER_IDOL_ITEM.get());
                output.accept(BEDCRUST_ITEM.get());
                output.accept(BEDSLAG_ITEM.get());
                output.accept(BEDSTONE_ITEM.get());
                output.accept(CRACKED_BEDROCK_ITEM.get());
                output.accept(POINTED_BEDROCK_ITEM.get());
                output.accept(STONE_BEDROCK_ITEM.get());
                output.accept(CHISELED_BEDROCK_ITEM.get());
                output.accept(BRICKED_BEDROCK_ITEM.get());
                output.accept(SLATED_BEDROCK_ITEM.get());
                output.accept(COBBLED_BEDROCK_ITEM.get());
                output.accept(VEIL_CRYSTAL_ITEM.get());
                output.accept(COCOONCHEST_ITEM.get());
                output.accept(TALL_VEIL_CRYSTAL_ITEM.get());
            }).build());
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(AracneMod.MODID);
    public static final ResourceKey<Level> THE_VOID = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(AracneMod.MODID,"void"));
    public static final DeferredHolder<EntityType<?>, EntityType<OrbEntity>> ORB =
            ENTITY_TYPES.register("orb",
                    () -> EntityType.Builder
                            .of(OrbEntity::new, MobCategory.MISC)
                            .sized(2.0F, 2.0F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"orb"))));

    public static final DeferredHolder<EntityType<?>, EntityType<WebElevatorEntity>> WEB_ELEVATOR =
            ENTITY_TYPES.register("web_elevator",
                    () -> EntityType.Builder
                            .of(WebElevatorEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 2.0F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"web_elevator"))));
    public static final DeferredHolder<EntityType<?>, EntityType<EnterDimensionEntity>> ENTER_DIMENSION =
            ENTITY_TYPES.register("enter_dimension",
                    () -> EntityType.Builder
                            .of(EnterDimensionEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 0.2F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"enter_dimension"))));

    public static final DeferredHolder<EntityType<?>, EntityType<ScarabEntity>> SCARAB =
            ENTITY_TYPES.register("scarab",
                    () -> EntityType.Builder
                            .of(ScarabEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 2.0F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"scarab"))));

    public static final DeferredHolder<EntityType<?>, EntityType<VoidNeedleEntity>> VOID_NEEDLE =
            ENTITY_TYPES.register("void_needle",
                    () -> EntityType.Builder
                            .of(VoidNeedleEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 2.0F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_needle"))));

    public static final DeferredHolder<EntityType<?>, EntityType<VoidHopperEntity>> VOID_HOPPER =
            ENTITY_TYPES.register("void_hopper",
                    () -> EntityType.Builder.of(VoidHopperEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 2.0F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_hopper"))));
    public static final DeferredHolder<EntityType<?>, EntityType<VoidBeetleEntity>> VOID_BEETLE =
            ENTITY_TYPES.register("void_beetle",
                    () -> EntityType.Builder.of(VoidBeetleEntity::new, MobCategory.MONSTER)
                            .sized(0.25F, 0.5F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_beetle"))));
    public static final DeferredHolder<EntityType<?>, EntityType<VoidVeilmothEntity>> VOID_VEILMOTH =
            ENTITY_TYPES.register("void_veilmoth",
                    () -> EntityType.Builder.of(VoidVeilmothEntity::new, MobCategory.MONSTER)
                            .sized(0.25F, 0.5F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_veilmoth"))));

    public static final DeferredHolder<StructurePieceType, StructurePieceType> VOID_ZIGURAT_PIECE =
            PIECES.register("void_zigurat", () -> (context, tag) -> new VoidZiguratStructurePiece(tag, context.structureTemplateManager()));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> VOID_NEEDLE_HIVE_PIECE =
            PIECES.register("void_needle_hive", () -> (context, tag) -> new VoidNeedleHiveStructurePiece(tag, context.structureTemplateManager()));
    public static final DeferredHolder<StructurePieceType, StructurePieceType> VOID_BONE_REMAINS_PIECE =
            PIECES.register("void_bone_remains", () -> (context, tag) -> new VoidBoneRemainsStructurePiece(tag, context.structureTemplateManager()));

    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR,AracneMod.MODID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<VoidChunkGenerator>> VOID =
            CHUNK_GENERATORS.register("void",
                    () -> VoidChunkGenerator.CODEC);
    public static final DeferredHolder<StructurePlacementType<?>,StructurePlacementType<VoidZiguratPlacement>> VOID_PLACEMENT = STRUCTURE_PLACEMENT_TYPE.register("void_placement",()-> (StructurePlacementType<VoidZiguratPlacement>) () -> VoidZiguratPlacement.CODEC);
    public static final DeferredHolder<StructureType<?>,StructureType<VoidZiguratStructure>> VOID_ZIGURAT =
            STRUCTURE_TYPE.register("void_zigurat",()-> () -> VoidZiguratStructure.CODEC);
    public static final DeferredHolder<StructureType<?>,StructureType<VoidNeedleHiveStructure>> NEEDLE_HIVE =
            STRUCTURE_TYPE.register("needle_hive",()-> () -> VoidNeedleHiveStructure.CODEC);

    public static final DeferredHolder<StructureType<?>,StructureType<VoidBoneRemainsStructure>> BONE_REMAINS =
            STRUCTURE_TYPE.register("bone_remains",()-> () -> VoidBoneRemainsStructure.CODEC);
    public static final DeferredHolder<Feature<?>,Feature<VoidCrystalFeatureConfiguration>> BONES =
            FEATURE.register("bones",()->new BonesFeature(VoidCrystalFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>,Feature<VoidCrystalFeatureConfiguration>> VOID_CRYSTAL =
            FEATURE.register("void_crystal",()->new VoidCrystalFeature(VoidCrystalFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>,Feature<DripstoneClusterConfiguration>> POINTED_BEDROCK =
            FEATURE.register("pointed_bedrock",()->new PoitedBedrockFeature(DripstoneClusterConfiguration.CODEC));

}
