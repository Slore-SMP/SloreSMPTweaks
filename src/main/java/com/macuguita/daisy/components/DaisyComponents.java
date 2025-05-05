/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.block.entity.NetherLanternBlockEntity;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class DaisyComponents implements BlockComponentInitializer, EntityComponentInitializer, ScoreboardComponentInitializer {
    public static final ComponentKey<NetherLanternComponent> NETHER_LANTERN_COMPONENT =
            ComponentRegistry.getOrCreate(DaisyTweaks.id("nether_lantern"), NetherLanternComponent.class);

    public static final ComponentKey<HomesComponent> HOMES_COMPONENT =
            ComponentRegistry.getOrCreate(DaisyTweaks.id("homes"), HomesComponent.class);

    public static final ComponentKey<WarpsComponent> WARPS_COMPONENT =
            ComponentRegistry.getOrCreate(DaisyTweaks.id("warps"), WarpsComponent.class);

    public static final ComponentKey<WelcomeComponent> WELCOME_COMPONENT =
            ComponentRegistry.getOrCreate(DaisyTweaks.id("welcome"), WelcomeComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry blockComponentFactoryRegistry) {
        blockComponentFactoryRegistry.registerFor(
                NetherLanternBlockEntity.class,
                NETHER_LANTERN_COMPONENT,
                NetherLanternComponent::new
        );
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(
                DaisyComponents.HOMES_COMPONENT,
                HomesComponent::new,
                RespawnCopyStrategy.CHARACTER
        );
        entityComponentFactoryRegistry.registerForPlayers(
                DaisyComponents.WELCOME_COMPONENT,
                WelcomeComponent::new,
                RespawnCopyStrategy.CHARACTER
        );
    }

    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry scoreboardComponentFactoryRegistry) {
        scoreboardComponentFactoryRegistry.registerScoreboardComponent(
            DaisyComponents.WARPS_COMPONENT,
            WarpsComponent::new
        );
    }
}
