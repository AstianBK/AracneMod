package com.astianbk.arachnemod.server.network;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketHandlerParticle(int id, BlockPos pos) implements CustomPacketPayload {
    public static final Type<PacketHandlerParticle> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(AracneMod.MODID, "play_particles"));
    public static final StreamCodec<FriendlyByteBuf, PacketHandlerParticle> STREAM_CODEC =
            CustomPacketPayload.codec(PacketHandlerParticle::write, PacketHandlerParticle::new);

    public PacketHandlerParticle(FriendlyByteBuf buf) {
        this(buf.readInt(),buf.readBlockPos());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeBlockPos(this.pos);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static <T extends CustomPacketPayload> void handle(PacketHandlerParticle msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            assert minecraft.level!=null;
            switch (msg.id){
                case 0->{
                    addRoarParticleEffects(minecraft,msg.pos,minecraft.level.getRandom());
                }
                case 1->{
                    addWeaverParticle(minecraft,msg.pos,minecraft.level.getRandom());
                }
            }
        });
    }

    private static void addRoarParticleEffects(Minecraft minecraft,BlockPos pos, RandomSource randomSource) {
        Vec3 center = Vec3.atCenterOf(pos);

        for(int i = 0; i < 40; ++i) {
            double velocityX = randomSource.nextGaussian() * 0.2;
            double velocityY = randomSource.nextGaussian() * 0.2;
            double velocityZ = randomSource.nextGaussian() * 0.2;
            minecraft.level.addParticle(ParticleTypes.POOF, center.x, center.y, center.z, velocityX, velocityY, velocityZ);
        }

    }
    private static void addWeaverParticle(Minecraft minecraft,BlockPos pos, RandomSource randomSource) {
        Vec3 center = Vec3.atLowerCornerOf(pos);

        for(int i = 0; i < 50; ++i) {
            double x = center.x + randomSource.nextFloat();
            double y = center.y + randomSource.nextFloat();
            double z = center.z + randomSource.nextFloat();

            minecraft.particleEngine.createParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, x, y, z, 0.0, 0.0, 0.0);
        }

    }
}
