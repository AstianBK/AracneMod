package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.anim.WarSpiderAnim;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;

public class WarSpiderModel<T extends AvatarRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "warspider"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart torso;
	private final ModelPart lowertorso;
	private final ModelPart legright1;
	private final ModelPart right1lower;
	private final ModelPart right1spike;
	private final ModelPart legleft1;
	private final ModelPart left1lower;
	private final ModelPart left1spike;
	private final ModelPart legright2;
	private final ModelPart right1lower2;
	private final ModelPart right1spike2;
	private final ModelPart legleft2;
	private final ModelPart left1lower2;
	private final ModelPart left1spike2;
	private final ModelPart legright3;
	private final ModelPart right1lower3;
	private final ModelPart right1spike3;
	private final ModelPart legleft3;
	private final ModelPart left1lower3;
	private final ModelPart left1spike3;
	private final ModelPart booty;
	private final ModelPart head;
	private final ModelPart fangleft;
	private final ModelPart fangright;
	private final ModelPart grasperleft;
	private final ModelPart grasperleftlower;
	private final ModelPart grasperleftneedle;
	private final ModelPart grasperright;
	private final ModelPart grasperrightlower;
	private final ModelPart grasperrightneedle;

	private KeyframeAnimation walkLegs;
	private KeyframeAnimation walkBody;
	public WarSpiderModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.torso = this.main.getChild("torso");
		this.lowertorso = this.main.getChild("lowertorso");
		this.legright1 = this.main.getChild("legright1");
		this.right1lower = this.legright1.getChild("right1lower");
		this.right1spike = this.right1lower.getChild("right1spike");
		this.legleft1 = this.main.getChild("legleft1");
		this.left1lower = this.legleft1.getChild("left1lower");
		this.left1spike = this.left1lower.getChild("left1spike");
		this.legright2 = this.main.getChild("legright2");
		this.right1lower2 = this.legright2.getChild("right1lower2");
		this.right1spike2 = this.right1lower2.getChild("right1spike2");
		this.legleft2 = this.main.getChild("legleft2");
		this.left1lower2 = this.legleft2.getChild("left1lower2");
		this.left1spike2 = this.left1lower2.getChild("left1spike2");
		this.legright3 = this.main.getChild("legright3");
		this.right1lower3 = this.legright3.getChild("right1lower3");
		this.right1spike3 = this.right1lower3.getChild("right1spike3");
		this.legleft3 = this.main.getChild("legleft3");
		this.left1lower3 = this.legleft3.getChild("left1lower3");
		this.left1spike3 = this.left1lower3.getChild("left1spike3");
		this.booty = this.main.getChild("booty");
		this.head = this.main.getChild("head");
		this.fangleft = this.head.getChild("fangleft");
		this.fangright = this.head.getChild("fangright");
		this.grasperleft = this.main.getChild("grasperleft");
		this.grasperleftlower = this.grasperleft.getChild("grasperleftlower");
		this.grasperleftneedle = this.grasperleftlower.getChild("grasperleftneedle");
		this.grasperright = this.main.getChild("grasperright");
		this.grasperrightlower = this.grasperright.getChild("grasperrightlower");
		this.grasperrightneedle = this.grasperrightlower.getChild("grasperrightneedle");
		this.walkBody = WarSpiderAnim.move1.bake(root);
		this.walkLegs = WarSpiderAnim.move1legs.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = main.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 45).addBox(-6.0F, -9.0429F, -9.68F, 12.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.9571F, 5.68F));

		PartDefinition chest2_r1 = torso.addOrReplaceChild("chest2_r1", CubeListBuilder.create().texOffs(85, 55).addBox(-4.0F, -5.0F, -4.5F, 8.0F, 10.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.39F, 2.7274F, -0.2182F, 0.0F, 0.0F));

		PartDefinition lowertorso = main.addOrReplaceChild("lowertorso", CubeListBuilder.create().texOffs(52, 43).addBox(-5.0F, -6.5F, -8.5F, 10.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 7.5F));

		PartDefinition legright1 = main.addOrReplaceChild("legright1", CubeListBuilder.create(), PartPose.offset(-3.1176F, -8.2067F, 0.5F));

		PartDefinition cube_r1 = legright1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(105, 13).addBox(-3.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower = legright1.addOrReplaceChild("right1lower", CubeListBuilder.create(), PartPose.offset(-5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r2 = right1lower.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(90, 26).addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike = right1lower.addOrReplaceChild("right1spike", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1266F, -9.5F, 0.0F));

		PartDefinition legleft1 = main.addOrReplaceChild("legleft1", CubeListBuilder.create(), PartPose.offset(3.1176F, -8.2067F, 0.5F));

		PartDefinition cube_r3 = legleft1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(105, 13).mirror().addBox(-5.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower = legleft1.addOrReplaceChild("left1lower", CubeListBuilder.create(), PartPose.offset(5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r4 = left1lower.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(90, 26).mirror().addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike = left1lower.addOrReplaceChild("left1spike", CubeListBuilder.create().texOffs(73, 63).mirror().addBox(-2.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.1266F, -9.5F, 0.0F));

		PartDefinition legright2 = main.addOrReplaceChild("legright2", CubeListBuilder.create(), PartPose.offset(-3.1176F, -8.2067F, 10.5F));

		PartDefinition cube_r5 = legright2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(105, 13).addBox(-3.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower2 = legright2.addOrReplaceChild("right1lower2", CubeListBuilder.create(), PartPose.offset(-5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r6 = right1lower2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(90, 26).addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike2 = right1lower2.addOrReplaceChild("right1spike2", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1266F, -9.5F, 0.0F));

		PartDefinition legleft2 = main.addOrReplaceChild("legleft2", CubeListBuilder.create(), PartPose.offset(3.1176F, -8.2067F, 10.5F));

		PartDefinition cube_r7 = legleft2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(105, 13).mirror().addBox(-5.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower2 = legleft2.addOrReplaceChild("left1lower2", CubeListBuilder.create(), PartPose.offset(5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r8 = left1lower2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(90, 26).mirror().addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike2 = left1lower2.addOrReplaceChild("left1spike2", CubeListBuilder.create().texOffs(73, 63).mirror().addBox(-2.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.1266F, -9.5F, 0.0F));

		PartDefinition legright3 = main.addOrReplaceChild("legright3", CubeListBuilder.create(), PartPose.offset(-3.1176F, -8.2067F, 5.5F));

		PartDefinition cube_r9 = legright3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(105, 13).addBox(-3.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower3 = legright3.addOrReplaceChild("right1lower3", CubeListBuilder.create(), PartPose.offset(-5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r10 = right1lower3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(90, 26).addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike3 = right1lower3.addOrReplaceChild("right1spike3", CubeListBuilder.create().texOffs(73, 63).addBox(-4.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1266F, -9.5F, 0.0F));

		PartDefinition legleft3 = main.addOrReplaceChild("legleft3", CubeListBuilder.create(), PartPose.offset(3.1176F, -8.2067F, 5.5F));

		PartDefinition cube_r11 = legleft3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(105, 13).mirror().addBox(-5.0F, 0.0F, -2.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8824F, 1.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower3 = legleft3.addOrReplaceChild("left1lower3", CubeListBuilder.create(), PartPose.offset(5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r12 = left1lower3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(90, 26).mirror().addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8219F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike3 = left1lower3.addOrReplaceChild("left1spike3", CubeListBuilder.create().texOffs(73, 63).mirror().addBox(-2.0F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.1266F, -9.5F, 0.0F));

		PartDefinition booty = main.addOrReplaceChild("booty", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0805F, 13.8641F));

		PartDefinition cube_r13 = booty.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -9.0F, -12.5F, 20.0F, 18.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 11.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(65, 5).addBox(-7.0F, -7.75F, -11.0F, 14.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(36, 45).addBox(-6.0F, -6.9F, -10.0F, 12.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -23.25F, 5.0F));

		PartDefinition fangleft = head.addOrReplaceChild("fangleft", CubeListBuilder.create().texOffs(10, -2).mirror().addBox(0.0F, 3.5F, -2.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-1.5F, -0.5F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -2.25F, -11.0F));

		PartDefinition fangright = head.addOrReplaceChild("fangright", CubeListBuilder.create().texOffs(10, -2).addBox(0.0F, 3.5F, -2.0F, 0.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.25F, -11.0F));

		PartDefinition grasperleft = main.addOrReplaceChild("grasperleft", CubeListBuilder.create(), PartPose.offset(-4.0F, -15.8486F, -3.6808F));

		PartDefinition cube_r14 = grasperleft.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(101, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5736F, -3.8192F, 0.6109F, 0.0F, 0.0F));

		PartDefinition grasperleftlower = grasperleft.addOrReplaceChild("grasperleftlower", CubeListBuilder.create(), PartPose.offset(0.0F, 3.6073F, -3.9433F));

		PartDefinition cube_r15 = grasperleftlower.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(85, 33).addBox(-1.5F, -5.0F, -4.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0337F, -6.8759F, -0.9076F, 0.0F, 0.0F));

		PartDefinition grasperleftneedle = grasperleftlower.addOrReplaceChild("grasperleftneedle", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0498F, -6.4989F));

		PartDefinition cube_r16 = grasperleftneedle.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(102, -13).addBox(-1.0F, -4.0F, -7.0F, 0.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 9.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r17 = grasperleftneedle.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(101, 43).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition grasperright = main.addOrReplaceChild("grasperright", CubeListBuilder.create(), PartPose.offset(4.0F, -15.8486F, -3.6808F));

		PartDefinition cube_r18 = grasperright.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(101, 35).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.5736F, -3.8192F, 0.6109F, 0.0F, 0.0F));

		PartDefinition grasperrightlower = grasperright.addOrReplaceChild("grasperrightlower", CubeListBuilder.create(), PartPose.offset(0.0F, 3.6073F, -3.9433F));

		PartDefinition cube_r19 = grasperrightlower.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(85, 33).mirror().addBox(-1.5F, -5.0F, -4.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0337F, -6.8759F, -0.9076F, 0.0F, 0.0F));

		PartDefinition grasperrightneedle = grasperrightlower.addOrReplaceChild("grasperrightneedle", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0498F, -6.4989F));

		PartDefinition cube_r20 = grasperrightneedle.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(102, -13).mirror().addBox(1.0F, -4.0F, -7.0F, 0.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 9.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r21 = grasperrightneedle.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(101, 43).mirror().addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
	}
}