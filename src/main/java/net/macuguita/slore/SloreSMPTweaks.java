package net.macuguita.slore;

import net.fabricmc.api.ModInitializer;

import net.macuguita.slore.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SloreSMPTweaks implements ModInitializer {

	public static final String MOD_ID = "slore_tweaks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();


	}
}