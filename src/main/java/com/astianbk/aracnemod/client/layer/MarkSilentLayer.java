package com.astianbk.aracnemod.client.layer;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.AracneModClient;
import com.astianbk.aracnemod.common.registry.NRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MarkSilentLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
    public MarkSilentLayer(RenderLayerParent<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
        super(renderer);

    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, LivingEntityRenderState livingEntityRenderState, float v, float v1) {
        if (livingEntityRenderState.getRenderData(AracneModClient.EFFECT)!=null){
            MobEffectInstance instance = livingEntityRenderState.getRenderData(AracneModClient.EFFECT);
            if (instance.is(NRegistry.MARK_SILENT)){
                for (int k = 0 ; k < instance.getAmplifier()+1 ; k++){
                    poseStack.pushPose();
                    double sin = Mth.sin((livingEntityRenderState.ageInTicks+k*120.0F)/30.0F);
                    double cos = Mth.cos((livingEntityRenderState.ageInTicks+k*120.0F)/30.0F);
                    ItemStackRenderState renderState = new ItemStackRenderState();
                    poseStack.translate(-1.25*sin,0,1.25*cos);
                    Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderState,new ItemStack(Items.SNOWBALL), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,null,null,-1);
                    renderState.submit(poseStack,submitNodeCollector,i, OverlayTexture.NO_OVERLAY,livingEntityRenderState.outlineColor);
                    poseStack.popPose();
                }
            }

        }

    }
}
