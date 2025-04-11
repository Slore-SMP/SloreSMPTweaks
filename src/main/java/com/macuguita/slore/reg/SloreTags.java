package com.macuguita.slore.reg;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import static com.macuguita.slore.SloreTweaks.id;

public class SloreTags {

    public static class Items {

        public static final TagKey<Item> BREAKABLE = registerTagKey("breakable");
        public static final TagKey<Item> BUCKET = registerTagKey("bucket");
        public static final TagKey<Item> BUCKET_BLACKLIST = registerTagKey("bucket_blacklist");

        public static TagKey<Item> registerTagKey(String name) {
            return TagKey.of(RegistryKeys.ITEM, id(name));
        }
    }

    public static class Blocks {

    }
}
