/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.block.BlockDetectorBlock;
import com.macuguita.daisy.block.BulbBlock;
import com.macuguita.daisy.block.NetherLanternBlock;
import com.macuguita.daisy.reg.DaisyObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
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
        registerBlockDetectorModels(blockStateModelGenerator, DaisyObjects.BLOCK_DETECTOR.get());
        registerBulbModels(blockStateModelGenerator, DaisyObjects.AMETHYST_BULB.get());
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

    private void registerBlockDetectorModels(BlockStateModelGenerator generator, Block block) {
        Identifier normalModel = ModelIds.getBlockSubModelId(block, "");
        Identifier onModel = ModelIds.getBlockSubModelId(block, "_on");

        generator.registerParentedItemModel(block, normalModel);

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateVariantMap.create(BlockDetectorBlock.AXIS, BlockDetectorBlock.POWERED)
                                .register((axis, powered) -> {
                                    Identifier model = powered ? onModel : normalModel;
                                    BlockStateVariant variant = BlockStateVariant.create().put(VariantSettings.MODEL, model);

                                    return switch (axis) {
                                        case X -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                                        case Y -> variant.put(VariantSettings.X, VariantSettings.Rotation.R90);
                                        case Z -> variant;
                                    };
                                })
                        )
        );
    }

    private void registerBulbModels(BlockStateModelGenerator generator, Block block) {
        var id = Registries.BLOCK.getId(block);
        Identifier normalModel = ModelIds.getBlockSubModelId(block, "");
        Identifier litModel = ModelIds.getBlockSubModelId(block, "_lit");
        Identifier poweredModel = ModelIds.getBlockSubModelId(block, "_powered");
        Identifier poweredLitModel = ModelIds.getBlockSubModelId(block, "_powered_lit");

        uploadCubeAll(generator, normalModel, id.getPath());
        uploadCubeAll(generator, litModel, id.getPath() + "_lit");
        uploadCubeAll(generator, poweredModel, id.getPath() + "_powered");
        uploadCubeAll(generator, poweredLitModel, id.getPath() + "_powered_lit");

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateVariantMap.create(BulbBlock.LIT, BulbBlock.POWERED)
                                .register((lit, powered) -> {
                                    Identifier model;
                                    if (powered) {
                                        model = lit ? poweredLitModel : poweredModel;
                                    } else {
                                        model = lit ? litModel : normalModel;
                                    }
                                    return BlockStateVariant.create().put(VariantSettings.MODEL, model);
                                })
                        )
        );
    }

    private void uploadCubeAll(BlockStateModelGenerator generator, Identifier modelId, String texturePath) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, DaisyTweaks.id("block/" + texturePath));

        Models.CUBE_ALL.upload(modelId, textureMap, generator.modelCollector);
    }
}
