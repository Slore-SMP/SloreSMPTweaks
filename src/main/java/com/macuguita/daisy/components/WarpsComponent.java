/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import com.macuguita.daisy.homestpa.HomeLocation;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class WarpsComponent implements Component {
    private final Map<String, HomeLocation> warpList = new HashMap<>();
    private final Scoreboard provider;

    public WarpsComponent(Scoreboard provider, @Nullable MinecraftServer server) {
        this.provider = provider;
    }

    public Map<String, HomeLocation> getWarpList() {
        return warpList;
    }

    public HomeLocation getWarp(String name) {
        return warpList.get(name);
    }

    public Map<String, HomeLocation> getAllWarps() {
        return new HashMap<>(warpList);
    }

    public boolean addWarp(String name, BlockPos pos, RegistryKey<World> dimension) {
        return warpList.put(name, new HomeLocation(pos, dimension)) != null;
    }

    public boolean removeWarp(String name) {
        return warpList.remove(name) != null;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        warpList.clear();
        NbtList warpsNbtList = nbt.getList("Warps", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < warpsNbtList.size(); i++) {
            NbtCompound warpTag = warpsNbtList.getCompound(i);
            String name = warpTag.getString("Name");
            int x = warpTag.getInt("X");
            int y = warpTag.getInt("Y");
            int z = warpTag.getInt("Z");
            String dimensionId = warpTag.getString("Dimension");
            RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(dimensionId));
            warpList.put(name, new HomeLocation(new BlockPos(x, y, z), dimension));
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        NbtList warpsNbtList = new NbtList();
        for (Map.Entry<String, HomeLocation> entry : warpList.entrySet()) {
            NbtCompound warpTag = new NbtCompound();
            warpTag.putString("Name", entry.getKey());
            BlockPos pos = entry.getValue().getPosition();
            warpTag.putInt("X", pos.getX());
            warpTag.putInt("Y", pos.getY());
            warpTag.putInt("Z", pos.getZ());
            warpTag.putString("Dimension", entry.getValue().getDimension().getValue().toString());
            warpsNbtList.add(warpTag);
        }
        nbt.put("Warps", warpsNbtList);
    }
}
