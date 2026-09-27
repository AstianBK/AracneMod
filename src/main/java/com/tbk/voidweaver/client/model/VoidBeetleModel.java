package com.tbk.voidweaver.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.anim.VoidBeetleAnim;
import com.tbk.voidweaver.server.entity.VoidBeetleEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class VoidBeetleModel<T extends VoidBeetleEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "void_beetle"), "main");
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

	public VoidBeetleModel(ModelPart root) {
        super();
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
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -1.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.5F, 0.75F, -8.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -2.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -1.5F, -2.5F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.5F));

		PartDefinition leftleg1 = main.addOrReplaceChild("leftleg1", CubeListBuilder.create().texOffs(10, 14).addBox(0.0F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, -1.5F));

		PartDefinition rightleg1 = main.addOrReplaceChild("rightleg1", CubeListBuilder.create().texOffs(10, 14).mirror().addBox(-5.0F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 1.0F, -1.5F));

		PartDefinition leftleg3 = main.addOrReplaceChild("leftleg3", CubeListBuilder.create().texOffs(10, 14).addBox(0.0F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 2.5F));

		PartDefinition rightleg3 = main.addOrReplaceChild("rightleg3", CubeListBuilder.create().texOffs(10, 14).mirror().addBox(-5.0F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 1.0F, 2.5F));

		PartDefinition leftleg2 = main.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(10, 14).addBox(0.0F, -4.0F, -0.5F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 1.0F));

		PartDefinition rightleg2 = main.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(10, 14).mirror().addBox(-5.0F, -4.0F, -0.5F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 1.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public ModelPart root() {
		return null;
	}

	@Override
	public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
		this.animateWalk(VoidBeetleAnim.move,v,v1,1.0F,1.0f);
		this.animate(t.idle,VoidBeetleAnim.idle,v2);
	}
}