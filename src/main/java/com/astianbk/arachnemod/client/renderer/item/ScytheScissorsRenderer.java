package com.astianbk.arachnemod.client.renderer.item;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.AracneModClient;
import com.astianbk.arachnemod.client.model.ScytheScissorsModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public final class ScytheScissorsRenderer implements SpecialModelRenderer<ScytheScissorsModel.State> {
    public ScytheScissorsModel model;

    public static final ScytheScissorsModel.State INSTANCE = new ScytheScissorsModel.State(0,true, ItemDisplayContext.FIRST_PERSON_LEFT_HAND,null);
    public ScytheScissorsRenderer(ScytheScissorsModel model){
        this.model = model;
    }
    @Override
    public void submit(ScytheScissorsModel.State itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int i1, boolean b, int i2) {
        poseStack.pushPose();

        poseStack.translate(0.4, 0.0, -0.1);
        model.setupAnim(itemStack);
        submitNodeCollector.submitModel(model, itemStack,poseStack, RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_scythe/voidscythe.png")),i,i1,i2,null);

        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, output);
    }

    @Override
    public ScytheScissorsModel.State extractArgument(ItemStack itemStack) {
        return INSTANCE;
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<ScytheScissorsModel.State> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<ScytheScissorsModel.State> bake(SpecialModelRenderer.BakingContext context) {
            return new ScytheScissorsRenderer(new ScytheScissorsModel(context.entityModelSet().bakeLayer(ScytheScissorsModel.LAYER_LOCATION)));
        }

    }

}