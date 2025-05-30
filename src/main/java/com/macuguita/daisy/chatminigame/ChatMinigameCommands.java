/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatMinigameCommands {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(dispatcher);
        });
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("asktrivia")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ChatMinigame.askRandomQuestion(source.getServer(), false);
                    source.sendFeedback(() -> Text.literal("Chat question sent"), false);
                    return 1;
                })
                .then(CommandManager.argument("showAnswer", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean showAnswer = BoolArgumentType.getBool(context, "showAnswer");
                            ServerCommandSource source = context.getSource();
                            ChatMinigame.askRandomQuestion(source.getServer(), showAnswer);
                            source.sendFeedback(() -> Text.literal("Chat question sent"), false);
                            return 1;
                        })));
        dispatcher.register(CommandManager.literal("showanswer")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    source.sendFeedback(() -> Text.literal("Question answer is: ")
                                    .append(Text.literal(ChatMinigame.getAnswer()).formatted(Formatting.YELLOW)),
                            false);
                    if (source.getPlayer() != null) {
                        source.sendFeedback(() -> Text.literal(source.getPlayer().getName().getString() + " revealed the answer"), true);
                    }
                    return 1;
                }));
    }

}
