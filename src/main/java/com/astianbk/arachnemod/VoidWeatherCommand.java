package com.astianbk.arachnemod;

import com.astianbk.arachnemod.common.registry.NRegistry;
import com.astianbk.arachnemod.server.cap.ArachneAttachment;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;

public class VoidWeatherCommand {

    public static boolean flash = false;
    public static boolean bedrockFall = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("voidweather")
                        .requires(source -> true).then(Commands.literal("flash")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            flash = BoolArgumentType.getBool(context, "value");
                                            if (context.getSource().isPlayer()){

                                                ServerPlayer player = context.getSource().getPlayer();
                                                if (player.level().dimension() == NRegistry.THE_VOID){
                                                    if (flash){
                                                        player.level().getData(NRegistry.THE_VOID_ATTACHMENT.get()).startFlash(player.level());
                                                    }else {
                                                        player.level().getData(NRegistry.THE_VOID_ATTACHMENT.get()).stopFlash(player.level());
                                                    }
                                                    player.level().syncData(NRegistry.THE_VOID_ATTACHMENT.get());

                                                }
                                            }
                                            return 1;
                                        })
                                )
                        ).then(Commands.literal("bedrockfall")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            bedrockFall = BoolArgumentType.getBool(context, "value");
                                            if (context.getSource().isPlayer()){

                                                ServerPlayer player = context.getSource().getPlayer();
                                                if (player.level().dimension() == NRegistry.THE_VOID){
                                                    if (bedrockFall){
                                                        player.level().getData(NRegistry.THE_VOID_ATTACHMENT.get()).startBedrockFall(player.level());
                                                    }else {
                                                        player.level().getData(NRegistry.THE_VOID_ATTACHMENT.get()).stopBedrockFall(player.level());
                                                    }
                                                    player.level().syncData(NRegistry.THE_VOID_ATTACHMENT.get());

                                                }
                                            }

                                            return 1;
                                        })
                                )
                        )
        );
        dispatcher.register(
                Commands.literal("arachne")
                        .requires(source -> true).then(Commands.literal("setReputation")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0,100))
                                        .executes(context -> {
                                            int rep = IntegerArgumentType.getInteger(context, "value");
                                            if (context.getSource().isPlayer()){

                                                ServerPlayer player = context.getSource().getPlayer();
                                                ArachneAttachment.get(player).ifPresent(arachneAttachment -> {
                                                    arachneAttachment.setCurrentReputation(player,rep);
                                                    player.syncData(NRegistry.ARACNE);
                                                });
                                            }
                                            return 1;
                                        })
                                )
                        )
        );
    }
}