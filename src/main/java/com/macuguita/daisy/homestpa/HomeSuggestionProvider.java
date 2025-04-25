/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.homestpa;

import com.macuguita.daisy.components.DaisyComponents;
import com.macuguita.daisy.components.HomesComponent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class HomeSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            HomesComponent homesComponent = DaisyComponents.HOMES_COMPONENT.get(player);
            for (String homeName : homesComponent.getAllHomes().keySet()) {
                builder.suggest(homeName);
            }
        }
        return builder.buildFuture();
    }
}
