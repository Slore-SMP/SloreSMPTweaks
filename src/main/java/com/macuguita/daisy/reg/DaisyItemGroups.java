/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.utils.GuitaItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class DaisyItemGroups {

    public static final GuitaRegistry<ItemGroup> ITEM_GROUPS = GuitaRegistries.create(Registries.ITEM_GROUP, DaisyTweaks.MOD_ID);
    public static final Supplier<ItemGroup> DAISY_TAB = new GuitaItemGroup(new Identifier(DaisyTweaks.MOD_ID, "daisy_tweaks"))
            .setItemIcon(() -> Items.OXEYE_DAISY)
            .addRegistry(DaisyObjects.ITEMS)
            .build();

    public static void init() {
        ITEM_GROUPS.init();
    }
}
