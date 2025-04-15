package com.macuguita.daisy.datagen;

import com.macuguita.daisy.reg.DaisyObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class DaisyBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected DaisyBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(DaisyObjects.METAL_SCAFFOLDING.get());
    }
}
