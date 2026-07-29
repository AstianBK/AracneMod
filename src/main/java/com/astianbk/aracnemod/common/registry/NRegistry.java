package com.astianbk.aracnemod.common.registry;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.common.block.WeaverIdolBlock;
import com.astianbk.aracnemod.server.ScarabEntity;
import com.astianbk.aracnemod.server.cap.NerubianCap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public static final DeferredBlock<Block> WEAVER_IDOL_BLOCK = BLOCKS.registerBlock("weaver_idol", WeaverIdolBlock::new);
    public static final DeferredBlock<Block> BEDCRUST_BLOCK = BLOCKS.registerBlock("bedcrust", WeaverIdolBlock::new);
    public static final DeferredBlock<Block> BEDSLAG_BLOCK = BLOCKS.registerBlock("bedslag", WeaverIdolBlock::new);
    public static final DeferredBlock<Block> BEDSTONE_BLOCK = BLOCKS.registerBlock("bedstone", WeaverIdolBlock::new);
    public static final DeferredBlock<Block> CRACKED_BEDROCK_BLOCK = BLOCKS.registerBlock("cracked_bedrock", WeaverIdolBlock::new);

    public static final DeferredItem<BlockItem> WEAVER_IDOL_ITEM = ITEMS.registerSimpleBlockItem("weaver_idol_item",WEAVER_IDOL_BLOCK);
    public static final DeferredItem<BlockItem> BEDCRUST_ITEM = ITEMS.registerSimpleBlockItem("bedcrust_item",BEDCRUST_BLOCK);
    public static final DeferredItem<BlockItem> BEDSLAG_ITEM = ITEMS.registerSimpleBlockItem("bedslag_item",BEDSLAG_BLOCK);
    public static final DeferredItem<BlockItem> BEDSTONE_ITEM = ITEMS.registerSimpleBlockItem("bedstone_item",BEDSTONE_BLOCK);
    public static final DeferredItem<BlockItem> CRACKED_BEDROCK_ITEM = ITEMS.registerSimpleBlockItem("cracked_bedrock_item",CRACKED_BEDROCK_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.AracneMod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> WEAVER_IDOL_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WEAVER_IDOL_ITEM.get());
                output.accept(BEDCRUST_ITEM.get());
                output.accept(BEDSLAG_ITEM.get());
                output.accept(BEDSTONE_ITEM.get());
                output.accept(CRACKED_BEDROCK_ITEM.get());
            }).build());
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(AracneMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ScarabEntity>> SCARAB =
            ENTITY_TYPES.register("scarab",
                    () -> EntityType.Builder
                            .of(ScarabEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 2.0F).clientTrackingRange(10)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(AracneMod.MODID,"scarab"))));
}
