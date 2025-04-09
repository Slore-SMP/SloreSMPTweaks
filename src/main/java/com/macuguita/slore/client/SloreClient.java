package com.macuguita.slore.client;

import com.macuguita.slore.SloreTweaks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;

public class SloreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(SloreTweaks.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SloreTweaks.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());
    }
}
