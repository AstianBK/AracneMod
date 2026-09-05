package com.astianbk.arachnemod.client.renderer.item;

import com.astianbk.arachnemod.client.model.ScytheScissorsModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class ScytheScissorsClientExtensions implements IClientItemExtensions {
    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack stack, float partialTick, float equipProcess, float swingProcess) {
        if (!player.isUsingItem() || player.getUseItem() != stack) {
            return false;
        }
        Minecraft minecraft = Minecraft.getInstance();
        ScytheScissorsModel model = new ScytheScissorsModel( minecraft.getEntityModels().bakeLayer(ScytheScissorsModel.LAYER_LOCATION));



        float ticksUsando = player.getTicksUsingItem() + partialTick;

        float movimiento = Mth.sin(ticksUsando * 0.35F) * 0.08F;

        poseStack.translate(0.0D, movimiento, 0.0D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(ticksUsando * 0.35F) * 12.0F));

        return true;
    }
}