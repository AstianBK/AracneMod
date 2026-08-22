package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.anim.VoidBeetleAnim;
import com.astianbk.arachnemod.client.render_state.VoidGrubRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class VoidGrubModel<T extends VoidGrubRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "void_grub"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart leftleg1;
	private final ModelPart rightleg1;
	private final ModelPart leftleg3;
	private final ModelPart rightleg3;
	private final ModelPart leftleg2;
	private final ModelPart rightleg2;
	private KeyframeAnimation idle;
	private KeyframeAnimation walk;
	public VoidGrubModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.head = this.main.getChild("head");
		this.body = this.main.getChild("body");
		this.leftleg1 = this.main.getChild("leftleg1");
		this.rightleg1 = this.main.getChild("rightleg1");
		this.leftleg3 = this.main.getChild("leftleg3");
		this.rightleg3 = this.main.getChild("rightleg3");
		this.leftleg2 = this.main.getChild("leftleg2");
		this.rightleg2 = this.main.getChild("rightleg2");
		this.idle = VoidBeetleAnim.idle.bake(root);
		this.walk = VoidBeetleAnim.move.bake(root);

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -1.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -1.0F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(15, 6).addBox(-1.0F, -6.0F, -2.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, -8).addBox(0.5F, -2.0F, -9.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -2.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -0.5F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.5F));

		PartDefinition leftleg1 = main.addOrReplaceChild("leftleg1", CubeListBuilder.create().texOffs(14, 14).addBox(0.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, -1.5F));

		PartDefinition rightleg1 = main.addOrReplaceChild("rightleg1", CubeListBuilder.create().texOffs(14, 14).mirror().addBox(-6.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 1.0F, -1.5F));

		PartDefinition leftleg3 = main.addOrReplaceChild("leftleg3", CubeListBuilder.create().texOffs(14, 14).addBox(0.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 2.5F));

		PartDefinition rightleg3 = main.addOrReplaceChild("rightleg3", CubeListBuilder.create().texOffs(14, 14).mirror().addBox(-6.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 1.0F, 2.5F));

		PartDefinition leftleg2 = main.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(14, 14).addBox(0.0F, -4.0F, -0.5F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 1.0F));

		PartDefinition rightleg2 = main.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(14, 14).mirror().addBox(-6.0F, -4.0F, -0.5F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 1.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.walk.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.idle.apply(state.idle,state.ageInTicks);
//		this.attack1.apply(state.attack1,state.ageInTicks,0.5F);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.5F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.5F);
//
//		this.Head.yRot = state.yRot * 0.017453292F;
//		this.Head.xRot = state.xRot * 0.017453292F;

	}
}