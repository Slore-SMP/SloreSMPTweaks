package com.macuguita.daisy.client;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.mixin.buckets.ItemAccessor;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DaisyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(DaisyParticles.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(DaisyObjects.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());

        // Here the client receives the new stack sizes and updates them to be like the server.
        ClientPlayNetworking.registerGlobalReceiver(DaisyTweaks.BUCKET_STACK_PACKET, (client, handler, buf, responseSender) -> {
            int count = buf.readVarInt();
            List<Identifier> ids = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                ids.add(buf.readIdentifier());
            }

            client.execute(() -> {
                for (Identifier id : ids) {
                    Item item = Registries.ITEM.get(id);
                    ((ItemAccessor) item).daisy$setMaxCount(16);
                }
            });
        });

    }
}
