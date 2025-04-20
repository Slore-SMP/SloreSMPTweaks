package com.macuguita.daisy.client;

import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;

public class DaisyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(DaisyParticles.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(DaisyObjects.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());
    }
}
