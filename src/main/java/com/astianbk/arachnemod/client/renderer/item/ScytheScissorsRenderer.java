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

    public static final ScytheScissorsModel.State INSTANCE = new ScytheScissorsModel.State(0,true, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
    public ScytheScissorsRenderer(ScytheScissorsModel model){
        this.model = model;
    }
    @Override
    public void submit(ScytheScissorsModel.State itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int i1, boolean b, int i2) {
        poseStack.pushPose();

//        poseStack.scale(2,2,2);



        if (itemStack.ctxDisplay().firstPerson()){
            poseStack.translate(0.3,0.75,0.4);
            poseStack.mulPose(Axis.YN.rotation(0.516810F));
            poseStack.mulPose(Axis.ZP.rotation(2.0000002F));
            poseStack.mulPose(Axis.XP.rotation(1.916809F));
        }else {
            poseStack.mulPose(Axis.YN.rotationDegrees(-130));
            poseStack.mulPose(Axis.XP.rotationDegrees(-35));
            poseStack.translate(-0.9,0.3,0.3);
            poseStack.mulPose(Axis.ZP.rotationDegrees(15));
        }

        model.setupAnim(itemStack);
        submitNodeCollector.submitModel(model, itemStack,poseStack, RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_scythe/voidscythe.png")),i,i1,i2,null);

        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {

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