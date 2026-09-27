package com.tbk.voidweaver;

import com.tbk.voidweaver.client.gui.ArachneSpeechGui;
import com.tbk.voidweaver.client.gui.CocoonGui;
import com.tbk.voidweaver.client.layer.MarkSilentLayer;
import com.tbk.voidweaver.client.renderer.*;
import com.tbk.voidweaver.client.gui.DarknessGui;
import com.tbk.voidweaver.client.model.*;
import com.tbk.voidweaver.client.renderer.item.ScytheScissorsClientExtensions;
import com.tbk.voidweaver.client.renderer.item.ScytheScissorsItemModel;
import com.tbk.voidweaver.client.renderer.item.ScytheScissorsRenderer;
import com.tbk.voidweaver.common.items.VoidKnightArmorItem;
import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import com.tbk.voidweaver.server.network.PacketSyncLeftClick;
import com.google.common.base.Suppliers;
import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.*;

import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.PacketDistributor;


import java.util.List;

@Mod(value = AracneMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AracneMod.MODID, value = Dist.CLIENT)
public class AracneModClient {
    public static float[] offset = new float[3];
    public static int index = 0;
    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/war_spider/warspider.png");
    public static final ResourceLocation LOCATION_COCOON= ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/cocoon/shield_cocoon.png");
    public AracneModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
//    @SubscribeEvent
//    public static void registerRenderers(RegisterSpecialModelRendererEvent event) {
//        event.register(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "scythe_scissors"),  ScytheScissorsRenderer.Unbaked.MAP_CODEC);
//    }
//
//    @SubscribeEvent
//    public static void registerItemModel(RegisterItemModelsEvent event){
//        event.register(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "scythe_scissors_model"),  ScytheScissorsItemModel.Unbaked.MAP_CODEC);
//    }
//    @SubscribeEvent
//    public static void registerItemModel(RegisterConditionalItemModelPropertyEvent event){
//        event.register(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "attack"), UseScytherScissor.MAP_CODEC);
//    }
    @SubscribeEvent
    public static void onClick(InputEvent.MouseButton.Pre event){
        if (Minecraft.getInstance().screen!=null)return;
        if (event.getButton() == 1)return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();

        if (!stack.is(NRegistry.SCYTHE_SCISSORS.get())) {
            return;
        }
        event.setCanceled(true);
        if (event.getAction() == 0){
            PacketDistributor.sendToServer(new PacketSyncLeftClick(player.getId()));
        }
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.AddLayers event) {
        EntityRenderer<?> renderer = event.getRenderer(EntityType.PLAYER);

        if (renderer instanceof PlayerRenderer) {
            ((PlayerRenderer)renderer).addLayer((RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) new MarkSilentLayer(renderer));
        }
    }

    @SubscribeEvent
    public static void renderArm(RenderArmEvent event){

    }


    @SubscribeEvent
    public static void registerModel(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(VoidNeedleModel.LAYER_LOCATION,Suppliers.ofInstance(VoidNeedleModel.createBodyLayer()));
        event.registerLayerDefinition(ScarabModel.LAYER_LOCATION, Suppliers.ofInstance(ScarabModel.createBodyLayer()));
        event.registerLayerDefinition(WarSpiderModel.LAYER_LOCATION,Suppliers.ofInstance(WarSpiderModel.createBodyLayer()));
        event.registerLayerDefinition(VoidKnightArmorModel.CHESTPLATE_LOCATION,Suppliers.ofInstance(VoidKnightArmorModel.createChestLayer()));
        event.registerLayerDefinition(VoidKnightArmorModel.HELMET_LOCATION,Suppliers.ofInstance(VoidKnightArmorModel.createHelmetLayer()));
        event.registerLayerDefinition(VoidKnightArmorModel.LEGGINGS_LOCATION,Suppliers.ofInstance(VoidKnightArmorModel.createLeggingsLayer()));
        event.registerLayerDefinition(VoidKnightArmorModel.ALL_LOCATION,Suppliers.ofInstance(VoidKnightArmorModel.createBodyLayer()));
        event.registerLayerDefinition(VoidHopperModel.LAYER_LOCATION,Suppliers.ofInstance(VoidHopperModel.createBodyLayer()));
        event.registerLayerDefinition(VoidVeilmothModel.LAYER_LOCATION,Suppliers.ofInstance(VoidVeilmothModel.createBodyLayer()));
        event.registerLayerDefinition(VoidBeetleModel.LAYER_LOCATION,Suppliers.ofInstance(VoidBeetleModel.createBodyLayer()));
        event.registerLayerDefinition(EnterDimensionModel.LAYER_LOCATION,Suppliers.ofInstance(EnterDimensionModel.createBodyLayer()));
        event.registerLayerDefinition(WebElevatorModel.LAYER_LOCATION,Suppliers.ofInstance(WebElevatorModel.createBodyLayer()));
        event.registerLayerDefinition(WebPartModel.LAYER_LOCATION,Suppliers.ofInstance(WebPartModel.createBodyLayer()));
        event.registerLayerDefinition(VoidGrubModel.LAYER_LOCATION,Suppliers.ofInstance(VoidGrubModel.createBodyLayer()));
        event.registerLayerDefinition(SealingCrystalModel.LAYER_LOCATION,Suppliers.ofInstance(SealingCrystalModel.createBodyLayer()));
        event.registerLayerDefinition(ShieldCocoonModel.LAYER_LOCATION,Suppliers.ofInstance(ShieldCocoonModel.createBodyLayer()));
        event.registerLayerDefinition(ArachneLegModel.LAYER_LOCATION,Suppliers.ofInstance(ArachneLegModel.createBodyLayer()));
        event.registerLayerDefinition(VoidScytheModel.LAYER_LOCATION,Suppliers.ofInstance(VoidScytheModel.createBodyLayer()));
        event.registerLayerDefinition(ScytheScissorsModel.LAYER_LOCATION,Suppliers.ofInstance(ScytheScissorsModel.createBodyLayer()));
        event.registerLayerDefinition(NeedleHelmetModel.LAYER_LOCATION,Suppliers.ofInstance(NeedleHelmetModel.createBodyLayer()));

        //        event.registerLayerDefinition(ScarabModel.ARMOR_LOCATION,Suppliers.ofInstance(ScarabModel.createBodyLayer(new CubeDeformation(1.0F))));
    }


    @SubscribeEvent
    public static void RenderArm(RenderArmEvent event){
//        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        ArachneAttachment.get(event.getEntity()).ifPresent(arachneAttachment -> {
            if (arachneAttachment.isCocoon){
                event.getInput().leftImpulse = 0.0F;
                event.getInput().forwardImpulse = 0.0F;
            }
        });

    }
    @SubscribeEvent
    public static void renderModel(RenderLivingEvent.Pre event){
        if (event.getEntity() instanceof LocalPlayer){
            AbstractClientPlayer player = Minecraft.getInstance().player;
            ArachneAttachment.get(player).ifPresent(nerubianCap -> {
                ((HumanoidModel)event.getRenderer().getModel()).head.visible = !(player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof VoidKnightArmorItem);

                ((HumanoidModel)event.getRenderer().getModel()).rightArm.visible = !(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof VoidKnightArmorItem);
                ((HumanoidModel)event.getRenderer().getModel()).leftArm.visible = !(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof VoidKnightArmorItem);
                ((HumanoidModel)event.getRenderer().getModel()).leftLeg.visible = !(player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof VoidKnightArmorItem) && !(player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof VoidKnightArmorItem);
                ((HumanoidModel)event.getRenderer().getModel()).rightLeg.visible = !(player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof VoidKnightArmorItem) && !(player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof VoidKnightArmorItem);
            });
        }
    }



    private static void setModelProperties(AbstractClientPlayer clientPlayer,ScarabModel playermodel) {
        if (clientPlayer.isSpectator()) {

        } else {
            HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(clientPlayer, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(clientPlayer, InteractionHand.OFF_HAND);
            if (humanoidmodel$armpose.isTwoHanded()) {
                humanoidmodel$armpose1 = clientPlayer.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }

//            if (clientPlayer.getMainArm() == HumanoidArm.RIGHT) {
//                playermodel.rightArmPose = humanoidmodel$armpose;
//                playermodel.leftArmPose = humanoidmodel$armpose1;
//            } else {
//                playermodel.rightArmPose = humanoidmodel$armpose1;
//                playermodel.leftArmPose = humanoidmodel$armpose;
//            }
        }
    }

    private static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useanim == UseAnim.CROSSBOW && hand == player.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (useanim == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!player.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }
            HumanoidModel.ArmPose forgeArmPose = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(player, hand, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    protected static void setupRotations(Player entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        if (entity.isFullyFrozen()) {
            yBodyRot += (float)(Math.cos((double)entity.tickCount * 3.25) * Math.PI * 0.4000000059604645);
        }

        if (!entity.hasPose(Pose.SLEEPING)) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
        }

        if (entity.deathTime > 0) {
            float f = ((float)entity.deathTime + partialTick - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            poseStack.mulPose(Axis.ZP.rotationDegrees(f * 90));
        } else if (entity.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - entity.getXRot()));
            poseStack.mulPose(Axis.YP.rotationDegrees(((float)entity.tickCount + partialTick) * -75.0F));
        } else if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            float f1 = direction != null ? sleepDirectionToRotation(direction) : yBodyRot;
            poseStack.mulPose(Axis.YP.rotationDegrees(f1));
            poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        }
    }

    private static float sleepDirectionToRotation(Direction facing) {
        switch (facing) {
            case SOUTH:
                return 90.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    @SubscribeEvent
    public static void fogRender(ViewportEvent.RenderFog event){
        if (Minecraft.getInstance().player.level().dimension()==NRegistry.THE_VOID){
            if (Minecraft.getInstance().level.getData(NRegistry.THE_VOID_ATTACHMENT.get()).flash){
                event.setFarPlaneDistance(256 + 800 * Minecraft.getInstance().level.getData(NRegistry.THE_VOID_ATTACHMENT.get()).getIntensityFlash(0.0F));
            }

        }
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"cocoon_overlay"),new CocoonGui());
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"darkness"),new DarknessGui());
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"arachne_speech"),new ArachneSpeechGui());

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NRegistry.VOID_NEEDLE.get(), VoidNeedleRenderer::new);
        event.registerEntityRenderer(NRegistry.SCARAB.get(), ScarabRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_SCYTHE.get(), VoidScytheRenderer::new);
        event.registerEntityRenderer(NRegistry.ORB.get(), OrbRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_HOPPER.get(), VoidHopperRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_BEETLE.get(), VoidBeetleRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_GRUB.get(), VoidGrubRenderer::new);
        event.registerEntityRenderer(NRegistry.SEALING_CRYSTAL.get(), SealingCrystalRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_VEILMOTH.get(), VoidVeilmothRenderer::new);
        event.registerEntityRenderer(NRegistry.ENTER_DIMENSION.get(),EnterDimensionRenderer::new);
        event.registerEntityRenderer(NRegistry.WEB_ELEVATOR.get(),WebElevatorRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_SPIDER.get(),SummoneableSpiderRenderer::new);
        event.registerEntityRenderer(NRegistry.ARACHNE_LEG.get(),ArachneLegRenderer::new);
    }
}
