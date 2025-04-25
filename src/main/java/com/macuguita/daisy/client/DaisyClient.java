/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.client;

import com.macuguita.daisy.reg.DaisyBlockEntities;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class DaisyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(DaisyParticles.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(DaisyObjects.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());
    }
}
