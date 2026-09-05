package com.astianbk.arachnemod.common.items;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.data_component.ScytheScissorsAttack;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

public class ScytheScissorsItem extends Item {
    public ScytheScissorsItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack item, int ticksRemaining) {
        super.onUseTick(level, livingEntity, item, ticksRemaining);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BLOCK;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        return super.releaseUsing(itemStack, level, entity, remainingTime);
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }
}
