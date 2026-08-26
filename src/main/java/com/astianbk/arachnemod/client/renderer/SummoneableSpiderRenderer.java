package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.model.ScarabModel;
import com.astianbk.arachnemod.client.render_state.ScarabRenderState;
import com.astianbk.arachnemod.client.render_state.SummonableSpiderRenderState;
import com.astianbk.arachnemod.server.entity.ScarabEntity;
import com.astianbk.arachnemod.server.entity.SummoneableSpiderEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

public class SummoneableSpiderRenderer <T extends SummoneableSpiderEntity,R extends LivingEntityRenderState,M extends SpiderModel> extends MobRenderer<T,R,M> {
    private static final Identifier SPIDER_LOCATION = Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/spider_minion/spider_minion.png");

    public SummoneableSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new SpiderModel(context.bakeLayer(ModelLayers.SPIDER)), 0.5F);
        this.addLayer(new EyesLayer(this) {
            @Override
            public RenderType renderType() {
                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/spider_minion/spider_minion_eyes.png"));
            }
        });
    }

    @Override
    protected void scale(R state, PoseStack poseStack) {
        poseStack.scale(0.5F,0.5F,0.5F);
        super.scale(state, poseStack);
    }

    @Override
    public Identifier getTextureLocation(R r) {
        return SPIDER_LOCATION;
    }

    @Override
    public R createRenderState() {
        return (R) new SummonableSpiderRenderState();
    }
}
