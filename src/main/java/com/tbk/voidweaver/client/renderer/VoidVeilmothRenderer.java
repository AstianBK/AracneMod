package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.VoidVeilmothModel;
import com.tbk.voidweaver.server.entity.VoidVeilmothEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class VoidVeilmothRenderer<T extends VoidVeilmothEntity,M extends VoidVeilmothModel<T>> extends MobRenderer<T,M> {
    public VoidVeilmothRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidVeilmothModel<>(context.bakeLayer(VoidVeilmothModel.LAYER_LOCATION)), 0.25F);
        this.addLayer(new EyesLayer<T, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_veilmoth/void_veilmoth.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_veilmoth/void_veilmoth.png");
    }
}
