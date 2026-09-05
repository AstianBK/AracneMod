package com.astianbk.arachnemod.client.renderer.item;

import com.astianbk.arachnemod.client.model.ScytheScissorsModel;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;

import javax.annotation.Nullable;
import java.util.Optional;

public class ScytheScissorsItemModel implements ItemModel {

    private final ScytheScissorsRenderer renderer;
    private final Matrix4fc transformation;

    public ScytheScissorsItemModel(ScytheScissorsRenderer renderer, Matrix4fc transformation) {
        this.renderer = renderer;
        this.transformation = transformation;
    }

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        output.appendModelIdentityElement(this);

        ItemStackRenderState.LayerRenderState layer = output.newLayer();

        layer.setLocalTransform(this.transformation);

        LivingEntity living = owner != null ? owner.asLivingEntity() : null;

        if (living == null && level != null) {
            living = Minecraft.getInstance().player;
        }
        if (living instanceof Player player){

            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                ScytheScissorsModel.State state = new ScytheScissorsModel.State(arachneAttachment.scissorAttackTime,arachneAttachment.scissorAttack, displayContext);
                layer.setupSpecialModel(renderer, state);
            });
        }



        output.setAnimated();
    }
    public record Unbaked(Identifier base, Optional<Transformation> transformation, SpecialModelRenderer.Unbaked<?> specialModel) implements ItemModel.Unbaked {
        public static final MapCodec<ScytheScissorsItemModel.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                                Identifier.CODEC.fieldOf("base").forGetter(ScytheScissorsItemModel.Unbaked::base),
                                Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(ScytheScissorsItemModel.Unbaked::transformation),
                                SpecialModelRenderers.CODEC.fieldOf("model").forGetter(ScytheScissorsItemModel.Unbaked::specialModel)
                        )
                        .apply(i, ScytheScissorsItemModel.Unbaked::new)
        );

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.base);
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
            SpecialModelRenderer<?> bakedSpecialModel = this.specialModel.bake(context);
            if (bakedSpecialModel == null) {
                return context.missingItemModel(modelTransform);
            }

            ModelRenderProperties properties = this.getProperties(context);
            return new ScytheScissorsItemModel(new ScytheScissorsRenderer(new ScytheScissorsModel(context.entityModelSet().bakeLayer(ScytheScissorsModel.LAYER_LOCATION))), modelTransform);
        }

        private ModelRenderProperties getProperties(ItemModel.BakingContext context) {
            ModelBaker baker = context.blockModelBaker();
            ResolvedModel model = baker.getModel(this.base);
            TextureSlots textureSlots = model.getTopTextureSlots();
            return ModelRenderProperties.fromResolvedModel(baker, model, textureSlots);
        }

        @Override
        public MapCodec<ScytheScissorsItemModel.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}