package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.ScarabRenderState;
import com.astianbk.arachnemod.client.VoidHopperRenderState;
import com.astianbk.arachnemod.client.anim.VoidHopperAnim;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class VoidHopperModel<T extends VoidHopperRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "voidhopper"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart Head;
	private final ModelPart eyeshead;
	private final ModelPart Antennas;
	private final ModelPart Left;
	private final ModelPart Right;
	private final ModelPart Torso;
	private final ModelPart UpperChest;
	private final ModelPart SpikesChest;
	private final ModelPart bone1;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart bone7;
	private final ModelPart bone8;
	private final ModelPart bone9;
	private final ModelPart LowerChest;
	private final ModelPart LeftArm;
	private final ModelPart HandLeft;
	private final ModelPart RightArm;
	private final ModelPart HandRight;
	private KeyframeAnimation idle;
	private KeyframeAnimation emerge;
	private KeyframeAnimation flee;
	private KeyframeAnimation casting;
	public VoidHopperModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.Head = this.main.getChild("Head");
		this.eyeshead = this.Head.getChild("eyeshead");
		this.Antennas = this.Head.getChild("Antennas");
		this.Left = this.Antennas.getChild("Left");
		this.Right = this.Antennas.getChild("Right");
		this.Torso = this.main.getChild("Torso");
		this.UpperChest = this.Torso.getChild("UpperChest");
		this.SpikesChest = this.UpperChest.getChild("SpikesChest");
		this.bone1 = this.SpikesChest.getChild("1");
		this.bone2 = this.SpikesChest.getChild("2");
		this.bone3 = this.SpikesChest.getChild("3");
		this.bone4 = this.SpikesChest.getChild("4");
		this.bone5 = this.SpikesChest.getChild("5");
		this.bone6 = this.SpikesChest.getChild("6");
		this.bone7 = this.SpikesChest.getChild("7");
		this.bone8 = this.SpikesChest.getChild("8");
		this.bone9 = this.SpikesChest.getChild("9");
		this.LowerChest = this.Torso.getChild("LowerChest");
		this.LeftArm = this.Torso.getChild("LeftArm");
		this.HandLeft = this.LeftArm.getChild("HandLeft");
		this.RightArm = this.Torso.getChild("RightArm");
		this.HandRight = this.RightArm.getChild("HandRight");
		this.idle = VoidHopperAnim.idle.bake(root);
		this.flee = VoidHopperAnim.flee.bake(root);
		this.emerge = VoidHopperAnim.emerge.bake(root);
		this.casting = VoidHopperAnim.casting.bake(root);

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 30.0F, -0.7972F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = main.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(30, 0).addBox(-4.0F, -3.0F, -5.025F, 8.0F, 15.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(100, 25).addBox(-3.5F, 8.75F, -4.85F, 7.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -26.9749F, -1.7866F));

		PartDefinition eyeshead = Head.addOrReplaceChild("eyeshead", CubeListBuilder.create().texOffs(90, 28).mirror().addBox(-3.725F, -13.0F, -5.85F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(90, 28).addBox(0.725F, -13.0F, -5.85F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 1.0838F));

		PartDefinition Antennas = Head.addOrReplaceChild("Antennas", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, -4.0F));

		PartDefinition Left = Antennas.addOrReplaceChild("Left", CubeListBuilder.create().texOffs(0, -15).mirror().addBox(0.0F, -6.5F, -13.5F, 0.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, -14.0F, 0.5F));

		PartDefinition Right = Antennas.addOrReplaceChild("Right", CubeListBuilder.create().texOffs(0, -15).addBox(0.0F, -6.5F, -13.5F, 0.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -14.0F, 0.5F));

		PartDefinition Torso = main.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(-0.55F, -5.7002F, 4.153F));

		PartDefinition UpperChest = Torso.addOrReplaceChild("UpperChest", CubeListBuilder.create().texOffs(41, 26).addBox(-6.45F, -10.2747F, -5.9396F, 14.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition SpikesChest = UpperChest.addOrReplaceChild("SpikesChest", CubeListBuilder.create(), PartPose.offset(0.55F, -12.2747F, -0.9396F));

		PartDefinition bone1 = SpikesChest.addOrReplaceChild("1", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.15F, 0.85F, -1.0F, -0.2618F, 0.0F, 0.1745F));

		PartDefinition spike_r1 = bone1.addOrReplaceChild("spike_r1", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bone2 = SpikesChest.addOrReplaceChild("2", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.85F, 1.275F, 1.975F, -0.3054F, -0.6981F, 0.0F));

		PartDefinition spike_r2 = bone2.addOrReplaceChild("spike_r2", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bone3 = SpikesChest.addOrReplaceChild("3", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.15F, 1.275F, -3.025F, 0.4655F, -0.5792F, -0.4104F));

		PartDefinition spike_r3 = bone3.addOrReplaceChild("spike_r3", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bone4 = SpikesChest.addOrReplaceChild("4", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.15F, 1.8F, 3.425F, -0.5214F, -0.3843F, 0.2121F));

		PartDefinition spike_r4 = bone4.addOrReplaceChild("spike_r4", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8287F, 2.0511F, -3.1536F, 0.4193F, 0.3881F, -0.0215F));

		PartDefinition spike_r5 = bone4.addOrReplaceChild("spike_r5", CubeListBuilder.create().texOffs(104, 38).addBox(4.5F, -1.775F, 5.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4009F, 3.6239F, -2.4989F, 1.0535F, 1.1223F, 0.819F));

		PartDefinition spike_r6 = bone4.addOrReplaceChild("spike_r6", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, 0.925F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, 0.3873F, 0.0665F, -0.1615F));

		PartDefinition spike_r7 = bone4.addOrReplaceChild("spike_r7", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bone5 = SpikesChest.addOrReplaceChild("5", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.15F, 4.15F, 4.55F, -0.9599F, 0.0F, 0.0F));

		PartDefinition spike_r8 = bone5.addOrReplaceChild("spike_r8", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bone6 = SpikesChest.addOrReplaceChild("6", CubeListBuilder.create().texOffs(104, 38).mirror().addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.15F, 7.525F, 4.55F, -2.0033F, 0.1619F, 0.7076F));

		PartDefinition spike_r9 = bone6.addOrReplaceChild("spike_r9", CubeListBuilder.create().texOffs(104, 38).mirror().addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition bone7 = SpikesChest.addOrReplaceChild("7", CubeListBuilder.create().texOffs(104, 38).mirror().addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.85F, 9.05F, 4.55F, -1.4746F, 0.4346F, 0.0406F));

		PartDefinition spike_r10 = bone7.addOrReplaceChild("spike_r10", CubeListBuilder.create().texOffs(104, 38).mirror().addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition bone8 = SpikesChest.addOrReplaceChild("8", CubeListBuilder.create().texOffs(104, 38).mirror().addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.2F, 5.5F, 1.975F, 1.1606F, -1.4733F, -2.0352F));

		PartDefinition spike_r11 = bone8.addOrReplaceChild("spike_r11", CubeListBuilder.create().texOffs(104, 38).mirror().addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition bone9 = SpikesChest.addOrReplaceChild("9", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.2F, 5.5F, 1.975F, -0.0852F, 1.3005F, 2.1876F));

		PartDefinition spike_r12 = bone9.addOrReplaceChild("spike_r12", CubeListBuilder.create().texOffs(104, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition LowerChest = Torso.addOrReplaceChild("LowerChest", CubeListBuilder.create().texOffs(88, 2).addBox(-5.0F, -12.5F, -4.0F, 10.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.55F, 0.2253F, -0.9396F));

		PartDefinition LeftArm = Torso.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(0.0F, -1.0F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.55F, -18.2747F, -2.9396F));

		PartDefinition HandLeft = LeftArm.addOrReplaceChild("HandLeft", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2.5F, -2.5F, -20.0F, 5.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 15.5F, -2.0F));

		PartDefinition RightArm = Torso.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(56, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.45F, -18.2747F, -2.9396F));

		PartDefinition HandRight = RightArm.addOrReplaceChild("HandRight", CubeListBuilder.create().texOffs(0, 10).addBox(-2.5F, -2.5F, -20.0F, 5.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 15.5F, -2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);

//		this.walkBody.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
//		this.walkLegs.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed,2.0F,1.0F);
		this.idle.apply(state.idle,state.ageInTicks);
		this.flee.apply(state.flee,state.ageInTicks);
		this.emerge.apply(state.emerge,state.ageInTicks);
		this.casting.apply(state.casting,state.ageInTicks);
//		this.attack1.apply(state.attack1,state.ageInTicks,0.5F);
//		this.attack2.apply(state.attack2,state.ageInTicks,0.5F);
//
//		this.bite.apply(state.bite,state.ageInTicks,0.5F);

		this.Head.yRot = state.yRot * 0.017453292F;
		this.Head.xRot = state.xRot * 0.017453292F;
	}
}