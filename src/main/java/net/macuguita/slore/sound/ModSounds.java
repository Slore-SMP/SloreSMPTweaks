package net.macuguita.slore.sound;

import net.macuguita.slore.SloreSMPTweaks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent RACHEL = registerSoundEvent("rachel");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(SloreSMPTweaks.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSound() {
        SloreSMPTweaks.LOGGER.info("Registering sounds for " + SloreSMPTweaks.MOD_ID);
    }
}
