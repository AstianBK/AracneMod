package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.ScarabRenderState;
import com.astianbk.arachnemod.client.VoidBeetleRenderState;
import com.astianbk.arachnemod.client.VoidVeilmothRenderState;
import com.astianbk.arachnemod.client.anim.VoidVeilmothAnim;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class VoidVeilmothModel<T extends VoidVeilmothRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "veilmoth"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart torso;
	private final ModelPart chest;
	private final ModelPart head;
	private final ModelPart antennaright;
	private final ModelPart sec1;
	private final ModelPart sec2;
	private final ModelPart antennaleft;
	private final ModelPart sec3;
	private final ModelPart sec4;
	private final ModelPart legleft;
	private final ModelPart legleft3;
	private final ModelPart legright3;
	private final ModelPart legright;
	private final ModelPart legleft2;
	private final ModelPart legright2;
	private final ModelPart lowerchest;
	private final ModelPart tail;
	private final ModelPart wings;
	private final ModelPart wingleft;
	private final ModelPart wingright;
	private KeyframeAnimation idle;
	private KeyframeAnimation walk;

	public VoidVeilmothModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.torso = this.main.getChild("torso");
		this.chest = this.torso.getChild("chest");
		this.head = this.torso.getChild("head");
		this.antennaright = this.head.getChild("antennaright");
		this.sec1 = this.antennaright.getChild("sec1");
		this.sec2 = this.antennaright.getChild("sec2");
		this.antennaleft = this.head.getChild("antennaleft");
		this.sec3 = this.antennaleft.getChild("sec3");
		this.sec4 = this.antennaleft.getChild("sec4");
		this.legleft = this.torso.getChild("legleft");
		this.legleft3 = this.torso.getChild("legleft3");
		this.legright3 = this.torso.getChild("legright3");
		this.legright = this.torso.getChild("legright");
		this.legleft2 = this.torso.getChild("legleft2");
		this.legright2 = this.torso.getChild("legright2");
		this.lowerchest = this.torso.getChild("lowerchest");
		this.tail = this.lowerchest.getChild("tail");
		this.wings = this.main.getChild("wings");
		this.wingleft = this.wings.getChild("wingleft");
		this.wingright = this.wings.getChild("wingright");
		this.idle = VoidVeilmothAnim.idle.bake(root);
		this.walk = VoidVeilmothAnim.wingson.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = main.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition chest = torso.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, -1.528F, -2.0167F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.472F, -1.9833F));

		PartDefinition antennaright = head.addOrReplaceChild("antennaright", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0735F, -1.236F, -0.6167F, 0.0F, 0.0F, -0.48F));

		PartDefinition sec1 = antennaright.addOrReplaceChild("sec1", CubeListBuilder.create().texOffs(16, 4).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.8727F, 0.0F, 0.0F));

		PartDefinition sec2 = antennaright.addOrReplaceChild("sec2", CubeListBuilder.create().texOffs(16, 8).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition antennaleft = head.addOrReplaceChild("antennaleft", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0735F, -1.236F, -0.6167F, 0.0F, 0.0F, 0.48F));

		PartDefinition sec3 = antennaleft.addOrReplaceChild("sec3", CubeListBuilder.create().texOffs(16, 4).mirror().addBox(-1.5F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.8727F, 0.0F, 0.0F));

		PartDefinition sec4 = antennaleft.addOrReplaceChild("sec4", CubeListBuilder.create().texOffs(16, 8).mirror().addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legleft = torso.addOrReplaceChild("legleft", CubeListBuilder.create().texOffs(19, 1).mirror().addBox(-0.075F, -0.1F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.075F, 0.5F, -0.5F));

		PartDefinition legleft3 = torso.addOrReplaceChild("legleft3", CubeListBuilder.create().texOffs(19, 1).mirror().addBox(-0.075F, -0.1F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.075F, 0.5F, 2.75F));

		PartDefinition legright3 = torso.addOrReplaceChild("legright3", CubeListBuilder.create().texOffs(19, 1).addBox(-4.925F, -0.1F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.075F, 0.5F, 2.75F));

		PartDefinition legright = torso.addOrReplaceChild("legright", CubeListBuilder.create().texOffs(19, 1).addBox(-4.925F, -0.1F, -1.5F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.075F, 0.5F, -0.5F));

		PartDefinition legleft2 = torso.addOrReplaceChild("legleft2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.175F, 0.65F, 2.7F, 0.6545F, 0.0F, 0.0F));

		PartDefinition legright2 = torso.addOrReplaceChild("legright2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.225F, 0.725F, 2.7F, 0.6545F, 0.0F, 0.0F));

		PartDefinition lowerchest = torso.addOrReplaceChild("lowerchest", CubeListBuilder.create(), PartPose.offset(0.0F, -1.475F, 1.725F));

		PartDefinition lowerchest_r1 = lowerchest.addOrReplaceChild("lowerchest_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.975F, 0.675F, -0.5672F, 0.0F, 0.0F));

		PartDefinition tail = lowerchest.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-0.5F, 1.7809F, 1.9401F));

		PartDefinition lowerchesttail_r1 = tail.addOrReplaceChild("lowerchesttail_r1", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5672F, 0.0F, 0.0F));

		PartDefinition wings = main.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(-1.0F, -2.0F, 1.0F));

		PartDefinition wingleft = wings.addOrReplaceChild("wingleft", CubeListBuilder.create().texOffs(-13, 14).addBox(0.0F, 0.0F, -3.0F, 11.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.0F, -1.0F));

		PartDefinition wingright = wings.addOrReplaceChild("wingright", CubeListBuilder.create().texOffs(-13, 14).mirror().addBox(-11.0F, 0.0F, -3.0F, 11.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -2.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.idle.apply(state.idle,state.ageInTicks);
		this.walk.apply(state.idle,state.ageInTicks);
//		this.attack1.apply(state.attack1,state.ageInTicks,0.5F);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.5F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.5F);
//
//		this.Head.yRot = state.yRot * 0.017453292F;
//		this.Head.xRot = state.xRot * 0.017453292F;

	}
}