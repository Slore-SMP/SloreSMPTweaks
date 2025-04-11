package com.macuguita.slore.datagen;

import com.macuguita.slore.reg.SloreObjects;
import com.macuguita.slore.reg.SloreSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;

import java.util.Locale;
import java.util.Objects;

public class SloreLanguajeProvider extends FabricLanguageProvider {
    protected SloreLanguajeProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        generateItemTranslations(SloreObjects.REAPER.get(), translationBuilder);
        generateItemTranslations(SloreObjects.MOVIE_SCRIPT.get(), translationBuilder);
        generateBlockTranslations(SloreObjects.METAL_SCAFFOLDING.get(), translationBuilder);
        translationBuilder.add("item.slore.movie_script.tooltip", "A Minecraft Movie script");

        generateMovieTranslations(SloreSounds.STEVE, translationBuilder);
        generateMovieTranslations(SloreSounds.CRAFTING_TABLE, translationBuilder);
        generateMovieTranslations(SloreSounds.THE_OVERWORLD, translationBuilder);
        generateMovieTranslations(SloreSounds.ENDER_PEARL, translationBuilder);
        generateMovieTranslations(SloreSounds.BLOCKS, translationBuilder);
        generateMovieTranslations(SloreSounds.ELYTRA, translationBuilder);
        generateMovieTranslations(SloreSounds.WATER_BUCKET, translationBuilder);
        generateMovieTranslations(SloreSounds.FLINT_AND_STEEL, translationBuilder);
        generateMovieTranslations(SloreSounds.THE_NETHER, translationBuilder);
        generateMovieTranslations(SloreSounds.CHICKEN_JOCKEY, translationBuilder);
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
        String soundId = "slore.sound.subtitle." + Objects.requireNonNull(Registries.SOUND_EVENT.getId(sound)).getPath();
        String temp = capitalizeString(Objects.requireNonNull(Registries.SOUND_EVENT.getId(sound)).getPath().replace("_", " "));
        String toReturn = "Jack Black says " + temp;
        translationBuilder.add(soundId, toReturn);
    }
}
