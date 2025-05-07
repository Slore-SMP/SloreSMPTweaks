/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.admin;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;
import java.util.function.Consumer;

public interface CustomWorldSaveHandler {

    DataResult<NbtCompound> daisy$edit(UUID uuid, Consumer<NbtCompound> editor);
}
