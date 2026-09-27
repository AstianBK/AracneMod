package com.tbk.voidweaver.server.network;

import com.tbk.voidweaver.AracneMod;
import com.tbk.voidweaver.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSetScreen(int id) implements CustomPacketPayload {
    public static final Type<PacketSetScreen> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AracneMod.MODID, "set_screen"));
    public static final StreamCodec<FriendlyByteBuf, PacketSetScreen> STREAM_CODEC =
            CustomPacketPayload.codec(PacketSetScreen::write, PacketSetScreen::new);

    public PacketSetScreen(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static <T extends CustomPacketPayload> void handle(PacketSetScreen msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(new LevelLoadingScreen(StoringChunkProgressListener.create(2)));
        });
    }
}
