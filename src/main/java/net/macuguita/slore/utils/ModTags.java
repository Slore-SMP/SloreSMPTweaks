package net.macuguita.slore.utils;

import net.macuguita.slore.SloreSMPTweaks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static TagKey<Block> NEEDS_PINK_GLASS_TOOL = createTag("needs_glass_tool");
        public static TagKey<Block> INCORRECT_FOR_GLASS_TOOL = createTag("incorrect_for_glass_tool");


        private static TagKey<Block> createTag(String name){
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(SloreSMPTweaks.MOD_ID, name));
        }
    }

    public static class Items {
        public static TagKey<Item> PRESENT_BLACKLIST = createTag("present_blacklist");

        private static TagKey<Item> createTag(String name){
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(SloreSMPTweaks.MOD_ID, name));
        }
    }
}
