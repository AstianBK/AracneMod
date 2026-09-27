
package com.tbk.voidweaver.server.network;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.common.registry.NRegistry;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncLeftClick(int id) implements CustomPacketPayload {
    public static final Type<PacketSyncLeftClick> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "sycn_left_click"));
    public static final StreamCodec<FriendlyByteBuf, PacketSyncLeftClick> STREAM_CODEC =
            CustomPacketPayload.codec(PacketSyncLeftClick::write, PacketSyncLeftClick::new);

    public PacketSyncLeftClick(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static <T extends CustomPacketPayload> void handle(PacketSyncLeftClick msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player entity = ctx.player();
            ArachneAttachment.get(entity).ifPresent(arachneAttachment -> {

                if (!arachneAttachment.scissorAttack){
                    arachneAttachment.scissorAttack = true;
                    arachneAttachment.scissorAttackTime = 20;
                    entity.syncData(NRegistry.ARACNE);
                }
            });
        });
    }
}
