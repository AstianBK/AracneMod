package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.model.ScarabModel;
import com.tbk.voidweaver.server.entity.ScarabEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class ScarabRenderer<T extends ScarabEntity,M extends ScarabModel<T>> extends MobRenderer<T,M> {
    public ScarabRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new ScarabModel<>(context.bakeLayer(ScarabModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new EyesLayer<T, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/scarab_1.png");

    }

}
