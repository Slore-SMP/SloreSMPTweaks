package com.macuguita.slore.client;

import com.macuguita.slore.reg.SloreObjects;
import com.macuguita.slore.reg.SloreParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;

public class SloreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(SloreParticles.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SloreObjects.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());
    }
}
