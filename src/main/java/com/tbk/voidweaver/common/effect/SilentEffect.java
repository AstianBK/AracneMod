package com.tbk.voidweaver.common.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SilentEffect extends MobEffect {
    public SilentEffect() {
        super(MobEffectCategory.HARMFUL, 745784);
    }

    @Override
    public void applyInstantenousEffect(@org.jetbrains.annotations.Nullable Entity source, @org.jetbrains.annotations.Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        List<Holder<MobEffect>> removeEffect = new ArrayList<>();
        for (MobEffectInstance instance:livingEntity.getActiveEffects()){
            if (instance.getEffect().value().isBeneficial()){
                removeEffect.add(instance.getEffect());
            }
        }
        for (Holder<MobEffect> effectHolder : removeEffect){
            livingEntity.removeEffect(effectHolder);
        }
    }


}
