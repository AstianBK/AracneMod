
package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.VoidBeetleModel;
import com.tbk.voidweaver.server.entity.VoidBeetleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class VoidBeetleRenderer<T extends VoidBeetleEntity,M extends VoidBeetleModel<T>> extends MobRenderer<T,M> {
    public VoidBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidBeetleModel<>(context.bakeLayer(VoidBeetleModel.LAYER_LOCATION)), 0.25F);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/void_beetle/void_beetle_eyes.png"));
            }

        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_beetle/void_beetle.png");
    }
}
