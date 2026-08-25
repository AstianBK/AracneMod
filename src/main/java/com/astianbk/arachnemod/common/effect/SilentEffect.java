package com.astianbk.arachnemod.common.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SilentEffect extends MobEffect {
    public SilentEffect() {
        super(MobEffectCategory.HARMFUL, 745784);
    }

    @Override
    public void applyInstantaneousEffect(ServerLevel level, @Nullable Entity source, @Nullable Entity owner, LivingEntity mob, int amplification, double scale) {
        super.applyInstantaneousEffect(level, source, owner, mob, amplification, scale);
        List<Holder<MobEffect>> removeEffect = new ArrayList<>();
        for (MobEffectInstance instance:mob.getActiveEffects()){
            if (instance.getEffect().value().isBeneficial()){
                removeEffect.add(instance.getEffect());
            }
        }
        for (Holder<MobEffect> effectHolder : removeEffect){
            mob.removeEffect(effectHolder);
        }
    }
}
