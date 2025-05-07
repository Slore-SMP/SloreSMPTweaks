/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.admin;

import com.macuguita.daisy.admin.CustomWorldSaveHandler;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DataResult;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Util;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Consumer;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin implements CustomWorldSaveHandler {
    @Shadow @Final private File playerDataDir;

    @Shadow @Final protected DataFixer dataFixer;

    @Override
    public DataResult<NbtCompound> daisy$edit(UUID uuid, Consumer<NbtCompound> editor) {
        NbtCompound tag;

        try {
            File file = new File(this.playerDataDir, uuid.toString() + ".dat");
            if (file.exists() && file.isFile()) {
                tag = NbtIo.readCompressed(file.toPath().toFile());
            } else {
                return DataResult.error(() -> "Player data file for " + uuid + " does not exist");
            }
        } catch (Exception var4) {
            return DataResult.error(() -> "Failed to load player data for " + uuid);
        }

        int i = NbtHelper.getDataVersion(tag, -1);
        tag = DataFixTypes.PLAYER.update(this.dataFixer, tag, i);

        editor.accept(tag);
        try {
            Path path = this.playerDataDir.toPath();
            Path path2 = Files.createTempFile(path, uuid + "-", ".dat");
            NbtIo.writeCompressed(tag, path2.toFile());
            Path path3 = path.resolve(uuid + ".dat");
            Path path4 = path.resolve(uuid + ".dat_old");
            Util.backupAndReplace(path3, path2, path4);
        } catch (Exception var6) {
            return DataResult.error(() -> "Failed to save player data for " + uuid);
        }
        return DataResult.success(tag);
    }
}

