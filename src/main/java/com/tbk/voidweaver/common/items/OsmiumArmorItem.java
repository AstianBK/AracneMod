package com.tbk.voidweaver.common.items;

import com.tbk.voidweaver.AracneMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class OsmiumArmorItem extends ArmorItem {
    public OsmiumArmorItem(Type type,Properties properties) {
        super(VoidMaterial.OSMIUM,type,properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {

        ItemAttributeModifiers original = super.getDefaultAttributeModifiers(stack);

        if (!stack.isDamageableItem()) {
            return original;
        }

        float damageRatio = (float) stack.getDamageValue() / (float) stack.getMaxDamage();

        float armorBonus = 8.0F * damageRatio;

        if (armorBonus <= 0.0F) {
            return original;
        }

        return ItemAttributeModifiers.builder().add(Attributes.ARMOR, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "osmium_armor_durability_bonus"), armorBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build();
    }
}
