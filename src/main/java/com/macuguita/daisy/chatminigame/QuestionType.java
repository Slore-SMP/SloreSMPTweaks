/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

public enum QuestionType {
    UNSCRAMBLE_ITEM("chatminigame.daisy.question.unscramble"),
    FILL_IN_THE_BLANKS("chatminigame.daisy.question.fill_in"),
    REVERSE_ITEM("chatminigame.daisy.question.reverse"),
    DATA_DRIVEN(""); // prompt already comes from JSON

    private final String translationKey;

    QuestionType(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }
}

