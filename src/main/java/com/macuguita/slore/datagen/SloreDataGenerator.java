package com.macuguita.slore.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SloreDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(SloreBlockLootTableProvider::new);
		pack.addProvider(SloreBlockTagProvider::new);
		pack.addProvider(SloreItemTagProvider::new);
		pack.addProvider(SloreLanguajeProvider::new);
		pack.addProvider(SloreModelProvider::new);
		pack.addProvider(SloreRecipeProvider::new);
	}
}
