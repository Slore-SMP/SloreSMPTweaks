package com.macuguita.slore.client;

import com.macuguita.slore.SloreTweaks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.SoulParticle;

public class SloreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(SloreTweaks.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
    }
}
