/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

import com.macuguita.daisy.reg.DaisyGameRules;
import com.macuguita.daisy.reg.DaisyObjects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Supplier;

public class ChatMinigame {

    private static int tickCounter = 0;

    private static Question currentQuestion = null;
    private static QuestionType lastQuestionType = null;

    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tickCounter++;

            int interval = server.getGameRules().getInt(DaisyGameRules.CHAT_MINIGAME_INTERVAL);

            if (tickCounter >= interval) {
                tickCounter = 0;
                if (server.getCurrentPlayerCount() > 0) {
                    askRandomQuestion(server, false);
                }
            }
        });


        ServerMessageEvents.CHAT_MESSAGE.register((SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) -> {
            String raw = message.getContent().getString().trim().toLowerCase(Locale.ROOT);

            if (currentQuestion != null && currentQuestion.isCorrect(raw)) {
                ItemStack prizeBagStack = new ItemStack(DaisyObjects.PRIZE_BAG.get());
                if (!sender.getInventory().insertStack(prizeBagStack)) {
                    sender.dropItem(prizeBagStack, true);
                }
                Text textMessage = Text.translatable(
                        "chatminigame.daisy.correct_answer",
                        sender.getDisplayName(),
                        Text.literal(formatLists(currentQuestion.acceptableAnswers))
                );
                serverBroadcast(sender.getServer(), textMessage.getString());

                currentQuestion = null;
            }
        });
    }

    private static String formatLists(List<String> input) {
        return input.size() == 1
                ? input.get(0)  // Only one answer
                : String.join(", ", input);  // Multiple answers
    }

    static void askRandomQuestion(MinecraftServer server, boolean showAnswer) {
        List<Supplier<Question>> generators = List.of(
                ChatMinigame::generateUnscrambleQuestion,
                ChatMinigame::generateFillInTheBlanksQuestion,
                ChatMinigame::generateReverseItemQuestion,
                ChatMinigame::getRandomDatapackQuestion
        );

        List<Supplier<Question>> filtered = generators.stream()
                .filter(supplier -> {
                    Question test = supplier.get();
                    return test != null && test.type() != lastQuestionType;
                })
                .toList();

        if (filtered.isEmpty()) return;

        Supplier<Question> selectedSupplier = filtered.get(new Random().nextInt(filtered.size()));
        Question selected = selectedSupplier.get();

        if (selected != null) {
            currentQuestion = selected;
            lastQuestionType = selected.type();
            serverBroadcast(server, selected.promptText().getString());

            if (showAnswer) {
                serverBroadcast(server, "Question answer is: " + formatLists(selected.acceptableAnswers));
            }
        }
    }

    // -- Question Generator (type: unscramble item) --

    private static Question generateUnscrambleQuestion() {
        Item item = getRandomItem();
        String rawId = Registries.ITEM.getId(item).getPath();

        if (rawId.length() <= 3 || rawId.contains("debug") || rawId.contains("barrier")) {
            return generateUnscrambleQuestion(); // Try again
        }

        String displayName = capitalizeWords(rawId.replace("_", " "));
        String scrambled = scrambleKeepingSpaces(displayName);
        return new Question(QuestionType.UNSCRAMBLE_ITEM, scrambled, List.of(displayName));
    }

    private static Item getRandomItem() {
        List<Identifier> ids = Registries.ITEM.getIds().stream().toList();
        return Registries.ITEM.get(ids.get(new Random().nextInt(ids.size())));
    }

    // -- Question Generator (type: fill in the blanks)

    private static Question generateFillInTheBlanksQuestion() {
        Item item = getRandomItem();
        String rawId = Registries.ITEM.getId(item).getPath();

        if (rawId.length() <= 3 || rawId.contains("debug") || rawId.contains("barrier")) {
            return generateFillInTheBlanksQuestion(); // Retry
        }

        String displayName = capitalizeWords(rawId.replace("_", " "));
        String prompt = blankOutLetters(displayName);
        String answer = extractMissingLetters(displayName, prompt);

        return new Question(QuestionType.FILL_IN_THE_BLANKS, prompt, List.of(answer));
    }

    // -- Question Generator (type: unreverse)

    private static Question generateReverseItemQuestion() {
        Item item = getRandomItem();
        String rawId = Registries.ITEM.getId(item).getPath();

        if (rawId.length() <= 3 || rawId.contains("debug") || rawId.contains("barrier") || rawId.contains("creative")) {
            return generateReverseItemQuestion(); // Retry
        }

        String displayName = capitalizeWords(rawId.replace("_", " "));
        String reversed = new StringBuilder(displayName).reverse().toString();

        return new Question(QuestionType.REVERSE_ITEM, reversed, List.of(displayName));
    }

    // -- Question Generator (type: datapack)

    private static Question getRandomDatapackQuestion() {
        List<Question> pool = DatapackQuestionLoader.DATA_QUESTIONS;
        if (pool.isEmpty()) return null;
        return pool.get(new Random().nextInt(pool.size()));
    }

    // -- Question Helpers --

    private static String scrambleKeepingSpaces(String input) {
        List<Integer> spaceIndices = new ArrayList<>();
        List<Character> letters = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ') spaceIndices.add(i);
            else letters.add(c);
        }

        Collections.shuffle(letters);
        StringBuilder result = new StringBuilder();
        int letterIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            result.append(spaceIndices.contains(i) ? ' ' : letters.get(letterIndex++));
        }

        return result.toString();
    }

    private static String capitalizeWords(String string) {
        char[] chars = string.toLowerCase(Locale.ROOT).toCharArray();
        boolean capitalizeNext = true;

        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                chars[i] = Character.toUpperCase(chars[i]);
                capitalizeNext = false;
            }
        }

        return new String(chars);
    }

    private static String blankOutLetters(String input) {
        StringBuilder result = new StringBuilder();
        Random rand = new Random();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c) && rand.nextBoolean()) {
                result.append('_');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String extractMissingLetters(String full, String masked) {
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < full.length(); i++) {
            if (masked.charAt(i) == '_') {
                answer.append(full.charAt(i));
            }
        }
        return answer.toString();
    }

    private static void serverBroadcast(MinecraftServer server, String message) {
        if (server != null) {
            server.getPlayerManager().broadcast(Text.literal(message), false);
        }
    }

    // -- Question Record --

    public record Question(QuestionType type, String prompt, List<String> acceptableAnswers) {

        public boolean isCorrect(String input) {
            String norm = normalize(input);
            return acceptableAnswers.stream().anyMatch(ans -> normalize(ans).equals(norm));
        }

        private String normalize(String s) {
            return s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        }

        public Text promptText() {
            return type == QuestionType.DATA_DRIVEN
                    ? Text.literal(prompt)
                    : Text.translatable(type.translationKey(), prompt);
        }
    }
}
