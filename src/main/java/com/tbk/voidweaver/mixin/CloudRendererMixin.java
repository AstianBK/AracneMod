package com.tbk.voidweaver.mixin;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.registry.NRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;

//@Mixin(Cloud.class)
public abstract class CloudRendererMixin {
//    private static final ResourceLocation ARACNE_CLOUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "textures/sky/cloud.png");
//
//    @Redirect(method = "prepare", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;open(Lnet/minecraft/resources/ResourceLocation;)Ljava/io/InputStream;"))
//    private InputStream aracnemod$replaceCloudTexture(ResourceManager manager, ResourceLocation original) throws IOException {
//        if (Minecraft.getInstance().level !=null && Minecraft.getInstance().level.dimension() == NRegistry.THE_VOID){
//            return manager.open(ARACNE_CLOUD_TEXTURE);
//        }
//        return manager.open(original);
//    }
}
