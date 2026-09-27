package com.tbk.voidweaver.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.anim.VoidScytheAnim;
import com.tbk.voidweaver.server.entity.VoidScytheEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class VoidScytheModel<T extends VoidScytheEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "void_scythe"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart mandibles;
	private final ModelPart right;
	private final ModelPart left;
	private final ModelPart torso;
	private final ModelPart necktorso;
	private final ModelPart Upper;
	private final ModelPart shell;
	private final ModelPart grasperleft;
	private final ModelPart grasperleftlower;
	private final ModelPart grasperleftneedle;
	private final ModelPart grasperright;
	private final ModelPart grasperrightlower;
	private final ModelPart grasperrightneedle;
	private final ModelPart legleft1;
	private final ModelPart left1lower;
	private final ModelPart left1spike;
	private final ModelPart legright1;
	private final ModelPart right1lower;
	private final ModelPart right1spike;
	private final ModelPart legleft2;
	private final ModelPart left1lower2;
	private final ModelPart left1spike2;
	private final ModelPart legright2;
	private final ModelPart right1lower2;
	private final ModelPart right1spike2;
	public VoidScytheModel(ModelPart root) {
        super();
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.head = this.main.getChild("head");
		this.mandibles = this.head.getChild("mandibles");
		this.right = this.mandibles.getChild("right");
		this.left = this.mandibles.getChild("left");
		this.torso = this.main.getChild("torso");
		this.necktorso = this.torso.getChild("necktorso");
		this.Upper = this.necktorso.getChild("Upper");
		this.shell = this.torso.getChild("shell");
		this.grasperleft = this.main.getChild("grasperleft");
		this.grasperleftlower = this.grasperleft.getChild("grasperleftlower");
		this.grasperleftneedle = this.grasperleftlower.getChild("grasperleftneedle");
		this.grasperright = this.main.getChild("grasperright");
		this.grasperrightlower = this.grasperright.getChild("grasperrightlower");
		this.grasperrightneedle = this.grasperrightlower.getChild("grasperrightneedle");
		this.legleft1 = this.main.getChild("legleft1");
		this.left1lower = this.legleft1.getChild("left1lower");
		this.left1spike = this.left1lower.getChild("left1spike");
		this.legright1 = this.main.getChild("legright1");
		this.right1lower = this.legright1.getChild("right1lower");
		this.right1spike = this.right1lower.getChild("right1spike");
		this.legleft2 = this.main.getChild("legleft2");
		this.left1lower2 = this.legleft2.getChild("left1lower2");
		this.left1spike2 = this.left1lower2.getChild("left1spike2");
		this.legright2 = this.main.getChild("legright2");
		this.right1lower2 = this.legright2.getChild("right1lower2");
		this.right1spike2 = this.right1lower2.getChild("right1spike2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 22.75F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 68).addBox(-3.5F, -4.4214F, -10.3372F, 7.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(56, 84).addBox(3.5F, -4.4214F, -6.3372F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(56, 84).mirror().addBox(-7.5F, -4.4214F, -6.3372F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, -26.75F, 0.7139F));

		PartDefinition mandibles = head.addOrReplaceChild("mandibles", CubeListBuilder.create(), PartPose.offset(7.5F, 5.5065F, -18.9976F));

		PartDefinition right = mandibles.addOrReplaceChild("right", CubeListBuilder.create().texOffs(52, 56).mirror().addBox(-6.5F, 0.0F, -13.5F, 7.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-11.5F, -7.9279F, 11.1604F));

		PartDefinition left = mandibles.addOrReplaceChild("left", CubeListBuilder.create().texOffs(52, 56).addBox(-0.5F, 0.0F, -13.5F, 7.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -7.9279F, 11.1604F));

		PartDefinition torso = main.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.5F, -10.9846F, -0.3891F));

		PartDefinition necktorso = torso.addOrReplaceChild("necktorso", CubeListBuilder.create(), PartPose.offset(0.0F, 3.4846F, 0.3891F));

		PartDefinition necklower_r1 = necktorso.addOrReplaceChild("necklower_r1", CubeListBuilder.create().texOffs(0, 83).addBox(-2.5F, -5.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.1156F, -0.9785F, 0.4363F, 0.0F, 0.0F));

		PartDefinition Upper = necktorso.addOrReplaceChild("Upper", CubeListBuilder.create(), PartPose.offset(-0.5F, -9.0F, -3.0F));

		PartDefinition neckupper_r1 = Upper.addOrReplaceChild("neckupper_r1", CubeListBuilder.create().texOffs(36, 77).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.5F, -5.803F, 0.4447F, -0.0873F, 0.0F, 0.0F));

		PartDefinition shell = torso.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.5506F, -0.7444F, 10.0F, 9.0F, 25.0F, new CubeDeformation(0.0F))
				.texOffs(58, 84).addBox(-3.0F, -3.0506F, -0.5444F, 6.0F, 9.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition grasperleft = main.addOrReplaceChild("grasperleft", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -19.8486F, -2.6808F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r1 = grasperleft.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(70, 71).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.4098F, -6.8117F, 0.6109F, 0.0F, 0.0F));

		PartDefinition grasperleftlower = grasperleft.addOrReplaceChild("grasperleftlower", CubeListBuilder.create(), PartPose.offset(0.0F, 6.4435F, -6.9359F));

		PartDefinition cube_r2 = grasperleftlower.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(70, 17).addBox(-2.5F, -5.0F, -4.0F, 4.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0337F, -6.8759F, -0.9076F, 0.0F, 0.0F));

		PartDefinition grasperleftneedle = grasperleftlower.addOrReplaceChild("grasperleftneedle", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0497F, -6.4989F));

		PartDefinition cube_r3 = grasperleftneedle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -9.0F, -8.0F, 0.0F, 10.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 9.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r4 = grasperleftneedle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(70, 0).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition grasperright = main.addOrReplaceChild("grasperright", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -19.8486F, -2.6808F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r5 = grasperright.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(70, 71).mirror().addBox(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.4098F, -6.8117F, 0.6109F, 0.0F, 0.0F));

		PartDefinition grasperrightlower = grasperright.addOrReplaceChild("grasperrightlower", CubeListBuilder.create(), PartPose.offset(0.0F, 6.4435F, -6.9359F));

		PartDefinition cube_r6 = grasperrightlower.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(70, 17).mirror().addBox(-1.5F, -5.0F, -4.0F, 4.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0337F, -6.8759F, -0.9076F, 0.0F, 0.0F));

		PartDefinition grasperrightneedle = grasperrightlower.addOrReplaceChild("grasperrightneedle", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0497F, -6.4989F));

		PartDefinition cube_r7 = grasperrightneedle.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(1.0F, -9.0F, -8.0F, 0.0F, 10.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 9.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r8 = grasperrightneedle.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(70, 0).mirror().addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 4.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition legleft1 = main.addOrReplaceChild("legleft1", CubeListBuilder.create(), PartPose.offset(3.1176F, -7.2067F, 2.5F));

		PartDefinition cube_r9 = legleft1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(70, 30).addBox(-5.0F, 0.0F, -2.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8824F, 0.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower = legleft1.addOrReplaceChild("left1lower", CubeListBuilder.create(), PartPose.offset(5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r10 = left1lower.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(36, 71).addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.322F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike = left1lower.addOrReplaceChild("left1spike", CubeListBuilder.create().texOffs(20, 83).addBox(-0.5F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(8.1266F, -9.5F, 0.0F));

		PartDefinition legright1 = main.addOrReplaceChild("legright1", CubeListBuilder.create(), PartPose.offset(-3.1176F, -7.2067F, 2.5F));

		PartDefinition cube_r11 = legright1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(70, 30).mirror().addBox(-6.0F, 0.0F, -2.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.8824F, 0.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower = legright1.addOrReplaceChild("right1lower", CubeListBuilder.create(), PartPose.offset(-5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r12 = right1lower.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(36, 71).mirror().addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.322F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike = right1lower.addOrReplaceChild("right1spike", CubeListBuilder.create().texOffs(20, 83).mirror().addBox(-5.5F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.1266F, -9.5F, 0.0F));

		PartDefinition legleft2 = main.addOrReplaceChild("legleft2", CubeListBuilder.create(), PartPose.offset(3.1176F, -7.2067F, 13.5F));

		PartDefinition cube_r13 = legleft2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(70, 30).addBox(-5.0F, 0.0F, -2.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8824F, 0.2067F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition left1lower2 = legleft2.addOrReplaceChild("left1lower2", CubeListBuilder.create(), PartPose.offset(5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r14 = left1lower2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(36, 71).addBox(-5.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.322F, -2.6429F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition left1spike2 = left1lower2.addOrReplaceChild("left1spike2", CubeListBuilder.create().texOffs(20, 83).addBox(-0.5F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(8.1266F, -9.5F, 0.0F));

		PartDefinition legright2 = main.addOrReplaceChild("legright2", CubeListBuilder.create(), PartPose.offset(-3.1176F, -7.2067F, 13.5F));

		PartDefinition cube_r15 = legright2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(70, 30).mirror().addBox(-6.0F, 0.0F, -2.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.8824F, 0.2067F, 1.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right1lower2 = legright2.addOrReplaceChild("right1lower2", CubeListBuilder.create(), PartPose.offset(-5.7233F, 5.1548F, 0.0F));

		PartDefinition cube_r16 = right1lower2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(36, 71).mirror().addBox(-9.0F, -3.75F, -1.5F, 14.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.322F, -2.6429F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right1spike2 = right1lower2.addOrReplaceChild("right1spike2", CubeListBuilder.create().texOffs(20, 83).mirror().addBox(-5.5F, -7.0F, 0.0F, 6.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.1266F, -9.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
		this.animate(t.idle,VoidScytheAnim.idle,v2);

		if (v<0.00001F){
			this.truemain.getAllParts().forEach(ModelPart::resetPose);
			if (!t.counter.prepare().isStarted() && !t.counter.attack().isStarted() && !t.counter.loop().isStarted()){
				this.animateWalk(VoidScytheAnim.movebody,v,v1,1.0f,1.0f);
			}
			this.animateWalk(VoidScytheAnim.movelegs,v,v1,1.0f,1.0f);
		}

		this.animate(t.attack1.prepare(),VoidScytheAnim.chargeattack1,v2);
		this.animate(t.attack1.attack(),VoidScytheAnim.chargedattack1go,v2);
		this.animate(t.attack1.loop(),VoidScytheAnim.chargeattack1loop,v2);


		this.animate(t.attack2.prepare(),VoidScytheAnim.chargeattack2,v2);
		this.animate(t.attack2.attack(),VoidScytheAnim.chargeattack2go,v2);
		this.animate(t.attack2.loop(),VoidScytheAnim.chargeattack2loop,v2);


		this.animate(t.counter.prepare(),VoidScytheAnim.counterstart,v2);
		this.animate(t.counter.attack(),VoidScytheAnim.countergo,v2);
		this.animate(t.counter.loop(),VoidScytheAnim.counterloop,v2);

		this.animate(t.jump.prepare(),VoidScytheAnim.jump,v2);
		this.animate(t.jump.attack(),VoidScytheAnim.jumpland,v2);
		this.animate(t.jump.loop(),VoidScytheAnim.jumploop,v2);
	}

	@Override
	public ModelPart root() {
		return this.truemain;
	}
}