/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import com.macuguita.daisy.block.entity.NetherLanternBlockEntity;
import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;

import static com.macuguita.daisy.DaisyTweaks.MOD_ID;

public class DaisyBlockEntities {

    public static final GuitaRegistry<BlockEntityType<?>> BLOCK_ENTITIES = GuitaRegistries.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);


    public static final GuitaRegistryEntry<BlockEntityType<NetherLanternBlockEntity>> NETHER_LANTERN = BLOCK_ENTITIES.register(
            "nether_lantern", () -> BlockEntityType.Builder.create(NetherLanternBlockEntity::new, DaisyObjects.NETHER_LANTERN.get()).build(null));

    public static void init() {
        BLOCK_ENTITIES.init();
    }
}
