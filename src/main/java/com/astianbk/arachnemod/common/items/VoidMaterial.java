package com.astianbk.arachnemod.common.items;

import com.astianbk.arachnemod.AracneMod;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.*;

import java.util.Map;

public class VoidMaterial implements ArmorMaterials {
    static ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    public static ResourceKey<EquipmentAsset> ASSET_VOID = createId("void");
    public static ResourceKey<EquipmentAsset> ASSET_OSMIUM = createId("osmium");
    public static ArmorMaterial VOID = new ArmorMaterial(5, makeDefense(3, 6, 8, 3, 0), 2, SoundEvents.ARMOR_EQUIP_NETHERITE, 7.0F, 0.0F, ItemTags.REPAIRS_LEATHER_ARMOR,ASSET_VOID);
    public static ArmorMaterial OSMIUM = new ArmorMaterial(8, makeDefense(2, 2, 3, 2, 0), 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 5.0F, 0.0F, ItemTags.REPAIRS_LEATHER_ARMOR,ASSET_OSMIUM);

    private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body));
    }

    static ResourceKey<EquipmentAsset> createId(String name) {
        return ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(AracneMod.MODID,name));
    }
}
