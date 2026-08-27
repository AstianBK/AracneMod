package com.astianbk.arachnemod.common.items;

import com.astianbk.arachnemod.AracneMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class OsmiumArmorItem extends Item {
    public OsmiumArmorItem(Properties properties) {
        super(properties);
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

        return ItemAttributeModifiers.builder().add(Attributes.ARMOR, new AttributeModifier(Identifier.fromNamespaceAndPath(AracneMod.MODID, "osmium_armor_durability_bonus"), armorBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ARMOR).build();
    }
}
