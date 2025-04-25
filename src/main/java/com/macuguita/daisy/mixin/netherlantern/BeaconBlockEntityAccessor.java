/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.netherlantern;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {
    @Accessor("level")
    int daisy$getLevel();

    @Accessor("beamSegments")
    List<BeaconBlockEntity.BeamSegment> daisy$getBeamSegments();

    @Accessor("primary")
    StatusEffect daisy$getPrimaryEffect();

    @Accessor("secondary")
    StatusEffect daisy$getSecondaryEffect();
}
