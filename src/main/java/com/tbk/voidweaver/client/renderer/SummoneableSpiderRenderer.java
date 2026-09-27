package com.tbk.voidweaver.client.renderer;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.server.entity.SummoneableSpiderEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class SummoneableSpiderRenderer <T extends SummoneableSpiderEntity,M extends SpiderModel<T>> extends MobRenderer<T,M> {
    private static final ResourceLocation SPIDER_LOCATION = ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/spider_minion/spider_minion.png");

    public SummoneableSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new SpiderModel(context.bakeLayer(ModelLayers.SPIDER)), 0.5F);
        this.addLayer(new EyesLayer(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/spider_minion/spider_minion_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return SPIDER_LOCATION;
    }


    @Override
    protected void scale(T livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.5F,0.5F,0.5F);

        super.scale(livingEntity, poseStack, partialTickTime);
    }

}
