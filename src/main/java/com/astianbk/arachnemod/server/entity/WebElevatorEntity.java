package com.astianbk.arachnemod.server.entity;

import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class WebElevatorEntity extends Entity {
    public WebElevatorEntity(EntityType<?> type, Level level) {
        super( type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }


    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!this.level().isClientSide()){
            ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                ServerLevel serverLevel = ((ServerLevel)level()).getServer().getLevel(Level.OVERWORLD);
                if (serverLevel==null)return;
                Vec3 vec3 = arachneAttachment.teleportBack!=null ? Vec3.atBottomCenterOf(arachneAttachment.teleportBack ): new Vec3(player.position().x,serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE,blockPosition()),position().z);
                player.teleport(new TeleportTransition(serverLevel,vec3, Vec3.ZERO,0.0F,0.0F,(entity)->{

                }));
            });
        }

        return super.interact(player, hand, location);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {

    }
}
