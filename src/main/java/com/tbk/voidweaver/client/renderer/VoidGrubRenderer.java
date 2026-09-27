
package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.VoidGrubModel;
import com.tbk.voidweaver.server.entity.VoidGrubEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class VoidGrubRenderer<T extends VoidGrubEntity,M extends VoidGrubModel<T>> extends MobRenderer<T,M> {
    public VoidGrubRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidGrubModel<>(context.bakeLayer(VoidGrubModel.LAYER_LOCATION)), 0.25F);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/entity/void_grub/voidgrub_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_grub/voidgrub.png");
    }


}
