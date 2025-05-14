/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.reg.DaisyObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class DaisyRecipeProvider extends FabricRecipeProvider {
    public DaisyRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, DaisyObjects.METAL_SCAFFOLDING.get(), 6)
                .input('~', Blocks.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .pattern("I~I")
                .pattern("I I")
                .pattern("I I")
                .criterion(hasItem(Blocks.IRON_BARS.asItem()), conditionsFromItem(Blocks.IRON_BARS.asItem()))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, DaisyObjects.NETHER_LANTERN.get(), 1)
                .input('~', Items.NETHERITE_SCRAP)
                .input('I', Items.GOLD_INGOT)
                .input('v', Items.NETHER_STAR)
                .pattern("I~I")
                .pattern("~v~")
                .pattern("I~I")
                .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                .criterion(hasItem(Items.NETHER_STAR), conditionsFromItem(Items.NETHER_STAR))
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(consumer);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, DaisyObjects.NETHER_LANTERN.get(), 1)
                .input(DaisyObjects.NETHER_LANTERN.get())
                .criterion(hasItem(DaisyObjects.NETHER_LANTERN.get()), conditionsFromItem(DaisyObjects.NETHER_LANTERN.get()))
                .offerTo(consumer, DaisyTweaks.id("clean_nether_lantern"));
    }
}
