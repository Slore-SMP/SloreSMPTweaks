/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.block.*;
import com.macuguita.daisy.item.MetalScaffoldingItem;
import com.macuguita.daisy.item.MovieScriptItem;
import com.macuguita.daisy.item.PrizeItem;
import com.macuguita.daisy.item.ReaperItem;
import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Rarity;

import java.util.function.Supplier;

import static com.macuguita.daisy.DaisyTweaks.MOD_ID;

public class DaisyObjects {

    public static final GuitaRegistry<Item> ITEMS = GuitaRegistries.create(Registries.ITEM, MOD_ID);
    public static final GuitaRegistry<Item> CREATIVE_ITEMS = GuitaRegistries.create(Registries.ITEM, MOD_ID);
    public static final GuitaRegistry<Block> BLOCKS = GuitaRegistries.create(Registries.BLOCK, MOD_ID);

    public static final GuitaRegistryEntry<Item> REAPER = CREATIVE_ITEMS.register("reaper", () -> new ReaperItem(
            (float) Integer.MAX_VALUE, 1, ToolMaterials.NETHERITE, BlockTags.HOE_MINEABLE,
            new Item.Settings().maxCount(1).fireproof().maxDamage(-1)));

    public static final GuitaRegistryEntry<Block> CALCITE_FROG_STATUE = registerCreativeBlock("calcite_frog_statue", () -> new FrogStatueBlock(AbstractBlock.Settings.copy(Blocks.CALCITE).nonOpaque()));

    public static final GuitaRegistryEntry<Block> NETHER_LANTERN = registerBlock("nether_lantern", () -> new NetherLanternBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).nonOpaque()), 1);

    public static final GuitaRegistryEntry<Block> BLOCK_DETECTOR = registerBlock("block_detector", () -> new BlockDetectorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)));

    public static final GuitaRegistryEntry<Block> AMETHYST_BULB = registerBlock("amethyst_bulb", () -> new BulbBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)));

    public static final GuitaRegistryEntry<Block> METAL_SCAFFOLDING = registerMetalScaffoldingWithItem("metal_scaffolding", () -> new MetalScaffoldingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .noCollision()
                    .sounds(BlockSoundGroup.NETHERITE)
                    .dynamicBounds()
                    .allowsSpawning(Blocks::never)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)
    ));

    public static final GuitaRegistryEntry<Item> PRIZE_BAG = ITEMS.register("prize_bag", () -> new PrizeItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE), DaisyTweaks.id("prizes/prize")));

    public static final GuitaRegistryEntry<Item> MOVIE_SCRIPT = ITEMS.register("movie_script", () -> new MovieScriptItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));

    public static GuitaRegistryEntry<Block> registerBlock(String name, Supplier<Block> block) {
        return registerBlock(name, block, 64);
    }

    public static GuitaRegistryEntry<Block> registerBlock(String name, Supplier<Block> block, int maxStackSize) {
        GuitaRegistryEntry<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Settings().maxCount(maxStackSize)));
        return toReturn;
    }

    public static GuitaRegistryEntry<Block> registerCreativeBlock(String name, Supplier<Block> block) {
        return registerCreativeBlock(name, block, 64);
    }

    public static GuitaRegistryEntry<Block> registerCreativeBlock(String name, Supplier<Block> block, int maxStackSize) {
        GuitaRegistryEntry<Block> toReturn = BLOCKS.register(name, block);
        CREATIVE_ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Settings().maxCount(maxStackSize)));
        return toReturn;
    }

    public static GuitaRegistryEntry<Block> registerMetalScaffoldingWithItem(String name, Supplier<Block> block) {
        GuitaRegistryEntry<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new MetalScaffoldingItem(toReturn.get(), new Item.Settings()));
        return toReturn;
    }

    public static void init() {
        BLOCKS.init();
        ITEMS.init();
    }
}
