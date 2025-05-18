/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy;

import com.macuguita.daisy.admin.AdminCommands;
import com.macuguita.daisy.chatminigame.ChatMinigame;
import com.macuguita.daisy.chatminigame.ChatMinigameCommands;
import com.macuguita.daisy.chatminigame.ChatMinigameConfig;
import com.macuguita.daisy.chatminigame.DatapackQuestionLoader;
import com.macuguita.daisy.components.DaisyComponents;
import com.macuguita.daisy.components.WelcomeComponent;
import com.macuguita.daisy.homestpa.HomesTpaCommands;
import com.macuguita.daisy.item.ReaperItem;
import com.macuguita.daisy.mixin.reaper.LivingEntityAccessor;
import com.macuguita.daisy.reg.DaisyBlockEntities;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisyParticles;
import com.macuguita.daisy.reg.DaisySounds;
import com.macuguita.daisy.utils.AntiCheatConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DaisyTweaks implements ModInitializer {

    public static final String MOD_ID = "daisy";
    public static final String MOD_NAME = "Daisy SMP Tweaks";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final GameRules.Key<GameRules.IntRule> MAX_CHARGE_TICKS =
            GameRuleRegistry.register("daisy:maxNetherLanternCharge", GameRules.Category.MISC, GameRuleFactory.createIntRule(360000));

    public static Identifier SUSPICIOUS_MOD_LIST_ID = id("sus_mods");
    public static Identifier HACKED_CLIENT_REPORT_ID = id("client_reported");

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) AntiCheatConfig.load();
        ChatMinigameConfig.load();
        DaisyObjects.init();
        DaisyBlockEntities.init();
        DaisyParticles.init();
        DaisySounds.init();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new DatapackQuestionLoader());
        ChatMinigame.init();
        ChatMinigameCommands.init();
        HomesTpaCommands.init();
        AdminCommands.init();

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof LivingEntity livingEntity && livingEntity.getMainHandStack().getItem() instanceof ReaperItem) {
                killedEntity.deathTime = 0;
                ((LivingEntityAccessor) killedEntity).daisy$setExperienceDroppingDisabled(false);
                ReaperItem.spawnGhostParticle(killedEntity);
                killedEntity.remove(Entity.RemovalReason.KILLED);
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // Welcome
            ServerPlayerEntity player = handler.player;
            WelcomeComponent component = DaisyComponents.WELCOME_COMPONENT.get(handler.player);
            if (!component.getHasJoined()) {
                component.setHasJoined(true);
                server.getPlayerManager().broadcast(Text.literal(player.getName().getString() + " has joined for the first time, say hi!").formatted(Formatting.YELLOW), false);
            }

            // Send forbidden mods
            List<String> suspiciousMods = AntiCheatConfig.getSuspiciousMods();

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(suspiciousMods.size());
            for (String modId : suspiciousMods) {
                buf.writeString(modId);
            }

            ServerPlayNetworking.send(handler.player, SUSPICIOUS_MOD_LIST_ID, buf);
        });
        ServerPlayNetworking.registerGlobalReceiver(HACKED_CLIENT_REPORT_ID, (server, player, handler, buf, responseSender) -> {
            String modList = buf.readString();
            server.execute(() -> {
                String webhook = AntiCheatConfig.getWebhookUrl();
                String alertMessage = AntiCheatConfig.getAlertMessage();
                if (webhook != null && alertMessage != null && webhook.startsWith("http")) {
                    //AntiCheatConfig.sendDiscordWebhook(webhook, "**" + player.getEntityName() + "**" + " joined with suspicious mods: " + "**" + modList + "**");
                    AntiCheatConfig.sendDiscordWebhook(webhook, String.format(alertMessage, player.getEntityName(), modList));
                }
            });
        });

    }
}
