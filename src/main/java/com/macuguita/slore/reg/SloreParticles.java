package com.macuguita.slore.reg;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.macuguita.slore.SloreTweaks.id;

public class SloreParticles {

    public static final DefaultParticleType GHOST_PARTICLE = FabricParticleTypes.simple();

    public static void init() {
        Registry.register(Registries.PARTICLE_TYPE, id("ghost"), GHOST_PARTICLE);
    }
}
