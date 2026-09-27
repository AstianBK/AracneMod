package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.VoidNeedleModel;
import com.tbk.voidweaver.server.entity.VoidNeedleEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class VoidNeedleRenderer<T extends VoidNeedleEntity,M extends VoidNeedleModel<T>> extends MobRenderer<T,M> {
    public VoidNeedleRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidNeedleModel<>(context.bakeLayer(VoidNeedleModel.LAYER_LOCATION)),1.0F);
        this.addLayer(new EyesLayer<T, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_needle/voidneedle_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_needle/voidneedle.png");
    }

}
