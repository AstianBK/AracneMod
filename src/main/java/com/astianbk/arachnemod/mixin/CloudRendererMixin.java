package com.astianbk.arachnemod.mixin;

import com.astianbk.arachnemod.AracneMod;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererMixin {
    private static final Identifier ARACNE_CLOUD_TEXTURE =
            Identifier.fromNamespaceAndPath(
                    "arachnemod",
                    "textures/sky/cloud.png"
            );

    @Redirect(method = "prepare", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;open(Lnet/minecraft/resources/Identifier;)Ljava/io/InputStream;"))
    private InputStream aracnemod$replaceCloudTexture(ResourceManager manager, Identifier original) throws IOException {
        return manager.open(ARACNE_CLOUD_TEXTURE);
    }
}
