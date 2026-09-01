package com.astianbk.arachnemod.client.renderer;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.model.VoidHopperModel;
import com.astianbk.arachnemod.client.model.VoidScytheModel;
import com.astianbk.arachnemod.client.render_state.VoidHopperRenderState;
import com.astianbk.arachnemod.client.render_state.VoidScytheRenderState;
import com.astianbk.arachnemod.server.entity.VoidHopperEntity;
import com.astianbk.arachnemod.server.entity.VoidScytheEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class VoidScytheRenderer<T extends VoidScytheEntity,R extends VoidScytheRenderState,M extends VoidScytheModel<R>> extends MobRenderer<T,R,M> {
    public VoidScytheRenderer(EntityRendererProvider.Context context) {
        super(context, (M) new VoidScytheModel<>(context.bakeLayer(VoidScytheModel.LAYER_LOCATION)),1.0F);
        this.addLayer(new EyesLayer<R, M>(this) {
            @Override
            public RenderType renderType() {
                return RenderTypes.eyes(Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_scythe/voidscythe_eyes.png"));
            }
        });
    }

    @Override
    public void submit(R state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    protected void scale(R state, PoseStack poseStack) {
        poseStack.scale(1.3F,1.3F,1.3F);
        super.scale(state, poseStack);
    }

    @Override
    public Identifier getTextureLocation(R r) {
        return Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/void_scythe/voidscythe.png");
    }

    @Override
    public void extractRenderState(T entity, R state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idle = entity.idle;
        state.attack1 = entity.attack1.attack();
        state.prepareAttack1 = entity.attack1.prepare();
        state.attackLoop1 = entity.attack1.loop();

        state.attack2 = entity.attack2.attack();
        state.prepareAttack2 = entity.attack2.prepare();
        state.attackLoop2 = entity.attack2.loop();

        state.counter = entity.counter.attack();
        state.prepareCounter = entity.counter.prepare();
        state.counterLoop = entity.counter.loop();

        state.land = entity.jump.attack();
        state.jump = entity.jump.prepare();
        state.jumpLoop = entity.jump.loop();
        Vec3 velocity = entity.getDeltaMovement();
        state.isMoving = velocity.x != 0.0D || velocity.z != 0.0D;;
        state.isJump = entity.getPhase() == VoidScytheEntity.Phase.JUMP;
//        state.casting = entity.casting;
//        state.flee = entity.flee;
//        state.emerge = entity.emerge;
//        state.outlineColor = entity.getBlessing() == VoidHopperEntity.Blessing.NONE ? 0 : 0xFF0000 ;
    }

    @Override
    public R createRenderState() {
        return (R) new VoidScytheRenderState();
    }
}
