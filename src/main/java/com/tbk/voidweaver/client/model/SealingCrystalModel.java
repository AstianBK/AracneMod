package com.tbk.voidweaver.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.anim.SealingCrystalAnim;
import com.tbk.voidweaver.server.entity.SealingCrystalEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SealingCrystalModel<T extends SealingCrystalEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "sealing_crystal"), "main");
	private final ModelPart truemain;
	private final ModelPart base;
	private final ModelPart crystal;
	private final ModelPart outer_glass;
	private final ModelPart inner_glass;
	private final ModelPart inside;

	public SealingCrystalModel(ModelPart root) {
        super();
        this.truemain = root.getChild("truemain");
		this.base = this.truemain.getChild("base");
		this.crystal = this.truemain.getChild("crystal");
		this.outer_glass = this.crystal.getChild("outer_glass");
		this.inner_glass = this.crystal.getChild("inner_glass");
		this.inside = this.crystal.getChild("inside");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition base = truemain.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition crystal = truemain.addOrReplaceChild("crystal", CubeListBuilder.create(), PartPose.offset(0.0F, -1.9167F, 0.0F));

		PartDefinition outer_glass = crystal.addOrReplaceChild("outer_glass", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1667F, 0.0F));

		PartDefinition outer_glass_r1 = outer_glass.addOrReplaceChild("outer_glass_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition inner_glass = crystal.addOrReplaceChild("inner_glass", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1667F, 0.0F));

		PartDefinition inner_glass_r1 = inner_glass.addOrReplaceChild("inner_glass_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition inside = crystal.addOrReplaceChild("inside", CubeListBuilder.create().texOffs(32, 2).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.0833F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}


	@Override
	public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
		this.animate(t.idle,SealingCrystalAnim.idle,v2);
	}

	@Override
	public ModelPart root() {
		return this.truemain;
	}
}