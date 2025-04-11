package com.macuguita.slore.datagen;

import com.macuguita.slore.reg.SloreObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class SloreRecipeProvider extends FabricRecipeProvider {
    public SloreRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, SloreObjects.METAL_SCAFFOLDING.get(), 6)
                .input('~', Blocks.IRON_BARS)
                .input('I', Items.IRON_INGOT)
                .pattern("I~I")
                .pattern("I I")
                .pattern("I I")
                .criterion(hasItem(Blocks.IRON_BARS.asItem()), conditionsFromItem(Blocks.IRON_BARS.asItem()))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(consumer);
    }
}
