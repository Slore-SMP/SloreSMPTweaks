/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.reg;

import com.macuguita.daisy.block.FrogStatueBlock;
import com.macuguita.daisy.block.MetalScaffoldingBlock;
import com.macuguita.daisy.item.MetalScaffoldingItem;
import com.macuguita.daisy.item.MovieScriptItem;
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
    public static final GuitaRegistry<Block> BLOCKS = GuitaRegistries.create(Registries.BLOCK, MOD_ID);

    public static final GuitaRegistryEntry<MetalScaffoldingBlock> METAL_SCAFFOLDING = registerMetalScaffoldingWithItem("metal_scaffolding", () -> new MetalScaffoldingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .noCollision()
                    .sounds(BlockSoundGroup.NETHERITE)
                    .dynamicBounds()
                    .allowsSpawning(Blocks::never)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)
    ));

    public static final GuitaRegistryEntry<ReaperItem> REAPER = ITEMS.register("reaper", () -> new ReaperItem(
            (float) Integer.MAX_VALUE, 1, ToolMaterials.NETHERITE, BlockTags.HOE_MINEABLE,
            new Item.Settings().maxCount(1).fireproof().maxDamage(-1)));

    public static final GuitaRegistryEntry<MovieScriptItem> MOVIE_SCRIPT = ITEMS.register("movie_script", () -> new MovieScriptItem(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));

    public static final GuitaRegistryEntry<FrogStatueBlock> CALCITE_FROG_STATUE = registerBlock("calcite_frog_statue", () -> new FrogStatueBlock(AbstractBlock.Settings.copy(Blocks.CALCITE).nonOpaque()));

    public static  <T extends Block> GuitaRegistryEntry<T> registerBlock(String name, Supplier<T> block) {
        GuitaRegistryEntry<T> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Settings()));
        return toReturn;
    }

    public static <T extends Block> GuitaRegistryEntry<T> registerMetalScaffoldingWithItem(String name, Supplier<T> block) {
        GuitaRegistryEntry<T> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new MetalScaffoldingItem(toReturn.get(), new Item.Settings()));
        return toReturn;
    }

    public static void init(){
        BLOCKS.init();
        ITEMS.init();
    }
}
