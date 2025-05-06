package com.macuguita.daisy.utils;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class AntiCheatModConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("anticheat_config.json");

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
        obj.add("suspicious_mods", defaultMods);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(CONFIG_PATH, gson.toJson(obj));
        System.out.println("[AntiCheat] Created default config: " + CONFIG_PATH);
    }

    public static String getWebhookUrl() {
        return webhookUrl;
    }

    public static List<String> getSuspiciousMods() {
        return suspiciousMods;
    }
}
