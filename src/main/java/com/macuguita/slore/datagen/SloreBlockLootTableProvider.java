package com.macuguita.slore.datagen;

import com.macuguita.slore.SloreTweaks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class SloreBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected SloreBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(SloreTweaks.METAL_SCAFFOLDING.get());
    }
}
