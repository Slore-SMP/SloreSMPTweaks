/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.chatminigame;

import com.mojang.serialization.Codec;

import java.util.Locale;

public enum QuestionType {
    UNSCRAMBLE_ITEM,
    FILL_IN_THE_BLANKS,
    REVERSE_ITEM,
    DATA_DRIVEN;

    public static final Codec<QuestionType> CODEC = Codec.STRING.xmap(
            s -> QuestionType.valueOf(s.toUpperCase(Locale.ROOT)),
            qt -> qt.name().toLowerCase(Locale.ROOT)
    );
}
