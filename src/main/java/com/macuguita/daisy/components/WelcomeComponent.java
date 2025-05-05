/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class WelcomeComponent implements Component {
    private static PlayerEntity player;
    private boolean hasJoined = false;

    public WelcomeComponent(PlayerEntity player) {
        this.player = player;
    }

    public boolean getHasJoined() {
        return hasJoined;
    }

    public void setHasJoined(boolean hasJoined) {
        this.hasJoined = hasJoined;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.hasJoined = nbt.getBoolean("HasJoined");
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("HasJoined", this.hasJoined);
    }
}
