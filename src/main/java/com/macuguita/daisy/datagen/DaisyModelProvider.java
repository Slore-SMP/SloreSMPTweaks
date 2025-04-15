package com.macuguita.daisy.datagen;

import com.macuguita.daisy.reg.DaisyObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import static net.minecraft.data.client.BlockStateModelGenerator.createBooleanModelMap;

public class DaisyModelProvider extends FabricModelProvider {
    public DaisyModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerScaffoldingLike(blockStateModelGenerator, DaisyObjects.METAL_SCAFFOLDING.get());
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(DaisyObjects.CALCITE_FROG_STATUE.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(DaisyObjects.REAPER.get(), Models.HANDHELD);
        itemModelGenerator.register(DaisyObjects.MOVIE_SCRIPT.get(), Models.GENERATED);
    }

    private void registerScaffoldingLike(BlockStateModelGenerator blockStateModelGenerator, Block block) {
        Identifier identifier = ModelIds.getBlockSubModelId(block, "_stable");
        Identifier identifier2 = ModelIds.getBlockSubModelId(block, "_unstable");
        blockStateModelGenerator.registerParentedItemModel(block, identifier);
        blockStateModelGenerator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(createBooleanModelMap(Properties.BOTTOM, identifier2, identifier)));
    }
}
