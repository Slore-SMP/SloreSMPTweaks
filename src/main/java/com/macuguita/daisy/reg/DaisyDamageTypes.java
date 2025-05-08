/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import com.macuguita.daisy.DaisyTweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class DaisyDamageTypes {
    public static final RegistryKey<DamageType> REAPER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, DaisyTweaks.id("reaper"));

    public static DamageSource reaper(World world, Entity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(REAPER), attacker);
    }
}
