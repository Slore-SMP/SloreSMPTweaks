/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.utils;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class AntiCheat {

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("daisy_anticheat.json");

    private static AntiCheatConfig CONFIG = null;

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                createDefaultConfig();
            }

            try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
                var json = JsonHelper.deserialize(reader); // no need for Gson parser here
                var result = AntiCheatConfig.CODEC.parse(JsonOps.INSTANCE, json);

                CONFIG = result.resultOrPartial(msg -> System.err.println("[AntiCheat] Config parse error: " + msg)).orElse(null);
            }

        } catch (Exception e) {
            System.err.println("[AntiCheat] Failed to load config:");
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() throws IOException {
        AntiCheatConfig defaultConfig = new AntiCheatConfig(
                "PUT_YOUR_WEBHOOK_HERE",
                List.of("wurst", "meteor-client", "impact", "aristosis", "flux", "salhack", "future-client", "creeper-client", "lambda")
        );

        var result = AntiCheatConfig.CODEC.encodeStart(JsonOps.INSTANCE, defaultConfig);
        var jsonElement = result.getOrThrow(false, msg -> System.err.println("[AntiCheat] Failed to encode default config: " + msg));

        Path configDir = FabricLoader.getInstance().getConfigDir();
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        writePrettyJson(jsonElement, CONFIG_PATH);
        System.out.println("[AntiCheat] Created default config: " + CONFIG_PATH);
    }

    private static void writePrettyJson(JsonElement element, Path path) throws IOException {
        Writer stringWriter = Files.newBufferedWriter(path);
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.setIndent("  ");

        Streams.write(element, writer);
        writer.close();
    }

    public static void sendDiscordWebhook(String webhookUrl, String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonPayload = String.format("{\"content\": \"%s\"}", escapeJson(message));

            byte[] postData = jsonPayload.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", String.valueOf(postData.length));

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 204 && responseCode != 200) {
                System.err.println("[AntiCheat] Failed to send webhook, response code: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("[AntiCheat] Error sending webhook:");
            e.printStackTrace();
        }
    }

    public static String getWebhookUrl() {
        return CONFIG != null ? CONFIG.webhookUrl() : null;
    }

    public static List<String> getSuspiciousMods() {
        return CONFIG != null ? CONFIG.suspiciousMods() : List.of();
    }


    private static String escapeJson(String message) {
        return message.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    public record AntiCheatConfig(String webhookUrl, List<String> suspiciousMods) {
        public static final Codec<AntiCheatConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("webhook_url").forGetter(AntiCheatConfig::webhookUrl),
                Codec.STRING.listOf().fieldOf("suspicious_mods").forGetter(AntiCheatConfig::suspiciousMods)
        ).apply(instance, AntiCheatConfig::new));
    }
}
