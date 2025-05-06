package com.macuguita.daisy.utils;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class AntiCheat {

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("daisy_anticheat.json");

    private static String webhookUrl = null;
    private static List<String> suspiciousMods = List.of();

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                createDefaultConfig();
            }

            String json = Files.readString(CONFIG_PATH);
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

            if (obj.has("webhook_url")) {
                webhookUrl = obj.get("webhook_url").getAsString();
            }

            if (obj.has("suspicious_mods")) {
                JsonArray modsArray = obj.getAsJsonArray("suspicious_mods");
                suspiciousMods = new ArrayList<>();
                for (JsonElement el : modsArray) {
                    suspiciousMods.add(el.getAsString());
                }
            }

        } catch (Exception e) {
            System.err.println("[AntiCheat] Failed to load config:");
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("webhook_url", "PUT_YOUR_WEBHOOK_HERE");

        JsonArray defaultMods = new JsonArray();
        defaultMods.add("wurst");
        defaultMods.add("meteor-client");
        defaultMods.add("impact");
        defaultMods.add("aristosis");
        defaultMods.add("flux");
        defaultMods.add("salhack");
        defaultMods.add("future-client");
        defaultMods.add("creeper-client");
        defaultMods.add("lambda");
        obj.add("suspicious_mods", defaultMods);

        // Ensure config directory exists before writing
        Path configDir = FabricLoader.getInstance().getConfigDir();
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);  // Create directories if not exist
        }

        // Write the file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(CONFIG_PATH, gson.toJson(obj));
        System.out.println("[AntiCheat] Created default config: " + CONFIG_PATH);
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
        return webhookUrl;
    }

    public static List<String> getSuspiciousMods() {
        return suspiciousMods;
    }

    private static String escapeJson(String message) {
        // Escape characters that would break JSON formatting
        return message.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}
