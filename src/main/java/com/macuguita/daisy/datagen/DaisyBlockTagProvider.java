/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import com.macuguita.daisy.reg.DaisyObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class DaisyBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public DaisyBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .add(DaisyObjects.METAL_SCAFFOLDING.get());
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(DaisyObjects.METAL_SCAFFOLDING.get())
                .add(DaisyObjects.NETHER_LANTERN.get());
    }
}
