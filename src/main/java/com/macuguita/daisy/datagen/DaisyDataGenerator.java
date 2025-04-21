/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DaisyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(DaisyBlockLootTableProvider::new);
		pack.addProvider(DaisyBlockTagProvider::new);
		pack.addProvider(DaisyItemTagProvider::new);
		pack.addProvider(DaisyLanguajeProvider::new);
		pack.addProvider(DaisyModelProvider::new);
		pack.addProvider(DaisyRecipeProvider::new);
	}
}
