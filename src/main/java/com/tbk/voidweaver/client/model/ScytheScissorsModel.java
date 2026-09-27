package com.tbk.voidweaver.client.model;// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.client.anim.ItemsAnim;
import com.tbk.voidweaver.client.renderer.item.ScytheScissorsItemModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix4fc;

import javax.annotation.Nullable;

public class ScytheScissorsModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "scythe_scissors"), "main");
	private final ModelPart truemain;
	private final ModelPart scythe;
	private final ModelPart group2;
	private final ModelPart group;
	public ScytheScissorsModel(ModelPart root) {
        super( RenderType::entityCutout);
        this.truemain = root.getChild("truemain");
		this.scythe = this.truemain.getChild("scythe");
		this.group2 = this.scythe.getChild("group2");
		this.group = this.scythe.getChild("group");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create().texOffs(10, 46).mirror().addBox(-2.275F, -0.1801F, 5.7789F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.05F)).mirror(false)
				.texOffs(21, 43).mirror().addBox(-5.225F, -0.1301F, -3.2211F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(21, 43).addBox(0.775F, -0.1301F, -3.2211F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.725F, 22.8551F, -13.7789F));

		PartDefinition scythe = truemain.addOrReplaceChild("scythe", CubeListBuilder.create(), PartPose.offset(0.6F, -0.1942F, 9.7211F));

		PartDefinition group2 = scythe.addOrReplaceChild("group2", CubeListBuilder.create(), PartPose.offset(-1.375F, 0.0F, 0.125F));

		PartDefinition cube_r1 = group2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(0.0F, -8.0F, 2.0672F, 0.0F, 10.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 32).addBox(0.0F, -8.0F, 2.0672F, 0.0F, 10.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(70, 0).addBox(-1.0F, -1.0F, -0.0672F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.55F, 1.0F, -0.8172F, -0.4363F, 0.0F, 1.5708F));

		PartDefinition group = scythe.addOrReplaceChild("group", CubeListBuilder.create(), PartPose.offset(1.375F, 0.0F, -0.125F));

		PartDefinition cube_r2 = group.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -8.0F, 2.0672F, 0.0F, 10.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).mirror().addBox(0.0F, -8.0F, 2.0672F, 0.0F, 10.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(70, 0).mirror().addBox(-1.0F, -1.0F, -0.0672F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.3F, 1.0F, -0.6422F, -0.4363F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	private static float catmullRom(float time, float[] values, float[] times) {
		int count = values.length;

		if (time <= times[0])
			return values[0];

		if (time >= times[count - 1])
			return values[count - 1];

		int i = 0;

		while (i < count - 1 && time > times[i + 1]) {
			i++;
		}

        float t1 = times[i];
		float t2 = times[i + 1];

        float p0 = values[Math.max(0, i - 1)];
		float p1 = values[i];
		float p2 = values[i + 1];
		float p3 = values[Math.min(count - 1, i + 2)];

		float t = (time - t1) / (t2 - t1);

		return catmullRom(t, p0, p1, p2, p3);
	}

	private static float catmullRom(float t, float p0, float p1, float p2, float p3) {
		float t2 = t * t;
		float t3 = t2 * t;

		return 0.5F * (2.0F * p1 + (-p0 + p2) * t + (2.0F * p0 - 5.0F * p1 + 4.0F * p2 - p3) * t2 + (-p0 + 3.0F * p1 - 3.0F * p2 + p3) * t3);
	}
	public void prepareModel(State state){
		float time = 1.0F - (state.ticks() / 20.0F);

		this.truemain.xRot = 0.0F;
		this.truemain.yRot = 0.0F;
		this.truemain.zRot = 0.0F;

		this.truemain.x = 0.0F;
		this.truemain.y = 0.0F;
		this.truemain.z = 0.0F;

		this.group2.xRot = 0.0F;
		this.group2.yRot = 0.0F;
		this.group2.zRot = 0.0F;

		this.group2.xScale = 1.0F;
		this.group2.yScale = 1.0F;
		this.group2.zScale = 1.0F;

		this.group.xRot = 0.0F;
		this.group.yRot = 0.0F;
		this.group.zRot = 0.0F;

		this.group.xScale = 1.0F;
		this.group.yScale = 1.0F;
		this.group.zScale = 1.0F;

		if (!state.attacking())
			return;

		this.truemain.zRot = Mth.DEG_TO_RAD * catmullRom(time, new float[]{0.0F, 0.0F, -5.0F, 12.5F}, new float[]{0.0F, 0.3333F, 0.4583F, 0.75F});

		if (time > 0.75F) {
			this.truemain.zRot = 0.0F;
		}

		this.truemain.z = catmullRom(time, new float[]{0.0F, 0.0F, 3.0F, 0.0F}, new float[]{0.25F, 0.375F, 0.75F, 0.75F});


		this.group2.yRot = Mth.DEG_TO_RAD * catmullRom(time, new float[]{0.0F, -35.0F, 25.0F, 23.89F, 25.0F, 0.0F}, new float[]{0.0F, 0.25F, 0.4167F, 0.5F, 0.625F, 1.0F});

		this.group2.xScale = catmullRom(time, new float[]{1.0F, 1.1F, 1.0F, 1.0F}, new float[]{0.0F, 0.25F, 0.3333F, 1.0F});

		this.group.yRot = Mth.DEG_TO_RAD * catmullRom(time, new float[]{0.0F, 45.0F, -25.0F, -23.89F, -25.0F, 0.0F}, new float[]{0.0F, 0.25F, 0.4167F, 0.5F, 0.625F, 1.0F});

		this.group.xScale = catmullRom(time, new float[]{1.0F, 1.1F, 1.0F, 1.0F}, new float[]{0.0F, 0.25F, 0.3333F, 1.0F});

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
		this.truemain.render(poseStack,vertexConsumer,i,i1,i2);
	}

	public record State(float ticks, boolean attacking, ItemDisplayContext ctxDisplay, Matrix4fc transformation) {
        public float ticks() {
			return this.ticks;
		}

		public @Nullable boolean attacking() {
			return this.attacking;
		}
	}
}