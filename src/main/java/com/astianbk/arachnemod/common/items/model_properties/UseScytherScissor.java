package com.astianbk.arachnemod.common.items.model_properties;

import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class UseScytherScissor implements ConditionalItemModelProperty {
    public static final MapCodec<UseScytherScissor> MAP_CODEC = MapCodec.unit(new UseScytherScissor());

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return livingEntity instanceof Player && ArachneAttachment.get((Player) livingEntity).get().scissorAttack;
    }
}
