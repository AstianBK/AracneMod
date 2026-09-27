package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.VoidHopperModel;
import com.tbk.voidweaver.client.model.VoidScytheModel;
import com.tbk.voidweaver.server.entity.VoidScytheEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class VoidScytheRenderer<T extends VoidScytheEntity,M extends VoidScytheModel<T>> extends MobRenderer<T,M> {
    public VoidScytheRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidScytheModel<>(context.bakeLayer(VoidScytheModel.LAYER_LOCATION)),1.0F);
        this.addLayer(new EyesLayer<T, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_scythe/voidscythe_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_scythe/voidscythe.png");
    }

    @Override
    protected void scale(T livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.3F,1.3F,1.3F);

        super.scale(livingEntity, poseStack, partialTickTime);
    }
}
