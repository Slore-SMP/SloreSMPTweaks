/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.client;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class DaisyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(DaisyParticles.GHOST_PARTICLE, SoulParticle.SculkSoulFactory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(DaisyObjects.METAL_SCAFFOLDING.get(), RenderLayer.getCutout());

        ClientPlayNetworking.registerGlobalReceiver(DaisyTweaks.SUSPICIOUS_MOD_LIST_ID, (client, handler, buf, responseSender) -> {
            int count = buf.readInt();
            List<String> modIdsToCheck = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                modIdsToCheck.add(buf.readString());
            }

            List<String> detected = modIdsToCheck.stream()
                    .filter(id -> FabricLoader.getInstance().isModLoaded(id))
                    .toList();

            if (!detected.isEmpty()) {
                PacketByteBuf reportBuf = PacketByteBufs.create();
                reportBuf.writeString(String.join(",", detected));
                ClientPlayNetworking.send(DaisyTweaks.HACKED_CLIENT_REPORT_ID, reportBuf);
            }
        });
    }
}
