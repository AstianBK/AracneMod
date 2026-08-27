package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.anim.ShieldCocoonAnim;
import com.astianbk.arachnemod.client.render_state.SpiderAvatarRenderState;
import com.astianbk.arachnemod.client.render_state.VoidGrubRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class ShieldCocoonModel<T extends SpiderAvatarRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "shield_cocoon"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final KeyframeAnimation idle;
	public ShieldCocoonModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.idle = ShieldCocoonAnim.idle.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -30.75F, -9.0F, 18.0F, 28.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(54, 1).addBox(-6.0F, -33.75F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.25F, 0.0F));

		PartDefinition cube_r1 = main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(54, 1).addBox(-6.0F, -1.5F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.25F, 0.0F, 0.0F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
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