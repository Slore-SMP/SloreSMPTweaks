/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class DaisyGameRules {

    public static final GameRules.Key<GameRules.IntRule> CHAT_MINIGAME_INTERVAL =
            GameRuleRegistry.register("chatMinigameIntervalTicks", GameRules.Category.MISC, GameRuleFactory.createIntRule(20 * 300)); // default 5 min

    public static void init() {}
}
