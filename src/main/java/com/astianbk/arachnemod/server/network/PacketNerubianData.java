package com.astianbk.arachnemod.server.network;

import com.astianbk.arachnemod.AracneMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketNerubianData(CompoundTag tag) implements CustomPacketPayload {
    public static final Type<PacketNerubianData> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(AracneMod.MODID, "sync_data"));
    public static final StreamCodec<FriendlyByteBuf, PacketNerubianData> STREAM_CODEC =
            CustomPacketPayload.codec(PacketNerubianData::write, PacketNerubianData::new);

    public PacketNerubianData(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static <T extends CustomPacketPayload> void handle(PacketNerubianData msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

        });
    }
}
