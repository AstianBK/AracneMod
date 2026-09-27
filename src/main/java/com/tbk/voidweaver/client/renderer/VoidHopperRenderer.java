package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.VoidHopperModel;
import com.tbk.voidweaver.server.entity.VoidHopperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class VoidHopperRenderer<T extends VoidHopperEntity,M extends VoidHopperModel<T>> extends MobRenderer<T,M> {
    public VoidHopperRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidHopperModel<>(context.bakeLayer(VoidHopperModel.LAYER_LOCATION)),1.0F);
        this.addLayer(new EyesLayer<T, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_hopper/voidhopper_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_hopper/voidhopper.png");
    }

}
