package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.anim.EnterDimensionAnim;
import com.astianbk.arachnemod.client.render_state.EnterDimensionRenderState;
import com.astianbk.arachnemod.client.render_state.ScarabRenderState;
import com.astianbk.arachnemod.client.renderer.EnterDimensionRenderer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class EnterDimensionModel<T extends EnterDimensionRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "enter_dimension"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart legsright;
	private final ModelPart legright1;
	private final ModelPart right1lower;
	private final ModelPart right1spike;
	private final ModelPart legright2;
	private final ModelPart right1lower2;
	private final ModelPart right1spike2;
	private final ModelPart legright3;
	private final ModelPart right1lower3;
	private final ModelPart right1spike3;
	private final ModelPart legsleft;
	private final ModelPart legleft1;
	private final ModelPart left1lower;
	private final ModelPart left1spike;
	private final ModelPart legleft2;
	private final ModelPart left1lower2;
	private final ModelPart left1spike2;
	private final ModelPart legleft3;
	private final ModelPart left1lower3;
	private final ModelPart left1spike3;
	private KeyframeAnimation idle;
	private KeyframeAnimation take;
	private KeyframeAnimation spawn;
	public EnterDimensionModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.legsright = this.main.getChild("legsright");
		this.legright1 = this.legsright.getChild("legright1");
		this.right1lower = this.legright1.getChild("right1lower");
		this.right1spike = this.right1lower.getChild("right1spike");
		this.legright2 = this.legsright.getChild("legright2");
		this.right1lower2 = this.legright2.getChild("right1lower2");
		this.right1spike2 = this.right1lower2.getChild("right1spike2");
		this.legright3 = this.legsright.getChild("legright3");
		this.right1lower3 = this.legright3.getChild("right1lower3");
		this.right1spike3 = this.right1lower3.getChild("right1spike3");
		this.legsleft = this.main.getChild("legsleft");
		this.legleft1 = this.legsleft.getChild("legleft1");
		this.left1lower = this.legleft1.getChild("left1lower");
		this.left1spike = this.left1lower.getChild("left1spike");
		this.legleft2 = this.legsleft.getChild("legleft2");
		this.left1lower2 = this.legleft2.getChild("left1lower2");
		this.left1spike2 = this.left1lower2.getChild("left1spike2");
		this.legleft3 = this.legsleft.getChild("legleft3");
		this.left1lower3 = this.legleft3.getChild("left1lower3");
		this.left1spike3 = this.left1lower3.getChild("left1spike3");
		this.idle = EnterDimensionAnim.idle.bake(root);
		this.take = EnterDimensionAnim.take.bake(root);
		this.spawn = EnterDimensionAnim.spawn.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.7933F, 0.25F, 3.1416F, 0.0F, 0.0F));

		PartDefinition legsright = main.addOrReplaceChild("legsright", CubeListBuilder.create(), PartPose.offset(-4.1214F, 0.0F, -7.9128F));

		PartDefinition legright1 = legsright.addOrReplaceChild("legright1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.9599F, 0.0F));

		PartDefinition cube_r1 = legright1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(105, 13).addBox(-3.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower = legright1.addOrReplaceChild("right1lower", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.7233F, 3.1548F, 0.0F, 0.0F, 0.0F, -2.5744F));

		PartDefinition cube_r2 = right1lower.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(90, 26).addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike = right1lower.addOrReplaceChild("right1spike", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1266F, -9.5F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition legright2 = legsright.addOrReplaceChild("legright2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.9629F, 0.0F, 3.3962F, 0.0F, -0.4363F, 0.0F));

		PartDefinition cube_r3 = legright2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(105, 13).addBox(-3.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower2 = legright2.addOrReplaceChild("right1lower2", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.7233F, 3.1548F, 0.0F, 0.0F, 0.0F, -2.5744F));

		PartDefinition cube_r4 = right1lower2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(90, 26).addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.822F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike2 = right1lower2.addOrReplaceChild("right1spike2", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1266F, -9.5F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition legright3 = legsright.addOrReplaceChild("legright3", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.8096F, 0.0F, 7.3373F, 0.0F, -0.0873F, 0.0F));

		PartDefinition cube_r5 = legright3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(105, 13).addBox(-3.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower3 = legright3.addOrReplaceChild("right1lower3", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.7233F, 3.1548F, 0.0F, 0.0F, 0.0F, -2.5744F));

		PartDefinition cube_r6 = right1lower3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(90, 26).addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike3 = right1lower3.addOrReplaceChild("right1spike3", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1266F, -9.5F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition legsleft = main.addOrReplaceChild("legsleft", CubeListBuilder.create(), PartPose.offsetAndRotation(4.1214F, 0.0F, -8.9128F, 0.0F, 0.0873F, 0.0F));

		PartDefinition legleft1 = legsleft.addOrReplaceChild("legleft1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.9599F, 0.0F));

		PartDefinition cube_r7 = legleft1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(105, 13).mirror().addBox(-5.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower = legleft1.addOrReplaceChild("left1lower", CubeListBuilder.create(), PartPose.offsetAndRotation(5.7233F, 3.1548F, 0.0F, 0.0F, 0.0F, 2.5744F));

		PartDefinition cube_r8 = left1lower.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(90, 26).mirror().addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike = left1lower.addOrReplaceChild("left1spike", CubeListBuilder.create().texOffs(73, 63).mirror().addBox(-2.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.1266F, -9.5F, 0.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition legleft2 = legsleft.addOrReplaceChild("legleft2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.9629F, 0.0F, 3.3961F, 0.0F, 0.4363F, 0.0F));

		PartDefinition cube_r9 = legleft2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(105, 13).mirror().addBox(-5.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower2 = legleft2.addOrReplaceChild("left1lower2", CubeListBuilder.create(), PartPose.offsetAndRotation(5.7233F, 3.1548F, 0.0F, 0.0F, 0.0F, 2.5744F));

		PartDefinition cube_r10 = left1lower2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(90, 26).mirror().addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.822F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike2 = left1lower2.addOrReplaceChild("left1spike2", CubeListBuilder.create().texOffs(73, 63).mirror().addBox(-2.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.1266F, -9.5F, 0.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition legleft3 = legsleft.addOrReplaceChild("legleft3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.8096F, 0.0F, 7.3373F, 0.0F, 0.0873F, 0.0F));

		PartDefinition cube_r11 = legleft3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(105, 13).mirror().addBox(-5.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower3 = legleft3.addOrReplaceChild("left1lower3", CubeListBuilder.create(), PartPose.offsetAndRotation(5.7233F, 3.1548F, 0.0F, 0.0F, 0.0F, 2.5744F));

		PartDefinition cube_r12 = left1lower3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(90, 26).mirror().addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike3 = left1lower3.addOrReplaceChild("left1spike3", CubeListBuilder.create().texOffs(73, 63).mirror().addBox(-2.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.1266F, -9.5F, 0.0F, 0.0F, 0.0F, -1.1345F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.idle.apply(state.idle,state.ageInTicks);
		this.take.apply(state.take,state.ageInTicks);
		this.spawn.apply(state.spawn,state.ageInTicks);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.5F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.5F);
//
//		this.Head.yRot = state.yRot * 0.017453292F;
//		this.Head.xRot = state.xRot * 0.017453292F;

	}
}