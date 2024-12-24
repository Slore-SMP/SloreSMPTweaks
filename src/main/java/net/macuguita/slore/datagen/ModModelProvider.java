package net.macuguita.slore.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.macuguita.slore.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.GLASS_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BLOBFISH, Models.GENERATED);
        itemModelGenerator.register(ModItems.BIBLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PRESENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.LADYBRINES_FART, Models.GENERATED);
    }
}
