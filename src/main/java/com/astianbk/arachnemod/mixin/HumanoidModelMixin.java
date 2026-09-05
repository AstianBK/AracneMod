package com.astianbk.arachnemod.mixin;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.AracneModClient;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {
    @Shadow
    public ModelPart rightArm;
    @Shadow
    public ModelPart head;

    @Inject(method = "poseRightArm",at = @At(value = "HEAD"),cancellable = true)
    private void poseMix(HumanoidRenderState state,CallbackInfo callbackInfo){
        if (state.rightArmPose == HumanoidModel.ArmPose.BLOCK && state.rightHandItemStack.is(NRegistry.REAVER_GAUNTLET)){
            callbackInfo.cancel();
            rightArm.xRot = rightArm.xRot * 0.5F - 1.5424779F + Mth.clamp(this.head.xRot, (float) (-Math.PI * 4.0 / 9.0), 0.43633232F);
            rightArm.yRot = ( -60.0F ) * (float) (Math.PI / 180.0) + Mth.clamp(this.head.yRot, (float) (-Math.PI / 6), (float) (Math.PI / 6));

            AracneMod.LOGGER.info("poseMix");
            if (!state.getRenderData(AracneModClient.IS_FIRST_PERSON)){
            }else {
                AracneMod.LOGGER.info("is_first_person");
            }
        }
    }
}
