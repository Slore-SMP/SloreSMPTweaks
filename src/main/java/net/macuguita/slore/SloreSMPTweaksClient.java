package net.macuguita.slore;

import net.fabricmc.api.ClientModInitializer;
import net.macuguita.slore.utils.ExperienceContainerModelPredicate;

public class SloreSMPTweaksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ExperienceContainerModelPredicate.registerModels();
    }
}
