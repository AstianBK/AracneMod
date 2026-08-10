package com.astianbk.aracnemod.common.registry;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.common.block.PointedUpBlock;
import com.astianbk.aracnemod.common.block.WeaverIdolBlock;
import com.astianbk.aracnemod.common.effect.MarkEffect;
import com.astianbk.aracnemod.common.effect.SilentEffect;
import com.astianbk.aracnemod.common.items.VoidKnightArmorItem;
import com.astianbk.aracnemod.common.items.VoidMaterial;
import com.astianbk.aracnemod.common.worldgenerator.VoidChunkGenerator;
import com.astianbk.aracnemod.server.OrbEntity;
import com.astianbk.aracnemod.server.ScarabEntity;
import com.astianbk.aracnemod.server.VoidHopperEntity;
import com.astianbk.aracnemod.server.VoidNeedleEntity;
import com.astianbk.aracnemod.server.cap.NerubianCap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class NRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AracneMod.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AracneMod.MODID);
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, AracneMod.MODID);

    public static final Supplier<AttachmentType<NerubianCap>> ARACNE =
            ATTACHMENTS.register(
                    "aracne",
                    () -> AttachmentType.builder(NerubianCap::new)
                            .serialize(new NerubianCap.NerubianCapSerializer())
                            .copyOnDeath()
                            .build());
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(AracneMod.MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(AracneMod.MODID);
    public static final DeferredHolder<MobEffect, SilentEffect> SILENT = EFFECTS.register("silent",SilentEffect::new);
    public static final DeferredHolder<MobEffect, MarkEffect> MARK_SILENT = EFFECTS.register("mark_silent",MarkEffect::new);

    public static final DeferredBlock<Block> WEAVER_IDOL_BLOCK = BLOCKS.registerBlock("weaver_idol", WeaverIdolBlock::new);
    public static final DeferredBlock<Block> BEDCRUST_BLOCK = BLOCKS.registerBlock("bedcrust", Block::new);
    public static final DeferredBlock<Block> BEDSLAG_BLOCK = BLOCKS.registerBlock("bedslag", Block::new);
    public static final DeferredBlock<Block> BEDSTONE_BLOCK = BLOCKS.registerBlock("bedstone", Block::new);
    public static final DeferredBlock<Block> CHISELED_BEDROCK_BLOCK = BLOCKS.registerBlock("chiseled_bedrock", Block::new);
    public static final DeferredBlock<Block> COBBLED_BEDROCK_BLOCK = BLOCKS.registerBlock("cobbled_bedrock", Block::new);
    public static final DeferredBlock<Block> BRICKED_BEDROCK_BLOCK = BLOCKS.registerBlock("bricked_bedrock", Block::new);
    public static final DeferredBlock<Block> STONE_BEDROCK_BLOCK = BLOCKS.registerBlock("stone_bedrock", Block::new);
    public static final DeferredBlock<Block> SLATED_BEDROCK_BLOCK = BLOCKS.registerBlock("slated_bedrock", Block::new);
    public static final DeferredBlock<Block> CRACKED_BEDROCK_BLOCK = BLOCKS.registerBlock("cracked_bedrock", Block::new);

    public static final DeferredBlock<Block> POINTED_BEDROCK_BLOCK = BLOCKS.registerBlock("pointed_bedrock", PointedUpBlock::new);

    public static final DeferredItem<BlockItem> WEAVER_IDOL_ITEM = ITEMS.registerSimpleBlockItem("weaver_idol_item",WEAVER_IDOL_BLOCK);
    public static final DeferredItem<BlockItem> BEDCRUST_ITEM = ITEMS.registerSimpleBlockItem("bedcrust_item",BEDCRUST_BLOCK);
    public static final DeferredItem<BlockItem> BEDSLAG_ITEM = ITEMS.registerSimpleBlockItem("bedslag_item",BEDSLAG_BLOCK);
    public static final DeferredItem<BlockItem> BEDSTONE_ITEM = ITEMS.registerSimpleBlockItem("bedstone_item",BEDSTONE_BLOCK);
    public static final DeferredItem<BlockItem> CRACKED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("cracked_bedrock_item",CRACKED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> CHISELED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("chiseled_bedrock_item",CHISELED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> COBBLED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("cobbled_bedrock_item",COBBLED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> BRICKED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("bricked_bedrock_item",BRICKED_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> STONE_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("stone_bedrock_item",STONE_BEDROCK_BLOCK);
    public static final DeferredItem<BlockItem> SLATED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("slated_bedrock_item",SLATED_BEDROCK_BLOCK);

    public static final DeferredItem<BlockItem> POINTED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("pointed_bedrock_item",POINTED_BEDROCK_BLOCK);

    public static final DeferredItem<Item> VOID_HELMET = ITEMS.registerItem("void_helmet",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.HELMET).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_helmet" )))));

    public static final DeferredItem<Item> VOID_CHESTPLATE = ITEMS.registerItem("void_chestplate",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.CHESTPLATE).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_chestplate" )))));
    public static final DeferredItem<Item> VOID_LEGGINGS = ITEMS.registerItem("void_leggings",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.LEGGINGS).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_leggings" )))));

    public static final DeferredItem<Item> VOID_BOOTS = ITEMS.registerItem("void_boots",(properties)->new VoidKnightArmorItem(new Item.Properties().humanoidArmor(VoidMaterial.VOID, ArmorType.BOOTS).setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_boots" )))));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.aracnemod"))
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

            }).build());
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(AracneMod.MODID);
    public static final ResourceKey<Level> THE_VOID = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(AracneMod.MODID,"void"));
    public static final DeferredHolder<EntityType<?>, EntityType<OrbEntity>> ORB =
            ENTITY_TYPES.register("orb",
                    () -> EntityType.Builder
                            .of(OrbEntity::new, MobCategory.MISC)
                            .sized(2.0F, 2.0F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"orb"))));

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
                    () -> EntityType.Builder
                            .of(VoidHopperEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 2.0F).clientTrackingRange(10).updateInterval(2)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"void_needle"))));

    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR,AracneMod.MODID);

    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<VoidChunkGenerator>> VOID =
            CHUNK_GENERATORS.register("void",
                    () -> VoidChunkGenerator.CODEC);
}
