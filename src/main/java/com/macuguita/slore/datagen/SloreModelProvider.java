package com.macuguita.slore.datagen;

import com.macuguita.slore.SloreTweaks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class SloreModelProvider extends FabricModelProvider {
    public SloreModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(SloreTweaks.REAPER.get(), Models.HANDHELD);
        itemModelGenerator.register(SloreTweaks.DEBUG_ITEM.get(), Models.GENERATED);
    }
}
