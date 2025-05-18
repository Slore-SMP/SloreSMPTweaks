/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.macuguita.daisy.DaisyTweaks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ChatMinigameConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("daisy/chat_minigames.json");

    private static ChatMinigameConfig.ChatMinigameConfiguration CONFIG = null;

    public static boolean getEnabled(QuestionType type) {
        return CONFIG != null ? CONFIG.enabledQuestions.get(type) : false;
    }

    public static String getQuestionIcon(QuestionType type) {
        return CONFIG != null ? CONFIG.questionIcons.get(type) : null;
    }

    public static String getQuestionPrompt(QuestionType type) {
        return CONFIG != null ? CONFIG.questionPrompt.get(type) : null;
    }

    public static String getForbiddenRegex() {
        return CONFIG != null ? CONFIG.forbiddenRegex : ".^";
    }

    public static List<String> getForbiddenModIds() {
        return CONFIG != null ? CONFIG.forbiddenModIds : List.of();
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                createDefaultConfig();
            }

            try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
                var json = JsonHelper.deserialize(reader); // no need for Gson parser here
                var result = ChatMinigameConfig.ChatMinigameConfiguration.CODEC.parse(JsonOps.INSTANCE, json);

                CONFIG = result.resultOrPartial(msg -> DaisyTweaks.LOGGER.error("[Chat Minigames] Config parse error: " + msg)).orElse(null);
            }

        } catch (Exception e) {
            DaisyTweaks.LOGGER.error("[Chat Minigames] Failed to load config:");
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() throws IOException {
        ChatMinigameConfig.ChatMinigameConfiguration defaultConfig = new ChatMinigameConfig.ChatMinigameConfiguration(
                Map.of(
                        QuestionType.UNSCRAMBLE_ITEM, true,
                        QuestionType.FILL_IN_THE_BLANKS, true,
                        QuestionType.REVERSE_ITEM, true,
                        QuestionType.DATA_DRIVEN, true
                ),
                Map.of(
                        QuestionType.UNSCRAMBLE_ITEM, "🌀",
                        QuestionType.FILL_IN_THE_BLANKS, "✏",
                        QuestionType.REVERSE_ITEM, "🔁",
                        QuestionType.DATA_DRIVEN, "❓"
                ),
                Map.of(
                        QuestionType.UNSCRAMBLE_ITEM, "Unscramble this Minecraft item: ",
                        QuestionType.FILL_IN_THE_BLANKS, "Fill in this Minecraft item: ",
                        QuestionType.REVERSE_ITEM, "What Minecraft item is this when reversed? "
                ),
                "(creative|debug)",
                List.of("everycomp", "stonezone")
        );

        var result = ChatMinigameConfig.ChatMinigameConfiguration.CODEC.encodeStart(JsonOps.INSTANCE, defaultConfig);
        var jsonElement = result.getOrThrow(false, msg -> DaisyTweaks.LOGGER.error("[Chat Minigames] Failed to encode default config: " + msg));

        if (!Files.exists(CONFIG_PATH.getParent())) {
            Files.createDirectories(CONFIG_PATH.getParent());
        }

        writePrettyJson(jsonElement, CONFIG_PATH);
        DaisyTweaks.LOGGER.info("[Chat Minigames] Created default config: " + CONFIG_PATH);
    }

    private static void writePrettyJson(JsonElement element, Path path) throws IOException {
        Writer stringWriter = Files.newBufferedWriter(path);
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.setIndent("  ");

        Streams.write(element, writer);
        writer.close();
    }

    public record ChatMinigameConfiguration(
            Map<QuestionType, Boolean> enabledQuestions,
            Map<QuestionType, String> questionIcons,
            Map<QuestionType, String> questionPrompt,
            String forbiddenRegex,
            List<String> forbiddenModIds
    ) {

        public static final Codec<ChatMinigameConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(QuestionType.CODEC, Codec.BOOL)
                        .fieldOf("enabled_questions")
                        .forGetter(ChatMinigameConfiguration::enabledQuestions),
                Codec.unboundedMap(QuestionType.CODEC, Codec.STRING)
                        .fieldOf("question_icons")
                        .forGetter(ChatMinigameConfiguration::questionIcons),
                Codec.unboundedMap(QuestionType.CODEC, Codec.STRING)
                        .fieldOf("question_prompt")
                        .forGetter(ChatMinigameConfiguration::questionPrompt),
                Codec.STRING
                        .fieldOf("forbidden_regex")
                        .forGetter(ChatMinigameConfiguration::forbiddenRegex),
                Codec.STRING
                        .listOf()
                        .fieldOf("forbidden_modids")
                        .forGetter(ChatMinigameConfiguration::forbiddenModIds)
        ).apply(instance, ChatMinigameConfiguration::new));
    }
}
