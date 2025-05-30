/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import static com.macuguita.daisy.DaisyTweaks.id;

public class DaisyTags {

    public static class Items {

        public static TagKey<Item> registerTagKey(String name) {
            return TagKey.of(RegistryKeys.ITEM, id(name));
        }
    }

    public static class Blocks {

    }
}
