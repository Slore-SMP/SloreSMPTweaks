/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.homestpa;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TpacceptSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

    private final Map<ServerPlayerEntity, ServerPlayerEntity> pendingRequestsTo;
    private final Map<ServerPlayerEntity, ServerPlayerEntity> pendingRequestsHere;

    public TpacceptSuggestionProvider(
            Map<ServerPlayerEntity, ServerPlayerEntity> pendingRequestsTo,
            Map<ServerPlayerEntity, ServerPlayerEntity> pendingRequestsHere
    ) {
        this.pendingRequestsTo = pendingRequestsTo;
        this.pendingRequestsHere = pendingRequestsHere;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(
            CommandContext<ServerCommandSource> context,
            SuggestionsBuilder builder
    ) {
        ServerPlayerEntity acceptor;
        try {
            acceptor = context.getSource().getPlayer();
        } catch (Exception e) {
            return builder.buildFuture();
        }

        String remaining = builder.getRemaining().toLowerCase();

        for (Map.Entry<ServerPlayerEntity, ServerPlayerEntity> entry : pendingRequestsTo.entrySet()) {
            if (entry.getValue().equals(acceptor)) {
                String name = entry.getKey().getName().getString();
                if (name.toLowerCase().startsWith(remaining)) {
                    builder.suggest(name);
                }
            }
        }

        if (pendingRequestsHere.containsKey(acceptor)) {
            ServerPlayerEntity requester = pendingRequestsHere.get(acceptor);
            String name = requester.getName().getString();
            if (name.toLowerCase().startsWith(remaining)) {
                builder.suggest(name);
            }
        }

        return builder.buildFuture();
    }
}
