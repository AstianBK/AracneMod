package com.astianbk.arachnemod.server.network;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketPlayDialog(Identifier identifier,int id) implements CustomPacketPayload {
    public static final Type<PacketPlayDialog> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(AracneMod.MODID, "play_dialog"));
    public static final StreamCodec<FriendlyByteBuf, PacketPlayDialog> STREAM_CODEC =
            CustomPacketPayload.codec(PacketPlayDialog::write, PacketPlayDialog::new);

    public PacketPlayDialog(FriendlyByteBuf buf) {
        this(buf.readIdentifier(),buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeIdentifier(this.identifier);
        buf.writeInt(this.id);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static <T extends CustomPacketPayload> void handle(PacketPlayDialog msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level.getEntity(msg.id) instanceof  Player player){
                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                    AracneMod.LOGGER.info("play");
                    arachneAttachment.playDialog(msg.identifier);
                });
            }
        });
    }
}
