package com.tbk.voidweaver.server.entity;

import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class WebElevatorEntity extends Entity {
    public WebElevatorEntity(EntityType<?> type, Level level) {
        super( type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {

    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide()){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                ServerLevel serverLevel = ((ServerLevel) level()).getServer().getLevel(Level.OVERWORLD);
                if (serverLevel == null) return;

                Vec3 vec3 = arachneAttachment.teleportBack != null
                        ? Vec3.atBottomCenterOf(arachneAttachment.teleportBack)
                        : new Vec3(
                        player.position().x,
                        serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE, blockPosition().getX(), blockPosition().getZ()),
                        position().z
                );

                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.teleportTo(serverLevel, vec3.x, vec3.y, vec3.z, Set.of(), 0.0F, 0.0F);
                }
            });
        }
        return InteractionResult.SUCCESS;
    }



    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4){
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.WEEPING_VINES_PLACE, SoundSource.BLOCKS,3.0F,1.0F,random,blockPosition()));
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.COBWEB_PLACE, SoundSource.BLOCKS,2.0F,-1.0F,random,blockPosition()));
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS,2.0F,-1.0F,random,blockPosition()));

        }
        super.handleEntityEvent(id);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float v) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
