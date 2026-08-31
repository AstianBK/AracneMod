package com.astianbk.arachnemod.mixin;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.registry.NRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
    @Shadow
    private static void spawnCategoryForPosition(net.minecraft.world.entity.MobCategory mobCategory, net.minecraft.server.level.ServerLevel level, net.minecraft.world.level.chunk.ChunkAccess chunk, net.minecraft.core.BlockPos start, net.minecraft.world.level.NaturalSpawner.SpawnPredicate extraTest, net.minecraft.world.level.NaturalSpawner.AfterSpawnCallback spawnCallback) {

    }

    @Inject(
            method = "spawnCategoryForChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void debugSpawn(
            MobCategory mobCategory,
            ServerLevel level,
            LevelChunk chunk,
            NaturalSpawner.SpawnPredicate extraTest,
            NaturalSpawner.AfterSpawnCallback spawnCallback,
            CallbackInfo ci
    ) {
//        if (mobCategory != ModMobCategory.VOID_CREATURE) {
//            return;
//        }

        BlockPos start = getRandom(level, chunk);
        if (start.getY() >= 200 + 1) {
            spawnCategoryForPosition(mobCategory, level, chunk, start, extraTest, spawnCallback);
        }
        ci.cancel();
    }
    private static BlockPos getRandom(Level level, LevelChunk chunk) {
        ChunkPos pos = chunk.getPos();
        int x = pos.getMinBlockX() + level.getRandom().nextInt(16);
        int z = pos.getMinBlockZ() + level.getRandom().nextInt(16);
        int topEmptyY = 260;
        int y = Mth.randomBetweenInclusive(level.getRandom(), 240, topEmptyY);
        return new BlockPos(x, y, z);
    }

}
