package com.astianbk.arachnemod.mixin;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.astianbk.arachnemod.server.cap.data.BlessingData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public abstract class WebBlockMixin {
    @Inject(method = "entityInside",at = @At(value = "HEAD"),cancellable = true)
    public void entityInsideMixin(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise, CallbackInfo ci){
        if (entity instanceof Player player){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                AracneMod.LOGGER.info("inside");
                if (arachneAttachment.blessingIsActive(BlessingData.BlessingType.ARACHNE_MOVE)){
                    ci.cancel();
                }
            });
        }
    }
}
