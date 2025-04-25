/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.block.entity.NetherLanternBlockEntity;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;

public class DaisyComponents implements BlockComponentInitializer {
    public static final ComponentKey<NetherLanternComponent> NETHER_LANTERN_COMPONENT = ComponentRegistry.getOrCreate(DaisyTweaks.id("nether_lantern"), NetherLanternComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry blockComponentFactoryRegistry) {
        blockComponentFactoryRegistry.registerFor(NetherLanternBlockEntity.class, NETHER_LANTERN_COMPONENT, NetherLanternComponent::new);
    }
}
