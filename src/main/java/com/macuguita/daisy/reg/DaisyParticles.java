package com.macuguita.daisy.reg;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.macuguita.daisy.DaisyTweaks.id;

public class DaisyParticles {

    public static final DefaultParticleType GHOST_PARTICLE = FabricParticleTypes.simple();

    public static void init() {
        Registry.register(Registries.PARTICLE_TYPE, id("ghost"), GHOST_PARTICLE);
    }
}
