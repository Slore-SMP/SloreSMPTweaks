/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.homestpa;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HomeLocation {
    private final BlockPos position;
    private final RegistryKey<World> dimension;

    public HomeLocation(BlockPos position, RegistryKey<World> dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    public BlockPos getPosition() {
        return position;
    }

    public RegistryKey<World> getDimension() {
        return dimension;
    }
}
