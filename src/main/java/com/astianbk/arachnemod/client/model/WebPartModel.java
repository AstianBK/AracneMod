package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.render_state.ScarabRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class WebPartModel<T extends ScarabRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "web_part"), "main");
	private final ModelPart truemain;
	private final ModelPart main;

	public WebPartModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -20.0F, 0.0F, 5.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-2.5F, -10.0F, -0.25F, 5.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, -10.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.idle.apply(state.idle,state.ageInTicks);
//		this.attack1.apply(state.attack1,state.ageInTicks,0.5F);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.5F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.5F);
//
//		this.Head.yRot = state.yRot * 0.017453292F;
//		this.Head.xRot = state.xRot * 0.017453292F;

	}
}