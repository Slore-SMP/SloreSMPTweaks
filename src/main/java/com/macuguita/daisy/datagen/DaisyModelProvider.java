/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import com.macuguita.daisy.block.NetherLanternBlock;
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
        registerNetherLanternModels(blockStateModelGenerator, DaisyObjects.NETHER_LANTERN.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(DaisyObjects.REAPER.get(), Models.HANDHELD);
        itemModelGenerator.register(DaisyObjects.MOVIE_SCRIPT.get(), Models.GENERATED);
        itemModelGenerator.register(DaisyObjects.PRIZE_BAG.get(), Models.GENERATED);
    }

    private void registerScaffoldingLike(BlockStateModelGenerator blockStateModelGenerator, Block block) {
        Identifier identifier = ModelIds.getBlockSubModelId(block, "_stable");
        Identifier identifier2 = ModelIds.getBlockSubModelId(block, "_unstable");
        blockStateModelGenerator.registerParentedItemModel(block, identifier);
        blockStateModelGenerator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block).coordinate(createBooleanModelMap(Properties.BOTTOM, identifier2, identifier)));
    }

    private void registerNetherLanternModels(BlockStateModelGenerator blockStateModelGenerator, Block block) {
        // Define model identifiers
        Identifier normalModel = ModelIds.getBlockSubModelId(block, "");
        Identifier chargedModel = ModelIds.getBlockSubModelId(block, "_charged");
        Identifier chargingModel = ModelIds.getBlockSubModelId(block, "_charging");

        // Register item model (uses default normal model)
        blockStateModelGenerator.registerParentedItemModel(block, normalModel);

        // Register block state variants
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateVariantMap.create(NetherLanternBlock.CHARGE_STATE)
                                .register((chargeState) -> switch (chargeState) {
                                    case 2 ->  // Charging state
                                            BlockStateVariant.create()
                                                    .put(VariantSettings.MODEL, chargingModel);
                                    case 1 ->  // Charged state
                                            BlockStateVariant.create()
                                                    .put(VariantSettings.MODEL, chargedModel);
                                    default -> // Default state (0)
                                            BlockStateVariant.create()
                                                    .put(VariantSettings.MODEL, normalModel);
                                })
                        )
        );
    }
}
