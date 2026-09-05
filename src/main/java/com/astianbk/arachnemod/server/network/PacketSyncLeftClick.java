
package com.astianbk.arachnemod.server.network;

import com.astianbk.arachnemod.AracneMod;
import com.astianbk.arachnemod.common.data_component.ScytheScissorsAttack;
import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncLeftClick(int id) implements CustomPacketPayload {
    public static final Type<PacketSyncLeftClick> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(AracneMod.MODID, "sycn_left_click"));
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
