package com.macuguita.slore.client;

import com.macuguita.slore.SloreTweaks;
import com.macuguita.slore.mixin.buckets.ItemAccessor;
import com.macuguita.slore.reg.SloreObjects;
import com.macuguita.slore.reg.SloreParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SloreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(SloreParticles.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SloreObjects.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());

        // Here the client receives the new stack sizes and updates them to be like the server.
        ClientPlayNetworking.registerGlobalReceiver(SloreTweaks.BUCKET_STACK_PACKET, (client, handler, buf, responseSender) -> {
            int count = buf.readVarInt();
            List<Identifier> ids = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                ids.add(buf.readIdentifier());
            }

            client.execute(() -> {
                for (Identifier id : ids) {
                    Item item = Registries.ITEM.get(id);
                    ((ItemAccessor) item).slore$setMaxCount(16);
                }
            });
        });

    }
}
