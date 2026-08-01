package com.astianbk.aracnemod.mixin;

import com.astianbk.aracnemod.AracneMod;
import com.astianbk.aracnemod.client.model.VoidKnightArmorModel;
import com.astianbk.aracnemod.common.items.VoidKnightArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer.shouldRender;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> {
    @Shadow
    private  ArmorModelSet<A> modelSet;
    @Shadow
    private  ArmorModelSet<A> babyModelSet;
    @Shadow
    private  EquipmentLayerRenderer equipmentRenderer;
    @Shadow
    private A getArmorModel(S state, EquipmentSlot slot) {
        return (A) (state.isBaby ? this.babyModelSet : this.modelSet).get(slot);
    }
    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("unchecked")
    private void model(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot slot, int lightCoords, S state, CallbackInfo cir) {
        if (itemStack.getItem() instanceof VoidKnightArmorItem){
            cir.cancel();
            Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
            if (equippable != null && shouldRender(equippable, slot)) {
                A model = (A) new VoidKnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(VoidKnightArmorModel.ALL_LOCATION));
                EquipmentClientInfo.LayerType layerType = state.isBaby && state.entityType != EntityType.ARMOR_STAND ? EquipmentClientInfo.LayerType.HUMANOID_BABY : (this.usesInnerModel(slot) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID);
                ((VoidKnightArmorModel)model).setPartVisibility(((VoidKnightArmorModel)model),slot);
                this.equipmentRenderer.renderLayers(layerType, (ResourceKey)equippable.assetId().orElseThrow(), model, state, itemStack, poseStack, submitNodeCollector, lightCoords, state.outlineColor);
            }
        }
    }

    @Shadow
    private static boolean shouldRender(Equippable equippable, EquipmentSlot slot) {
        return equippable.assetId().isPresent() && equippable.slot() == slot;
    }
    @Shadow
    private boolean usesInnerModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS;
    }
}
