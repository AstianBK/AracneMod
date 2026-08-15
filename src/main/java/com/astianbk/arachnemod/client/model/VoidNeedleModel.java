package com.astianbk.arachnemod.client.model;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.client.NeedleRenderState;
import com.astianbk.arachnemod.client.anim.VoidNeedleAnim;
import com.astianbk.arachnemod.server.entity.VoidNeedleEntity;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;

public class VoidNeedleModel<T extends NeedleRenderState> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(AracneMod.MODID, "voidneedlemodel"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart body;
	private final ModelPart tailstart;
	private final ModelPart tailtip;
	private final ModelPart horn;
	private final ModelPart rightleg1;
	private final ModelPart lowerightleg1;
	private final ModelPart sectionright1;
	private final ModelPart leftleg1;
	private final ModelPart loweleftleg1;
	private final ModelPart sectionleft1;
	private final ModelPart rightleg3;
	private final ModelPart lowerightleg3;
	private final ModelPart sectionright3;
	private final ModelPart leftleg3;
	private final ModelPart loweleftleg3;
	private final ModelPart sectionleft3;
	private final ModelPart rightwing1;
	private final ModelPart leftwing1;
	private KeyframeAnimation attack1;
	private KeyframeAnimation change;
	private KeyframeAnimation loop_charge;
	private KeyframeAnimation idle;
	private KeyframeAnimation wingsState;
	public VoidNeedleModel(ModelPart root) {
        super(root);
        this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.body = this.main.getChild("body");
		this.tailstart = this.body.getChild("tailstart");
		this.tailtip = this.tailstart.getChild("tailtip");
		this.horn = this.body.getChild("horn");
		this.rightleg1 = this.main.getChild("rightleg1");
		this.lowerightleg1 = this.rightleg1.getChild("lowerightleg1");
		this.sectionright1 = this.lowerightleg1.getChild("sectionright1");
		this.leftleg1 = this.main.getChild("leftleg1");
		this.loweleftleg1 = this.leftleg1.getChild("loweleftleg1");
		this.sectionleft1 = this.loweleftleg1.getChild("sectionleft1");
		this.rightleg3 = this.main.getChild("rightleg3");
		this.lowerightleg3 = this.rightleg3.getChild("lowerightleg3");
		this.sectionright3 = this.lowerightleg3.getChild("sectionright3");
		this.leftleg3 = this.main.getChild("leftleg3");
		this.loweleftleg3 = this.leftleg3.getChild("loweleftleg3");
		this.sectionleft3 = this.loweleftleg3.getChild("sectionleft3");
		this.rightwing1 = this.main.getChild("rightwing1");
		this.leftwing1 = this.main.getChild("leftwing1");
		this.wingsState = VoidNeedleAnim.wingson.bake(root);
		this.idle= VoidNeedleAnim.idle.bake(root);
		this.change = VoidNeedleAnim.startcharge.bake(root);
		this.loop_charge = VoidNeedleAnim.charge.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(22, 14).addBox(-2.5F, -3.0F, -5.0F, 5.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(-2.15F, -2.0F, -5.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).mirror().addBox(2.15F, -2.0F, -5.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -9.0F, 3.0F));

		PartDefinition tailstart = body.addOrReplaceChild("tailstart", CubeListBuilder.create().texOffs(18, 1).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.068F, 4.5229F));

		PartDefinition tailtip = tailstart.addOrReplaceChild("tailtip", CubeListBuilder.create().texOffs(40, -1).mirror().addBox(1.8F, -1.925F, 0.9F, 0.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(40, -1).addBox(-1.8F, -1.925F, 0.9F, 0.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.068F, 4.4771F));

		PartDefinition horn = body.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, -3.0F, -10.0F, 3.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(1.0F, -2.975F, -23.0F, 0.0F, 5.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -11.0F));

		PartDefinition rightleg1 = main.addOrReplaceChild("rightleg1", CubeListBuilder.create().texOffs(28, 10).addBox(-5.0063F, -0.2964F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5063F, -6.2964F, -0.5F));

		PartDefinition lowerightleg1 = rightleg1.addOrReplaceChild("lowerightleg1", CubeListBuilder.create().texOffs(44, 3).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5853F, -0.0235F, 0.0F));

		PartDefinition sectionright1 = lowerightleg1.addOrReplaceChild("sectionright1", CubeListBuilder.create().texOffs(30, 3).addBox(-7.0F, -0.5F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 0.5F, 0.0F));

		PartDefinition leftleg1 = main.addOrReplaceChild("leftleg1", CubeListBuilder.create().texOffs(28, 10).mirror().addBox(0.0063F, -0.2964F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5063F, -6.2964F, 1.75F));

		PartDefinition loweleftleg1 = leftleg1.addOrReplaceChild("loweleftleg1", CubeListBuilder.create().texOffs(44, 3).mirror().addBox(0.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.5853F, -0.0235F, 0.0F));

		PartDefinition sectionleft1 = loweleftleg1.addOrReplaceChild("sectionleft1", CubeListBuilder.create().texOffs(30, 3).mirror().addBox(0.0F, -0.5F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, 0.5F, 0.0F));

		PartDefinition rightleg3 = main.addOrReplaceChild("rightleg3", CubeListBuilder.create().texOffs(28, 10).addBox(-5.0063F, -0.2964F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5063F, -6.2964F, 4.5F));

		PartDefinition lowerightleg3 = rightleg3.addOrReplaceChild("lowerightleg3", CubeListBuilder.create().texOffs(44, 3).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5853F, -0.0235F, 0.0F));

		PartDefinition sectionright3 = lowerightleg3.addOrReplaceChild("sectionright3", CubeListBuilder.create().texOffs(30, 3).addBox(-7.0F, -0.5F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 0.5F, 0.0F));

		PartDefinition leftleg3 = main.addOrReplaceChild("leftleg3", CubeListBuilder.create().texOffs(28, 10).mirror().addBox(0.0063F, -0.2964F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5063F, -6.2964F, 6.75F));

		PartDefinition loweleftleg3 = leftleg3.addOrReplaceChild("loweleftleg3", CubeListBuilder.create().texOffs(44, 3).mirror().addBox(0.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.5853F, -0.0235F, 0.0F));

		PartDefinition sectionleft3 = loweleftleg3.addOrReplaceChild("sectionleft3", CubeListBuilder.create().texOffs(30, 3).mirror().addBox(0.0F, -0.5F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, 0.5F, 0.0F));

		PartDefinition rightwing1 = main.addOrReplaceChild("rightwing1", CubeListBuilder.create().texOffs(13, 30).addBox(-1.5F, 0.0F, 0.0F, 10.0F, 0.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -12.0F, -1.75F));

		PartDefinition leftwing1 = main.addOrReplaceChild("leftwing1", CubeListBuilder.create().texOffs(13, 30).mirror().addBox(-8.5F, 0.0F, 0.0F, 10.0F, 0.0F, 25.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -12.0F, -1.75F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);
		this.change.apply(state.change,state.ageInTicks,1.0F);
		this.idle.apply(state.idle,state.ageInTicks,1.0F);
		this.loop_charge.apply(state.loop_charge,state.ageInTicks);
		this.wingsState.apply(state.idle,state.ageInTicks,1.0F);

		this.truemain.yRot = state.yRot* 0.017453292F;
		if (state.phase == VoidNeedleEntity.AttackPhase.CHARGE || state.phase == VoidNeedleEntity.AttackPhase.SWOOP){
			this.truemain.xRot = state.xRot* 0.017453292F + 90.0F * 0.017453292F;
		}
	}
}