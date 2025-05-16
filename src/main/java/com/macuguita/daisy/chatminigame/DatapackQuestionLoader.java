/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

import com.macuguita.daisy.DaisyTweaks;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatapackQuestionLoader implements SimpleSynchronousResourceReloadListener {

    public static final List<ChatMinigame.Question> DATA_QUESTIONS = new ArrayList<>();
    private static final Identifier ID = DaisyTweaks.id("datapack_question_loader");
    private static final Identifier QUESTIONS_DIR = DaisyTweaks.id("chat_minigame_questions");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        DATA_QUESTIONS.clear();

        var resources = manager.findResources(QUESTIONS_DIR.getPath(), path -> path.getPath().endsWith(".json"));
        for (var entry : resources.entrySet()) {
            var id = entry.getKey();

            try (var reader = new InputStreamReader(entry.getValue().getInputStream())) {
                var json = JsonHelper.deserialize(reader);

                var result = ChatMinigame.Question.CODEC.parse(JsonOps.INSTANCE, json);
                result.resultOrPartial(error -> DaisyTweaks.LOGGER.warn("Failed to parse question at {}: {}", id, error))
                        .ifPresent(DATA_QUESTIONS::add);
            } catch (Exception e) {
                DaisyTweaks.LOGGER.error("Error reading question at {}: {}", id, e.getMessage(), e);
            }
        }
    }
}
