/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

import com.google.gson.Gson;
import com.macuguita.daisy.DaisyTweaks;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatapackQuestionLoader implements SimpleSynchronousResourceReloadListener {

    private static final Gson GSON = new Gson();
    private static final Identifier ID = DaisyTweaks.id("datapack_question_loader");
    private static final Identifier QUESTIONS_DIR = DaisyTweaks.id("chat_minigame_questions");

    public static final List<ChatMinigame.Question> DATA_QUESTIONS = new ArrayList<>();

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        DATA_QUESTIONS.clear();

        var resources = manager.findResources(QUESTIONS_DIR.getPath(), path -> path.getPath().endsWith(".json"));
        for (var entry : resources.entrySet()) {
            try (var reader = new InputStreamReader(entry.getValue().getInputStream())) {
                var json = GSON.fromJson(reader, Map.class);
                String prompt = (String) json.get("prompt");
                @SuppressWarnings("unchecked") List<String> answers = (List<String>) json.get("answers");

                if (prompt != null && answers != null && !answers.isEmpty()) {
                    DATA_QUESTIONS.add(new ChatMinigame.Question(QuestionType.DATA_DRIVEN, prompt, answers));
                }

            } catch (Exception e) {
                System.err.println("Failed to load question: " + entry.getKey() + " - " + e.getMessage());
            }
        }
    }
}
