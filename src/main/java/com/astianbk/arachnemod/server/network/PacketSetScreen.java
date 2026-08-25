package com.astianbk.arachnemod.server.network;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSetScreen(int id) implements CustomPacketPayload {
    public static final Type<PacketSetScreen> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(AracneMod.MODID, "set_screen"));
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
            minecraft.setScreenAndShow(new LevelLoadingScreen(new LevelLoadTracker(), LevelLoadingScreen.Reason.OTHER));
        });
    }
}
