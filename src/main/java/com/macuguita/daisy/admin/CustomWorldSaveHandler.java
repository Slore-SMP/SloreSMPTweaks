/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.admin;

import com.macuguita.daisy.homestpa.HomeLocation;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface CustomWorldSaveHandler {

    DataResult<NbtCompound> daisy$edit(UUID uuid, Consumer<NbtCompound> editor);

    BlockPos daisy$getPos(UUID uuid);

    Map<String, HomeLocation> daisy$getHomes(UUID uuid);
}
