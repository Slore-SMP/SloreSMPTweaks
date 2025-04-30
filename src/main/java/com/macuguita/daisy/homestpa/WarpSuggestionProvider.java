/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.homestpa;

import com.macuguita.daisy.components.DaisyComponents;
import com.macuguita.daisy.components.WarpsComponent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class WarpSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            WarpsComponent homesComponent = DaisyComponents.WARPS_COMPONENT.get(context.getSource().getServer().getScoreboard());
            for (String homeName : homesComponent.getAllWarps().keySet()) {
                builder.suggest(homeName);
            }
        }
        return builder.buildFuture();
    }
}
