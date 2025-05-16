/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.reg.DaisySounds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;

import java.util.Locale;
import java.util.Objects;

public class DaisyLanguajeProvider extends FabricLanguageProvider {

    protected DaisyLanguajeProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        generateItemTranslations(DaisyObjects.REAPER.get(), translationBuilder);
        generateItemTranslations(DaisyObjects.MOVIE_SCRIPT.get(), translationBuilder);
        generateItemTranslations(DaisyObjects.PRIZE_BAG.get(), translationBuilder);

        generateBlockTranslations(DaisyObjects.METAL_SCAFFOLDING.get(), translationBuilder);
        generateBlockTranslations(DaisyObjects.CALCITE_FROG_STATUE.get(), translationBuilder);
        generateBlockTranslations(DaisyObjects.NETHER_LANTERN.get(), translationBuilder);
        translationBuilder.add("item.daisy.movie_script.tooltip", "A Minecraft Movie script");

        translationBuilder.add("death.attack.reaper", "%1$s reaped what they sowed");
        translationBuilder.add("death.attack.reaper.player", "%1$s was reaped by %2$s");

        translationBuilder.add("tooltip.nether_lantern", "When placed on a beacon beam the Nether Lantern absorbs its energy and powers.");
        translationBuilder.add("tooltip.nether_lantern.charge_ticks", "Charge time: %s");

        generateMovieTranslations(DaisySounds.STEVE, translationBuilder);
        generateMovieTranslations(DaisySounds.CRAFTING_TABLE, translationBuilder);
        generateMovieTranslations(DaisySounds.THE_OVERWORLD, translationBuilder);
        generateMovieTranslations(DaisySounds.ENDER_PEARL, translationBuilder);
        generateMovieTranslations(DaisySounds.BLOCKS, translationBuilder);
        generateMovieTranslations(DaisySounds.ELYTRA, translationBuilder);
        generateMovieTranslations(DaisySounds.WATER_BUCKET, translationBuilder);
        generateMovieTranslations(DaisySounds.FLINT_AND_STEEL, translationBuilder);
        generateMovieTranslations(DaisySounds.THE_NETHER, translationBuilder);
        generateMovieTranslations(DaisySounds.CHICKEN_JOCKEY, translationBuilder);
    }

    private String capitalizeString(String string) {
        char[] chars = string.toLowerCase(Locale.getDefault()).toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }
        return new String(chars);
    }

    private void generateBlockTranslations(Block block, TranslationBuilder translationBuilder) {
        String temp = capitalizeString(Registries.BLOCK.getId(block).getPath().replace("_", " "));
        translationBuilder.add(block, temp);
    }

    private void generateItemTranslations(Item item, TranslationBuilder translationBuilder) {
        String temp = capitalizeString(Registries.ITEM.getId(item).getPath().replace("_", " "));
        translationBuilder.add(item, temp);
    }

    private void generateMovieTranslations(SoundEvent sound, TranslationBuilder translationBuilder) {
        String soundId = "sound.daisy.subtitle." + Objects.requireNonNull(Registries.SOUND_EVENT.getId(sound)).getPath();
        String temp = capitalizeString(Objects.requireNonNull(Registries.SOUND_EVENT.getId(sound)).getPath().replace("_", " "));
        String toReturn = "Jack Black says " + temp;
        translationBuilder.add(soundId, toReturn);
    }
}
