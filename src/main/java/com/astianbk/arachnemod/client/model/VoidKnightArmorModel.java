package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.AracneModClient;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class VoidKnightArmorModel<T extends AvatarRenderState> extends HumanoidModel<T> {
	public static final ModelLayerLocation HELMET_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "voidknightarmorhelmet"), "main");
	public static final ModelLayerLocation LEGGINGS_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "voidknightarmorleggings"), "main");
	public static final ModelLayerLocation CHESTPLATE_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "voidknightarmorchestplate"), "main");
	public static final ModelLayerLocation ALL_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "voidknightarmor"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart cape;
	private final ModelPart left_arm;
	private final ModelPart gauntlet;
	private final ModelPart right_arm;
	private final ModelPart gauntlet2;
	private final ModelPart left_leg;
	private final ModelPart left_boot;
	private final ModelPart right_leg;
	private final ModelPart right_boot;
	public VoidKnightArmorModel(ModelPart root) {
        super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.cape = this.body.getChild("cape");
		this.left_arm = root.getChild("left_arm");
		this.gauntlet = this.left_arm.getChild("gauntlet");
		this.right_arm = root.getChild("right_arm");
		this.gauntlet2 = this.right_arm.getChild("gauntlet2");

		this.left_leg = root.getChild("left_leg");
		this.left_boot = this.left_leg.getChild("left_boot");
		this.right_leg = root.getChild("right_leg");
		this.right_boot = this.right_leg.getChild("right_boot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition main = meshdefinition.getRoot();

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 1).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.01F))
				.texOffs(0, 7).addBox(-3.0F, -8.0F, -4.0F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(37, 0).addBox(-6.0F, -8.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.0F, -8.0F, -2.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(37, 0).mirror().addBox(4.0F, -8.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(6.0F, -8.0F, -2.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F).extend(0.5F)), PartPose.ZERO);

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.1F))
				.texOffs(38, 32).addBox(-6.0F, -0.85F, -3.15F, 12.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 32).addBox(-6.0F, 2.15F, -3.15F, 12.0F, 13.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 50).addBox(-5.0F, 8.0F, -3.0F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.02F))
				.texOffs(24, 44).addBox(-2.0F, 8.075F, -3.15F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(32, 45).addBox(-5.0F, 8.675F, -3.0F, 10.0F, 13.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(48, 24).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -3.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(65, 0).addBox(-7.0F, -0.475F, -1.0F, 14.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.125F, 3.5F));

		PartDefinition left_arm = main.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(18, 32).mirror().addBox(-1.075F, -3.675F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 38).mirror().addBox(-1.75F, 3.325F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 31).mirror().addBox(-1.025F, 1.325F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition gauntlet = left_arm.addOrReplaceChild("gauntlet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gauntlet_r1 = gauntlet.addOrReplaceChild("gauntlet_r1", CubeListBuilder.create().texOffs(95, 0).mirror().addBox(-2.5F, -1.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.475F, 10.7F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition right_arm = main.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(18, 32).addBox(-4.925F, -3.675F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.25F, 3.325F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 31).addBox(-3.975F, 1.325F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition gauntlet2 = right_arm.addOrReplaceChild("gauntlet2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gauntlet_r2 = gauntlet2.addOrReplaceChild("gauntlet_r2", CubeListBuilder.create().texOffs(95, 0).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.475F, 10.7F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition left_leg = main.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(45, 4).addBox(-1.85F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(48, 15).addBox(-1.85F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg = main.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(45, 4).mirror().addBox(-2.15F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false)
				.texOffs(94, 9).mirror().addBox(-1.95F, -4.3F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.19F)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(48, 15).mirror().addBox(-2.15F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	public static LayerDefinition createHelmetLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F),0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition main = partdefinition;

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 1).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.01F))
				.texOffs(0, 7).addBox(-3.0F, -8.0F, -4.0F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(37, 0).addBox(-6.0F, -8.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.0F, -8.0F, -2.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(37, 0).mirror().addBox(4.0F, -8.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(6.0F, -8.0F, -2.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0,0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F).extend(0.5F)), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	public void setPartVisibility(VoidKnightArmorModel<?> model, EquipmentSlot slot) {
		model.allParts().forEach(modelPart-> modelPart.visible = false);
		model.root.visible = true;
		switch (slot) {
			case CHEST -> {
				body.visible = true;
				this.rightArm.visible = true;
				this.right_arm.getAllParts().forEach(e->e.visible = true);
				this.leftArm.visible = true;
				this.left_arm.getAllParts().forEach(e->e.visible = true);
				cape.visible = true;
			}
			case HEAD -> {
				head.visible = true;
			}
			case LEGS -> {
				body.visible = true;
				rightLeg.visible = true;
				leftLeg.visible = true;
			}
			case FEET -> {
				rightLeg.visible = true;
				leftLeg.visible = true;
				right_boot.visible = true;
				left_boot.visible = true;
			}
		}
	}
	public static LayerDefinition createChestLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-5.0F, 0.0F, -3.0F, 10.0F, 8.0F, 6.0F, new CubeDeformation(0.1F))
						.texOffs(38, 32)
						.addBox(-6.0F, -0.85F, -3.15F, 12.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(64, 32)
						.addBox(-6.0F, 2.15F, -3.15F, 12.0F, 13.0F, 0.0F, new CubeDeformation(0.0F))
						.texOffs(0, 50)
						.addBox(-5.0F, 8.0F, -3.0F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.02F))
						.texOffs(24, 44)
						.addBox(-2.0F, 8.075F, -3.15F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
						.texOffs(32, 45)
						.addBox(-5.0F, 8.675F, -3.0F, 10.0F, 13.0F, 6.0F, new CubeDeformation(-0.2F)),
				PartPose.ZERO);

		PartDefinition voidChest = body;

		voidChest.addOrReplaceChild(
				"body_r1",
				CubeListBuilder.create()
						.texOffs(48, 24)
						.addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 3.0F, -3.5F, 0.0F, 0.0F, 0.7854F)
		);

		voidChest.addOrReplaceChild(
				"cape",
				CubeListBuilder.create()
						.texOffs(65, 0)
						.addBox(-7.0F, -0.475F, -1.0F, 14.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -1.125F, 3.5F)
		);

		PartDefinition leftArm = partdefinition.addOrReplaceChild(
				"left_arm",
				CubeListBuilder.create()
						.texOffs(0, 31).mirror()
						.addBox(-1.025F, 7.4F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(18, 32).mirror()
						.addBox(-1.075F, -3.675F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(0, 38).mirror()
						.addBox(-1.75F, 3.325F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(0, 31).mirror()
						.addBox(-1.025F, 1.325F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.ZERO
		);

		PartDefinition rightArm = partdefinition.addOrReplaceChild(
				"void_right_arm",
				CubeListBuilder.create()
						.texOffs(18, 32)
						.addBox(-4.925F, -3.675F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 38)
						.addBox(-4.25F, 3.325F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 31)
						.addBox(-3.975F, 1.325F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
						.texOffs(0, 31)
						.addBox(-3.975F, 7.4F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.ZERO
		);

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	public static LayerDefinition createLeggingsLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition leftLeg = partdefinition.getChild("left_leg");
		PartDefinition rightLeg = partdefinition.getChild("right_leg");

		leftLeg.addOrReplaceChild(
				"void_left_leggings",
				CubeListBuilder.create()
						.texOffs(45, 4)
						.addBox(
								-1.85F,
								0.0F,
								-2.0F,
								4.0F,
								7.0F,
								4.0F,
								new CubeDeformation(0.15F)
						),
				PartPose.ZERO
		);

		rightLeg.addOrReplaceChild(
				"void_right_leggings",
				CubeListBuilder.create()
						.texOffs(45, 4)
						.mirror()
						.addBox(
								-2.15F,
								0.0F,
								-2.0F,
								4.0F,
								7.0F,
								4.0F,
								new CubeDeformation(0.15F)
						)
						.mirror(false),
				PartPose.ZERO
		);

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);
	}
}