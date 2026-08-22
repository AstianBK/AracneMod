package com.astianbk.arachnemod.client.render_state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SealingCrystalRenderState extends LivingEntityRenderState {
    public AnimationState idle = new AnimationState();
    public boolean showsBottom = true;
    public @Nullable Vec3 beamOffset;
    public SealingCrystalRenderState(){

    }


}
