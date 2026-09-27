package com.tbk.voidweaver.mixin;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import com.tbk.voidweaver.server.cap.data.BlessingData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
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
    public void entityInsideMixin(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci){
        if (entity instanceof Player player){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                if (arachneAttachment.blessingIsActive(BlessingData.BlessingType.ARACHNE_MOVE)){
                    ci.cancel();
                }
            });
        }
    }
}
