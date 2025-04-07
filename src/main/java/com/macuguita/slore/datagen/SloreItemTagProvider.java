package com.macuguita.slore.datagen;

import com.macuguita.slore.SloreTweaks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SloreItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public SloreItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SloreTweaks.BREAKABLE);
        getOrCreateTagBuilder(SloreTweaks.BUCKET_BLACKLIST)
                .add(Items.TROPICAL_FISH_BUCKET)
                .add(Items.AXOLOTL_BUCKET)
                .add(Items.COD_BUCKET)
                .add(Items.PUFFERFISH_BUCKET)
                .add(Items.SALMON_BUCKET)
                .add(Items.TADPOLE_BUCKET);
    }
}
