package com.macuguita.slore.datagen;

import com.macuguita.slore.reg.SloreTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class SloreItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public SloreItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SloreTags.Items.BREAKABLE);
        getOrCreateTagBuilder(SloreTags.Items.BUCKET_BLACKLIST)
                .add(Items.TROPICAL_FISH_BUCKET)
                .add(Items.AXOLOTL_BUCKET)
                .add(Items.COD_BUCKET)
                .add(Items.PUFFERFISH_BUCKET)
                .add(Items.SALMON_BUCKET)
                .add(Items.TADPOLE_BUCKET)
                .addOptional(new Identifier("spelunkery", "salt_bucket"))
                .addOptional(new Identifier("clutter", "levitating_echofin_bucket"))
                .addOptional(new Identifier("clutter", "chorus_echofin_bucket"));
        getOrCreateTagBuilder(SloreTags.Items.BUCKET)
                .add(Items.LAVA_BUCKET)
                .add(Items.MILK_BUCKET)
                .add(Items.POWDER_SNOW_BUCKET)
                .add(Items.WATER_BUCKET);
    }
}
