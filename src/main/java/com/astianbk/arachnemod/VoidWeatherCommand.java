package com.astianbk.arachnemod;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;

public class VoidWeatherCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("voidweaver")
                        .then(Commands.literal("event")
                                .then(Commands.literal("bedrockfall")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    boolean value = BoolArgumentType.getBool(context, "value");
                                                    if (context.getSource().isPlayer()) {
                                                        ServerPlayer player = context.getSource().getPlayer();
                                                        if (player.level().dimension() == NRegistry.THE_VOID) {
                                                            var attachment = player.level()
                                                                    .getData(NRegistry.THE_VOID_ATTACHMENT.get());

                                                            if (value) {
                                                                attachment.startBedrockFall(player.level());
                                                            } else {
                                                                attachment.stopBedrockFall(player.level());
                                                            }

                                                            player.level().syncData(NRegistry.THE_VOID_ATTACHMENT.get());
                                                        }
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("voidflash")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    boolean value = BoolArgumentType.getBool(context, "value");
                                                    if (context.getSource().isPlayer()) {
                                                        ServerPlayer player = context.getSource().getPlayer();

                                                        if (player.level().dimension() == NRegistry.THE_VOID) {
                                                            var attachment = player.level().getData(NRegistry.THE_VOID_ATTACHMENT.get());

                                                            if (value) {
                                                                attachment.startFlash(player.level());
                                                            } else {
                                                                attachment.stopFlash(player.level());
                                                            }

                                                            player.level().syncData(NRegistry.THE_VOID_ATTACHMENT.get());
                                                        }
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("arachnepowerlevel")
                                .then(Commands.argument("level", IntegerArgumentType.integer(0, 100))
                                                .executes(context -> {
                                                    int rep = IntegerArgumentType.getInteger(context, "level");

                                                    if (context.getSource().isPlayer()) {
                                                        ServerPlayer player = context.getSource().getPlayer();

                                                        ArachneAttachment.get(player).ifPresent(
                                                                arachneAttachment -> {
                                                                    arachneAttachment.setCurrentReputation(player, rep);
                                                                    player.syncData(NRegistry.ARACNE);
                                                                }
                                                        );
                                                    }

                                                    return 1;
                                                })
                                )
                        )
        );
    }
}