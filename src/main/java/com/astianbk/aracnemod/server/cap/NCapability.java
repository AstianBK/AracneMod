package com.astianbk.aracnemod.server.cap;

import com.astianbk.aracnemod.AracneMod;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class NCapability {
    public static final EntityCapability<NerubianCap,Void> NERUBIAN_CAP = EntityCapability.createVoid(Identifier.fromNamespaceAndPath(AracneMod.MODID,"aracne_cap"), NerubianCap.class);
}
