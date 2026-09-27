package com.tbk.voidweaver.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;


@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends LivingEntity, M extends HumanoidModel<S>, A extends HumanoidModel<S>> {
//    @Shadow
//    private  ArmorModelSet<A> modelSet;
//    @Shadow
//    private  ArmorModelSet<A> babyModelSet;
//    @Shadow
//    private  EquipmentLayerRenderer equipmentRenderer;
//    @Shadow
//    private A getArmorModel(S state, EquipmentSlot slot) {
//        return (A) (state.isBaby ? this.babyModelSet : this.modelSet).get(slot);
//    }
//    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
//    @SuppressWarnings("unchecked")
//    private void model(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, S state, CallbackInfo cir) {
//        if (itemStack.getItem() instanceof VoidKnightArmorItem){
//            cir.cancel();
//            Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
//            if (equippable != null && shouldRender(equippable, slot)) {
//                A model = (A) new VoidKnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(VoidKnightArmorModel.ALL_LOCATION));
//                EquipmentClientInfo.LayerType layerType = state.isBaby && state.entityType != EntityTypes.ARMOR_STAND ? EquipmentClientInfo.LayerType.HUMANOID_BABY : (this.usesInnerModel(slot) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID);
//                ((VoidKnightArmorModel)model).setPartVisibility(((VoidKnightArmorModel)model),slot);
//                this.equipmentRenderer.renderLayers(layerType, (ResourceKey)equippable.assetId().orElseThrow(), model, state, itemStack, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
//            }
//        }
//        if (itemStack.getItem() instanceof NeedleHelmetItem){
//            cir.cancel();
//            Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
//            if (equippable != null && shouldRender(equippable, slot)) {
//                A model = (A) new NeedleHelmetModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(NeedleHelmetModel.LAYER_LOCATION));
//                EquipmentClientInfo.LayerType layerType = state.isBaby && state.entityType != EntityTypes.ARMOR_STAND ? EquipmentClientInfo.LayerType.HUMANOID_BABY : (this.usesInnerModel(slot) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID);
//                ((NeedleHelmetModel<AvatarRenderState>)model).setPartVisibility(((NeedleHelmetModel<AvatarRenderState>)model),slot);
//                this.equipmentRenderer.renderLayers(layerType, (ResourceKey)equippable.assetId().orElseThrow(), model, state, itemStack, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
//            }
//        }
//    }
//
//    @Shadow
//    private static boolean shouldRender(Equippable equippable, EquipmentSlot slot) {
//        return equippable.assetId().isPresent() && equippable.slot() == slot;
//    }
//    @Shadow
//    private boolean usesInnerModel(EquipmentSlot slot) {
//        return slot == EquipmentSlot.LEGS;
//    }
}
