package com.astianbk.arachnemod.client.model;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.anim.ArachneLegAnim;
import com.astianbk.arachnemod.client.render_state.ArachneLegRenderState;
import com.astianbk.arachnemod.client.render_state.EnterDimensionRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class ArachneLegModel<T extends ArachneLegRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "arachne_leg"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart legsright;
	private final ModelPart legright3;
	private final ModelPart right1lower3;
	private final ModelPart right1spike3;
	private final KeyframeAnimation spawn;
	public ArachneLegModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.legsright = this.main.getChild("legsright");
		this.legright3 = this.legsright.getChild("legright3");
		this.right1lower3 = this.legsright.getChild("right1lower3");
		this.right1spike3 = this.right1lower3.getChild("right1spike3");
		this.spawn = ArachneLegAnim.spawn.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.8274F, -0.2067F, 0.0377F, 0.0F, 0.0F, 1.5708F));

		PartDefinition legsright = main.addOrReplaceChild("legsright", CubeListBuilder.create(), PartPose.offset(0.2074F, 0.0774F, -0.0755F));

		PartDefinition legright3 = legsright.addOrReplaceChild("legright3", CubeListBuilder.create().texOffs(105, 13).addBox(-7.6324F, -0.7933F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1384F, -0.0774F, 0.0F));

		PartDefinition right1lower3 = legsright.addOrReplaceChild("right1lower3", CubeListBuilder.create().texOffs(90, 26).addBox(-12.822F, -1.3929F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.8616F, 0.0774F, 0.0F));

		PartDefinition right1spike3 = right1lower3.addOrReplaceChild("right1spike3", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.1266F, 0.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.spawn.apply(state.spawn,state.ageInTicks);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.5F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.5F);
//
		AracneMod.LOGGER.info("Rot Y :{}",state.yRot);
		this.truemain.yRot = state.yRot * 0.017453292F;
//		this.Head.xRot = state.xRot * 0.017453292F;

	}
}