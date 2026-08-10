package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.ScarabRenderState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;

public class ScarabPlayerModel<T extends AvatarRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "scarab_entity"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart Head;
	private final ModelPart NerubianHelmet;
	private final ModelPart Spike;
	private final ModelPart Pincers;
	private final ModelPart Right;
	private final ModelPart Left;
	private final ModelPart fangs;
	private final ModelPart fang1;
	private final ModelPart fang2;
	private final ModelPart Torso;
	private final ModelPart UpperChest;
	private final ModelPart NerubianChestplate;
	private final ModelPart Chest;
	private final ModelPart RightArm;
	private final ModelPart RightUpper;
	private final ModelPart RightNerubianArmor;
	private final ModelPart RightLower;
	private final ModelPart RightLowerArmor;
	private final ModelPart RightHand;
	private final ModelPart LeftArm;
	private final ModelPart LeftUpper;
	private final ModelPart LeftNerubianArmor;
	private final ModelPart LeftLower;
	private final ModelPart LeftLowerArmor;
	private final ModelPart LeftHand;
	private final ModelPart torsobehind;
	private final ModelPart RightBackLeg;
	private final ModelPart SectionBackRight;
	private final ModelPart LeftBackLeg;
	private final ModelPart SectionBackLeft;
	private final ModelPart LeftFrontLeg;
	private final ModelPart SectionFrontLeft;
	private final ModelPart RightFrontLeg;
	private final ModelPart SectionFrontRight;
	private KeyframeAnimation walkLegs;
	private KeyframeAnimation walkBody;
	private KeyframeAnimation attack1;
	private KeyframeAnimation attack2;
	private KeyframeAnimation bite;
	private KeyframeAnimation idle;
	public ScarabPlayerModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.Head = this.main.getChild("Head");
		this.NerubianHelmet = this.Head.getChild("NerubianHelmet");
		this.Spike = this.Head.getChild("Spike");
		this.Pincers = this.Head.getChild("Pincers");
		this.Right = this.Pincers.getChild("Right");
		this.Left = this.Pincers.getChild("Left");
		this.fangs = this.Head.getChild("fangs");
		this.fang1 = this.fangs.getChild("fang1");
		this.fang2 = this.fangs.getChild("fang2");
		this.Torso = this.main.getChild("Torso");
		this.UpperChest = this.Torso.getChild("UpperChest");
		this.NerubianChestplate = this.UpperChest.getChild("NerubianChestplate");
		this.Chest = this.UpperChest.getChild("Chest");
		this.RightArm = this.UpperChest.getChild("RightArm");
		this.RightUpper = this.RightArm.getChild("RightUpper");
		this.RightNerubianArmor = this.RightUpper.getChild("RightNerubianArmor");
		this.RightLower = this.RightArm.getChild("RightLower");
		this.RightLowerArmor = this.RightLower.getChild("RightLowerArmor");
		this.RightHand = this.RightLower.getChild("RightHand");
		this.LeftArm = this.UpperChest.getChild("LeftArm");
		this.LeftUpper = this.LeftArm.getChild("LeftUpper");
		this.LeftNerubianArmor = this.LeftUpper.getChild("LeftNerubianArmor");
		this.LeftLower = this.LeftArm.getChild("LeftLower");
		this.LeftLowerArmor = this.LeftLower.getChild("LeftLowerArmor");
		this.LeftHand = this.LeftLower.getChild("LeftHand");
		this.torsobehind = this.Torso.getChild("torsobehind");
		this.RightBackLeg = this.main.getChild("RightBackLeg");
		this.SectionBackRight = this.RightBackLeg.getChild("SectionBackRight");
		this.LeftBackLeg = this.main.getChild("LeftBackLeg");
		this.SectionBackLeft = this.LeftBackLeg.getChild("SectionBackLeft");
		this.LeftFrontLeg = this.main.getChild("LeftFrontLeg");
		this.SectionFrontLeft = this.LeftFrontLeg.getChild("SectionFrontLeft");
		this.RightFrontLeg = this.main.getChild("RightFrontLeg");
		this.SectionFrontRight = this.RightFrontLeg.getChild("SectionFrontRight");
		this.idle = ScarabAnimation.idle.bake(root);
		this.attack1= ScarabAnimation.attackclaw1.bake(root);
		this.attack2 = ScarabAnimation.attackclaw2.bake(root);

		this.bite = ScarabAnimation.attackspit.bake(root);
		this.walkBody = ScarabAnimation.move1.bake(root);
		this.walkLegs = ScarabAnimation.move1legs.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 6.2028F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = main.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(38, 72).addBox(-4.0F, -6.553F, -5.0405F, 8.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(51, 72).addBox(-4.75F, -6.5029F, -4.9905F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(51, 72).mirror().addBox(3.75F, -6.5029F, -4.9905F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 66).addBox(-5.0F, -6.678F, -6.0405F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -28.3219F, 3.2539F));

		PartDefinition NerubianHelmet = Head.addOrReplaceChild("NerubianHelmet", CubeListBuilder.create().texOffs(66, 84).addBox(-5.0F, -6.653F, -6.0405F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Spike = Head.addOrReplaceChild("Spike", CubeListBuilder.create(), PartPose.offset(0.0F, -0.7031F, 8.2461F));

		PartDefinition cube_r1 = Spike.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(59, 32).addBox(0.1745F, -3.4139F, -17.6267F, 0.0F, 9.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, -4.0F, 2.0071F, -0.0436F, -3.1416F));

		PartDefinition Pincers = Head.addOrReplaceChild("Pincers", CubeListBuilder.create(), PartPose.offset(0.0F, -1.6174F, -4.7287F));

		PartDefinition Right = Pincers.addOrReplaceChild("Right", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.0F, -0.5F));

		PartDefinition PRight_r1 = Right.addOrReplaceChild("PRight_r1", CubeListBuilder.create().texOffs(18, 53).addBox(-3.0F, -3.0F, 0.0F, 7.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0607F, -0.0251F, -1.5708F, 0.0F, 0.0F));

		PartDefinition Left = Pincers.addOrReplaceChild("Left", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -0.5F));

		PartDefinition PLeft_r1 = Left.addOrReplaceChild("PLeft_r1", CubeListBuilder.create().texOffs(18, 53).mirror().addBox(-4.0F, -3.0F, 0.0F, 7.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0607F, -0.0251F, -1.5708F, 0.0F, 0.0F));

		PartDefinition fangs = Head.addOrReplaceChild("fangs", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.6781F, -6.1039F));

		PartDefinition fang1 = fangs.addOrReplaceChild("fang1", CubeListBuilder.create().texOffs(31, 60).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.0F));

		PartDefinition fang2 = fangs.addOrReplaceChild("fang2", CubeListBuilder.create().texOffs(31, 60).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -1.5F, 1.0F));

		PartDefinition Torso = main.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(57, 1).addBox(-4.45F, 0.6649F, 2.7107F, 10.0F, 5.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.55F, -10.7002F, 4.153F));

		PartDefinition TorsoLower_r1 = Torso.addOrReplaceChild("TorsoLower_r1", CubeListBuilder.create().texOffs(44, 34).addBox(-3.0F, -3.5F, -3.5F, 10.0F, 7.0F, 8.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-1.45F, 3.2002F, -0.653F, 0.2618F, 0.0F, 0.0F));

		PartDefinition UpperChest = Torso.addOrReplaceChild("UpperChest", CubeListBuilder.create().texOffs(74, 26).addBox(-4.45F, -7.2747F, -4.9396F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition NerubianChestplate = UpperChest.addOrReplaceChild("NerubianChestplate", CubeListBuilder.create().texOffs(0, 82).addBox(-6.45F, -17.2747F, -4.9396F, 14.0F, 10.0F, 8.0F, new CubeDeformation(0.25F))
		.texOffs(0, 100).addBox(-4.45F, -6.7747F, -4.9396F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Chest = UpperChest.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(0, 34).addBox(-7.0F, -10.0F, -4.0F, 14.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.55F, -7.2747F, -0.9395F));

		PartDefinition RightArm = UpperChest.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offset(-6.55F, -15.3703F, -1.0745F));

		PartDefinition RightUpper = RightArm.addOrReplaceChild("RightUpper", CubeListBuilder.create().texOffs(104, 36).addBox(-4.9F, -1.9044F, -2.8651F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightNerubianArmor = RightUpper.addOrReplaceChild("RightNerubianArmor", CubeListBuilder.create().texOffs(58, 100).mirror().addBox(-4.9F, 5.5956F, -2.8651F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(44, 87).addBox(-4.9F, -1.9044F, -2.8651F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightLower = RightArm.addOrReplaceChild("RightLower", CubeListBuilder.create(), PartPose.offset(-2.0F, 5.0F, 0.0F));

		PartDefinition RightForeArm_r1 = RightLower.addOrReplaceChild("RightForeArm_r1", CubeListBuilder.create().texOffs(7, 7).addBox(-2.0F, -2.5F, 0.25F, 4.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -0.1544F, -0.3651F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightLowerArmor = RightLower.addOrReplaceChild("RightLowerArmor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightForeArm_r2 = RightLowerArmor.addOrReplaceChild("RightForeArm_r2", CubeListBuilder.create().texOffs(36, 105).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.1F, 2.5956F, -8.3651F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightHand = RightLower.addOrReplaceChild("RightHand", CubeListBuilder.create(), PartPose.offset(0.1F, 2.3196F, -10.2566F));

		PartDefinition RightClaw_r1 = RightHand.addOrReplaceChild("RightClaw_r1", CubeListBuilder.create().texOffs(0, -10).addBox(-1.0F, -3.5F, 0.5F, 0.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, -10).addBox(1.9063F, -3.5F, 0.0774F, 0.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.02F, 1.0717F, -1.8081F, -3.1416F, 0.4363F, -1.5708F));

		PartDefinition RightHand_r1 = RightHand.addOrReplaceChild("RightHand_r1", CubeListBuilder.create().texOffs(96, 0).addBox(-1.5153F, -1.975F, -2.2295F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.04F)), PartPose.offsetAndRotation(-0.015F, 1.0233F, -1.5984F, -3.1416F, 0.4363F, -1.5708F));

		PartDefinition LeftArm = UpperChest.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(7.65F, -15.3703F, -1.0745F));

		PartDefinition LeftUpper = LeftArm.addOrReplaceChild("LeftUpper", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftNerubianArmor = LeftUpper.addOrReplaceChild("LeftNerubianArmor", CubeListBuilder.create().texOffs(44, 87).mirror().addBox(-0.1F, -1.9044F, -2.8651F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(58, 100).addBox(-0.1F, 5.5956F, -2.8651F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftLower = LeftArm.addOrReplaceChild("LeftLower", CubeListBuilder.create(), PartPose.offset(2.0F, 5.0F, 0.0F));

		PartDefinition LeftForeArm_r1 = LeftLower.addOrReplaceChild("LeftForeArm_r1", CubeListBuilder.create().texOffs(7, 7).mirror().addBox(-2.0F, -2.5F, 0.25F, 4.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1F, -0.1544F, -0.3651F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftLowerArmor = LeftLower.addOrReplaceChild("LeftLowerArmor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftForeArm_r2 = LeftLowerArmor.addOrReplaceChild("LeftForeArm_r2", CubeListBuilder.create().texOffs(36, 105).mirror().addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-0.1F, 2.5956F, -8.3651F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftHand = LeftLower.addOrReplaceChild("LeftHand", CubeListBuilder.create(), PartPose.offset(-0.1F, 2.2696F, -10.2566F));

		PartDefinition LeftHand_r1 = LeftHand.addOrReplaceChild("LeftHand_r1", CubeListBuilder.create().texOffs(96, 0).mirror().addBox(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.04F)).mirror(false), PartPose.offsetAndRotation(0.54F, 0.7483F, -2.0484F, 3.1416F, -0.4363F, 1.5708F));

		PartDefinition LeftClaw_r1 = LeftHand.addOrReplaceChild("LeftClaw_r1", CubeListBuilder.create().texOffs(0, -10).mirror().addBox(1.0F, -3.5F, 0.5F, 0.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, -10).mirror().addBox(-1.9063F, -3.5F, 0.0774F, 0.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.02F, 1.1217F, -1.8081F, -3.1416F, -0.4363F, 1.5708F));

		PartDefinition torsobehind = Torso.addOrReplaceChild("torsobehind", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -4.5F, -0.5F, 16.0F, 9.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.55F, -3.8351F, 3.2107F));

		PartDefinition RightBackLeg = main.addOrReplaceChild("RightBackLeg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.5239F, -3.7321F, -9.254F, 4.0F, 4.0F, 10.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-5.3093F, -5.5763F, 10.3697F, 0.0F, 2.5307F, 0.0F));

		PartDefinition rightbackleg_r1 = RightBackLeg.addOrReplaceChild("rightbackleg_r1", CubeListBuilder.create().texOffs(96, 10).addBox(-3.0F, -6.0F, -2.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4761F, 0.0F, -13.7181F, -0.5236F, 0.0F, 0.0F));

		PartDefinition SectionBackRight = RightBackLeg.addOrReplaceChild("SectionBackRight", CubeListBuilder.create().texOffs(64, 12).addBox(-1.9833F, 4.3274F, -6.1715F, 4.0F, 6.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offset(-0.4906F, -4.7511F, -13.2238F));

		PartDefinition rightbacklegsection_r1 = SectionBackRight.addOrReplaceChild("rightbacklegsection_r1", CubeListBuilder.create().texOffs(64, 4).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0167F, 10.0267F, -5.1253F, -0.3054F, 0.0F, 0.0F));

		PartDefinition rightbacklegsection_r2 = SectionBackRight.addOrReplaceChild("rightbacklegsection_r2", CubeListBuilder.create().texOffs(39, 49).addBox(-4.0F, -10.0F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4667F, 4.8274F, -5.1715F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftBackLeg = main.addOrReplaceChild("LeftBackLeg", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.4761F, -3.7321F, -9.254F, 4.0F, 4.0F, 10.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(5.3093F, -5.5763F, 10.3697F, 0.0F, -2.5307F, 0.0F));

		PartDefinition leftbackleg_r1 = LeftBackLeg.addOrReplaceChild("leftbackleg_r1", CubeListBuilder.create().texOffs(96, 10).mirror().addBox(-1.0F, -6.0F, -2.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4761F, 0.0F, -13.7181F, -0.5236F, 0.0F, 0.0F));

		PartDefinition SectionBackLeft = LeftBackLeg.addOrReplaceChild("SectionBackLeft", CubeListBuilder.create().texOffs(64, 12).mirror().addBox(-2.0167F, 4.3274F, -6.1715F, 4.0F, 6.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offset(0.4906F, -4.7511F, -13.2238F));

		PartDefinition leftbacklegsection_r1 = SectionBackLeft.addOrReplaceChild("leftbacklegsection_r1", CubeListBuilder.create().texOffs(64, 4).mirror().addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0167F, 10.0267F, -5.1253F, -0.3054F, 0.0F, 0.0F));

		PartDefinition leftbacklegsection_r2 = SectionBackLeft.addOrReplaceChild("leftbacklegsection_r2", CubeListBuilder.create().texOffs(39, 49).mirror().addBox(-1.0F, -10.0F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.4667F, 4.8274F, -5.1715F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftFrontLeg = main.addOrReplaceChild("LeftFrontLeg", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(0.9686F, -3.7321F, -8.5575F, 4.0F, 4.0F, 10.0F, new CubeDeformation(-0.001F)).mirror(false)
		.texOffs(92, 52).addBox(-1.9069F, -1.7321F, -8.7255F, 3.0F, 0.0F, 10.0F, new CubeDeformation(-0.001F))
		.texOffs(92, 52).mirror().addBox(4.974F, -1.7321F, -8.7255F, 3.0F, 0.0F, 10.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(4.1343F, -5.5763F, 10.3697F, 0.0F, -0.4363F, 0.0F));

		PartDefinition rightfrontleg_r1 = LeftFrontLeg.addOrReplaceChild("rightfrontleg_r1", CubeListBuilder.create().texOffs(103, 52).addBox(-3.828F, -1.1F, -0.05F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(103, 52).mirror().addBox(2.128F, -1.1F, -0.05F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.8051F, -2.9766F, -13.1566F, -0.5236F, 0.0F, 0.0F));

		PartDefinition rightfrontleg_r2 = LeftFrontLeg.addOrReplaceChild("rightfrontleg_r2", CubeListBuilder.create().texOffs(103, 52).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2841F, -4.6907F, -9.6385F, -0.0503F, 0.5214F, 1.4701F));

		PartDefinition leftfrontleg_r1 = LeftFrontLeg.addOrReplaceChild("leftfrontleg_r1", CubeListBuilder.create().texOffs(96, 10).mirror().addBox(-1.0F, -6.0F, -2.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.9686F, 0.0F, -13.0216F, -0.5236F, 0.0F, 0.0F));

		PartDefinition SectionFrontLeft = LeftFrontLeg.addOrReplaceChild("SectionFrontLeft", CubeListBuilder.create().texOffs(64, 12).mirror().addBox(-2.0167F, 5.3274F, -7.1715F, 4.0F, 6.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offset(2.9353F, -5.7511F, -11.5273F));

		PartDefinition sectionfrontleg_r1 = SectionFrontLeft.addOrReplaceChild("sectionfrontleg_r1", CubeListBuilder.create().texOffs(64, 4).mirror().addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0167F, 11.0267F, -6.1253F, -0.3054F, 0.0F, 0.0F));

		PartDefinition sectionfrontleg_r2 = SectionFrontLeft.addOrReplaceChild("sectionfrontleg_r2", CubeListBuilder.create().texOffs(39, 49).mirror().addBox(-1.0F, -10.0F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.4667F, 5.8274F, -6.1715F, -0.4363F, 0.0F, 0.0F));

		PartDefinition RightFrontLeg = main.addOrReplaceChild("RightFrontLeg", CubeListBuilder.create().texOffs(0, 52).addBox(-1.1312F, -3.7321F, -9.0084F, 4.0F, 4.0F, 10.0F, new CubeDeformation(-0.001F))
		.texOffs(92, 52).addBox(-4.1312F, -1.7321F, -9.0084F, 3.0F, 0.0F, 10.0F, new CubeDeformation(-0.001F))
		.texOffs(92, 52).mirror().addBox(2.7498F, -1.7321F, -9.0084F, 3.0F, 0.0F, 10.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(-7.1343F, -5.5763F, 12.3697F, 0.0F, 0.4363F, 0.0F));

		PartDefinition rightfrontleg_r3 = RightFrontLeg.addOrReplaceChild("rightfrontleg_r3", CubeListBuilder.create().texOffs(103, 52).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5527F, -4.6907F, -9.6385F, -0.0503F, 0.5214F, 1.4701F));

		PartDefinition leftfrontleg_r2 = RightFrontLeg.addOrReplaceChild("leftfrontleg_r2", CubeListBuilder.create().texOffs(103, 52).mirror().addBox(-3.575F, -4.0F, -1.525F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(103, 52).addBox(-9.531F, -4.0F, -1.525F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(96, 10).addBox(-7.531F, -6.0F, -2.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.3998F, 0.0F, -13.4725F, -0.5236F, 0.0F, 0.0F));

		PartDefinition SectionFrontRight = RightFrontLeg.addOrReplaceChild("SectionFrontRight", CubeListBuilder.create().texOffs(64, 12).addBox(-1.9833F, 5.3274F, -7.1715F, 4.0F, 6.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.9021F, -5.7511F, -11.9782F));

		PartDefinition sectionfrontleg_r3 = SectionFrontRight.addOrReplaceChild("sectionfrontleg_r3", CubeListBuilder.create().texOffs(64, 4).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0167F, 11.0267F, -6.1253F, -0.3054F, 0.0F, 0.0F));

		PartDefinition sectionfrontleg_r4 = SectionFrontRight.addOrReplaceChild("sectionfrontleg_r4", CubeListBuilder.create().texOffs(39, 49).addBox(-4.0F, -10.0F, -2.0F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4667F, 5.8274F, -6.1715F, -0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.idle.apply(state.idle,state.ageInTicks);
//		this.attack1.apply(state.attack1,state.ageInTicks,0.75F);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.75F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.75F);
	}
}