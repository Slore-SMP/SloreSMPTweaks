/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import com.macuguita.daisy.homestpa.HomeLocation;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class HomesComponent implements Component {
    private final PlayerEntity player;
    private final Map<String, HomeLocation> homeList = new HashMap<>();
    private int maxHomes = 3;

    public HomesComponent(PlayerEntity player) {
        this.player = player;
    }

    public void addHome(String name, BlockPos pos, RegistryKey<World> dimension) {
        homeList.put(name, new HomeLocation(pos, dimension));
    }

    public HomeLocation getHome(String name) {
        return homeList.get(name);
    }

    public Map<String, HomeLocation> getAllHomes() {
        return new HashMap<>(homeList);
    }

    public boolean removeHome(String name) {
        return homeList.remove(name) != null;
    }

    public int getMaxHomes() {
        return maxHomes;
    }

    public void setMaxHomes(int maxHomes) {
        this.maxHomes = maxHomes;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.maxHomes = nbt.getInt("MaxHomes");
        homeList.clear();
        NbtList homesNbtList = nbt.getList("Homes", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < homesNbtList.size(); i++) {
            NbtCompound homeTag = homesNbtList.getCompound(i);
            String name = homeTag.getString("Name");
            int x = homeTag.getInt("X");
            int y = homeTag.getInt("Y");
            int z = homeTag.getInt("Z");
            String dimensionId = homeTag.getString("Dimension");
            RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(dimensionId));
            homeList.put(name, new HomeLocation(new BlockPos(x, y, z), dimension));
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("MaxHomes", this.maxHomes);
        NbtList homesNbtList = new NbtList();
        for (Map.Entry<String, HomeLocation> entry : homeList.entrySet()) {
            NbtCompound homeTag = new NbtCompound();
            homeTag.putString("Name", entry.getKey());
            BlockPos pos = entry.getValue().getPosition();
            homeTag.putInt("X", pos.getX());
            homeTag.putInt("Y", pos.getY());
            homeTag.putInt("Z", pos.getZ());
            homeTag.putString("Dimension", entry.getValue().getDimension().getValue().toString());
            homesNbtList.add(homeTag);
        }
        nbt.put("Homes", homesNbtList);
    }
}
