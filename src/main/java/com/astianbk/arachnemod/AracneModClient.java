package com.astianbk.arachnemod;

import com.astianbk.arachnemod.client.layer.MarkSilentLayer;
import com.astianbk.arachnemod.client.renderer.OrbRenderer;
import com.astianbk.arachnemod.client.renderer.ScarabRenderer;
import com.astianbk.arachnemod.client.gui.IdolSpeechGui;
import com.astianbk.arachnemod.client.model.*;
import com.astianbk.arachnemod.client.renderer.VoidHopperRenderer;
import com.astianbk.arachnemod.client.renderer.VoidNeedleRenderer;
import com.astianbk.arachnemod.common.items.VoidKnightArmorItem;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.NerubianCap;
import com.google.common.base.Suppliers;
import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import java.util.function.BiConsumer;

@Mod(value = AracneMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AracneMod.MODID, value = Dist.CLIENT)
public class AracneModClient {
    public static final Identifier LOCATION = Identifier.fromNamespaceAndPath(AracneMod.MODID,"textures/entity/war_spider/warspider.png");
    public static final ContextKey<MobEffectInstance> EFFECT = new ContextKey<>(Identifier.fromNamespaceAndPath(AracneMod.MODID,"effect"));
    public AracneModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.AddLayers event) {
        AvatarRenderer renderer = event.getPlayerRenderer(PlayerModelType.SLIM);

        if (renderer != null) {
            renderer.addLayer(new MarkSilentLayer(renderer));
        }
    }
    @SubscribeEvent
    public static void extractEvent(RegisterRenderStateModifiersEvent event){
        event.registerEntityModifier(
                new TypeToken<LivingEntityRenderer<LivingEntity,LivingEntityRenderState,?>>() {},
                (entity, renderState) -> {
                    if (entity.hasEffect(NRegistry.SILENT_HEX)){
                        renderState.setRenderData(EFFECT, entity.getEffect(NRegistry.SILENT_HEX));
                    }
                }
        );

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
//        event.registerLayerDefinition(ScarabModel.ARMOR_LOCATION,Suppliers.ofInstance(ScarabModel.createBodyLayer(new CubeDeformation(1.0F))));
    }

    @SubscribeEvent
    public static void RenderArm(RenderArmEvent event){
//        event.setCanceled(true);
    }
    @SubscribeEvent
    public static void renderModel(RenderLivingEvent.Pre event){
        if (event.getRenderState().entityType == EntityType.PLAYER){
            AbstractClientPlayer player = Minecraft.getInstance().player;
            NerubianCap.get(player).ifPresent(nerubianCap -> {
                ((HumanoidModel)event.getRenderer().getModel()).head.visible = !(player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof VoidKnightArmorItem);

                ((HumanoidModel)event.getRenderer().getModel()).rightArm.visible = !(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof VoidKnightArmorItem);
                ((HumanoidModel)event.getRenderer().getModel()).leftArm.visible = !(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof VoidKnightArmorItem);
                ((HumanoidModel)event.getRenderer().getModel()).leftLeg.visible = !(player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof VoidKnightArmorItem) && !(player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof VoidKnightArmorItem);
                ((HumanoidModel)event.getRenderer().getModel()).rightLeg.visible = !(player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof VoidKnightArmorItem) && !(player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof VoidKnightArmorItem);

                if (nerubianCap.transformComplete){
                    Minecraft mc = Minecraft.getInstance();
                    EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(),mc.getBlockModelResolver(),mc.getItemModelResolver(),mc.getMapRenderer(),mc.getResourceManager(),mc.getEntityModels(),new EquipmentAssetManager(),mc.getAtlasManager(),mc.font,mc.playerSkinRenderCache());
                    ScarabPlayerRenderer renderer = new ScarabPlayerRenderer(context);
                    WarSpiderModel model = new WarSpiderModel(context.bakeLayer(WarSpiderModel.LAYER_LOCATION));
//                    ItemScarabLayer layer = new ItemScarabLayer<>(renderer,Minecraft.getInstance().gameRenderer.itemInHandRenderer);
                    PoseStack poseStack = event.getPoseStack();
                    float partialTicks = event.getPartialTick();

                    event.setCanceled(true);
                    poseStack.pushPose();

                    boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
                    float f = Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
                    float f1 = Mth.rotLerp(partialTicks, player.yHeadRotO, player.yHeadRot);
                    float f2 = f1 - f;
                    float f7;
                    if (shouldSit && player.getVehicle() instanceof LivingEntity livingplayer) {
                        f = Mth.rotLerp(partialTicks, livingplayer.yBodyRotO, livingplayer.yBodyRot);
                        f2 = f1 - f;
                        f7 = Mth.wrapDegrees(f2);
                        if (f7 < -85.0F) {
                            f7 = -85.0F;
                        }

                        if (f7 >= 85.0F) {
                            f7 = 85.0F;
                        }

                        f = f1 - f7;
                        if (f7 * f7 > 2500.0F) {
                            f += f7 * 0.2F;
                        }

                        f2 = f1 - f;
                    }

                    float f6 = Mth.lerp(partialTicks, player.xRotO, player.getXRot());


                    f2 = Mth.wrapDegrees(f2);
                    if (player.hasPose(Pose.SLEEPING)) {
                        Direction direction = player.getBedOrientation();
                        if (direction != null) {
                            float f3 = player.getEyeHeight(Pose.STANDING) - 0.1F;
                            poseStack.translate((float)(-direction.getStepX()) * f3, 0.0F, (float)(-direction.getStepZ()) * f3);
                        }
                    }

                    float f8 = player.getScale();
                    poseStack.scale(f8, f8, f8);
                    float f9 = player.tickCount + partialTicks;
                    setupRotations(player,poseStack,f9, f, partialTicks, f8);
                    poseStack.scale(-1.0F, -1.0F, 1.0F);
                    poseStack.translate(0.0F, -1.501F, 0.0F);
                    float f4 = 0.0F;
                    float f5 = 0.0F;

                    if (!shouldSit && player.isAlive()) {
                        f4 = player.walkAnimation.speed(partialTicks);
                        f5 = player.walkAnimation.position(partialTicks);
                        if (player.isBaby()) {
                            f5 *= 3.0F;
                        }

                        if (f4 > 1.0F) {
                            f4 = 1.0F;
                        }
                    }

                    AvatarRenderState state = renderer.createRenderState();
//                    setModelProperties(player,model);
                    model.setupAnim((AvatarRenderState) event.getRenderState());

                    model.renderToBuffer(poseStack,mc.renderBuffers().bufferSource().getBuffer(RenderTypes.entityCutout(LOCATION)), renderer.getPackedLightCoords(player,partialTicks), LivingEntityRenderer.getOverlayCoords(state,0.0F));
                    poseStack.popPose();
                }
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
                ItemUseAnimation useanim = itemstack.getUseAnimation();
                if (useanim == ItemUseAnimation.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == ItemUseAnimation.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == ItemUseAnimation.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_TRIDENT;
                }

                if (useanim == ItemUseAnimation.CROSSBOW && hand == player.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == ItemUseAnimation.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (useanim == ItemUseAnimation.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (useanim == ItemUseAnimation.BRUSH) {
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
//            event.getFogData().environmentalStart = 32.0F;
//            event.getFogData().environmentalEnd = 128.0F;
//
//            event.getFogData().renderDistanceStart = 96.0F;
//            event.getFogData().renderDistanceEnd = 128.0F;
//
//            event.getFogData().skyEnd = 128.0F;
//            event.getFogData().cloudEnd = 128.0F;
        }
    }


    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, Identifier.fromNamespaceAndPath(AracneMod.MODID,"idol_speech"),new IdolSpeechGui());
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NRegistry.VOID_NEEDLE.get(), VoidNeedleRenderer::new);
        event.registerEntityRenderer(NRegistry.SCARAB.get(), ScarabRenderer::new);
        event.registerEntityRenderer(NRegistry.ORB.get(), OrbRenderer::new);
        event.registerEntityRenderer(NRegistry.VOID_HOPPER.get(), VoidHopperRenderer::new);
    }
}
