/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import com.macuguita.daisy.DaisyTweaks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

public class DaisySounds {

    public static final SoundEvent STEVE = registerSoundEvent("steve");
    public static final SoundEvent CRAFTING_TABLE = registerSoundEvent("crafting_table");
    public static final SoundEvent THE_OVERWORLD = registerSoundEvent("the_overworld");
    public static final SoundEvent ENDER_PEARL = registerSoundEvent("ender_pearl");
    public static final SoundEvent BLOCKS = registerSoundEvent("blocks");
    public static final SoundEvent ELYTRA = registerSoundEvent("elytra");
    public static final SoundEvent WATER_BUCKET = registerSoundEvent("water_bucket");
    public static final SoundEvent FLINT_AND_STEEL = registerSoundEvent("flint_and_steel");
    public static final SoundEvent THE_NETHER = registerSoundEvent("the_nether");
    public static final SoundEvent CHICKEN_JOCKEY = registerSoundEvent("chicken_jockey");

    public static final List<SoundEvent> MOVIE_SOUNDS = Arrays.asList(
            STEVE,
            CRAFTING_TABLE,
            THE_OVERWORLD,
            ENDER_PEARL,
            BLOCKS,
            ELYTRA,
            WATER_BUCKET,
            FLINT_AND_STEEL,
            THE_NETHER,
            CHICKEN_JOCKEY
    );

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = DaisyTweaks.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {}
}
