package com.astianbk.arachnemod.client.render_state;

import com.astianbk.arachnemod.server.entity.OrbEntity;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class OrbRenderState extends EntityRenderState {
    public OrbEntity.Type type = OrbEntity.Type.CANCEL;
    public boolean lock = false;

}
