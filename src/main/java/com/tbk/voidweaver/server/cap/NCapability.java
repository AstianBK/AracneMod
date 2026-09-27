package com.tbk.voidweaver.server.cap;

import com.tbk.voidweaver.AracneMod;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class NCapability {
    public static final EntityCapability<ArachneAttachment,Void> NERUBIAN_CAP = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID,"aracne_cap"), ArachneAttachment.class);
}
