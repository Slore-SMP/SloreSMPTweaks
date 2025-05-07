/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.admin;

import com.macuguita.daisy.mixin.admin.PlayerManagerAccessor;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class AdminCommands {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(dispatcher);
        });
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("offlinetp")
                .requires(context -> context.hasPermissionLevel(2))
                .then(CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                .executes(context -> {
                                    Collection<GameProfile> profiles = GameProfileArgumentType.getProfileArgument(context, "player");
                                    if (profiles.size() != 1) {
                                        return 0;
                                    }
                                    MinecraftServer server = context.getSource().getServer();
                                    GameProfile profile = profiles.iterator().next();
                                    BlockPos pos = BlockPosArgumentType.getLoadedBlockPos(context, "pos");

                                    if (server.getPlayerManager().getPlayer(profile.getId()) != null) {
                                        return 0;
                                    }

                                    CustomWorldSaveHandler handler = (CustomWorldSaveHandler) ((PlayerManagerAccessor) server.getPlayerManager()).daisy$getSaveHandler();
                                    handler.daisy$edit(profile.getId(), tag -> {
                                        tag.put("Pos", newDoubleList(pos.getX(), pos.getY(), pos.getZ()));
                                        tag.putString("Dimension", context.getSource().getWorld().getRegistryKey().toString());
                                    });
                                    context.getSource().sendFeedback(() -> Text.literal("Teleported " + profile.getName() + " to: " + " " +pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), true);
                                    return 1;
                                })
                        )
                ));
    }

    protected static NbtList newDoubleList(double... numbers) {
        NbtList nbtList = new NbtList();
        for (double d : numbers) {
            nbtList.add(NbtDouble.of(d));
        }

        return nbtList;
    }
}
