package com.tbk.voidweaver.common.items;

import com.tbk.voidweaver.AracneMod;
import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VoidMaterial {

    public static final Holder<ArmorMaterial> NEEDLE_HELMET = register("needle_helmet",
            makeDefense(3, 6, 8, 2, 0),
            5,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of()
    );

    public static final Holder<ArmorMaterial> VOID = register("void",makeDefense(3, 6, 8, 3, 0),
            5,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            7.0F,
            0.0F,() -> Ingredient.of());

    public static final Holder<ArmorMaterial> OSMIUM = register("osmium",
            makeDefense(2, 2, 3, 2, 0),
            8,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            5.0F,
            0.0F,
            () -> Ingredient.of()
    );

    private static EnumMap<ArmorItem.Type, Integer> makeDefense(
            int boots,
            int legs,
            int chest,
            int helm,
            int body
    ) {
        EnumMap<ArmorItem.Type, Integer> map =
                Maps.newEnumMap(ArmorItem.Type.class);

        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, legs);
        map.put(ArmorItem.Type.CHESTPLATE, chest);
        map.put(ArmorItem.Type.HELMET, helm);
        map.put(ArmorItem.Type.BODY, body);

        return map;
    }
    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngridient, List<ArmorMaterial.Layer> layers) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap(ArmorItem.Type.class);
        ArmorItem.Type[] var9 = ArmorItem.Type.values();
        int var10 = var9.length;

        for(int var11 = 0; var11 < var10; ++var11) {
            ArmorItem.Type armoritem$type = var9[var11];
            enummap.put(armoritem$type, (Integer)defense.get(armoritem$type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, ResourceLocation.withDefaultNamespace(name), new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngridient, layers, toughness, knockbackResistance));
    }
}