/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.datagen;

import com.macuguita.daisy.chatminigame.QuestionType;
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

        translationBuilder.add(QuestionType.UNSCRAMBLE_ITEM.translationKey(), "\n§e\uD83E\uDDE9§r Unscramble this Minecraft item: §e%s§r\n");
        translationBuilder.add(QuestionType.FILL_IN_THE_BLANKS.translationKey(), "\n§e\uD83E\uDDE9§r Fill in this Minecraft item: §e%s§r\n");
        translationBuilder.add(QuestionType.REVERSE_ITEM.translationKey(), "\n§e\uD83D\uDD01§r What Minecraft item is this when reversed? §e%s§r\n");
        translationBuilder.add("chatminigame.daisy.correct_answer", "§e%s§r got it right! The answer was: §e%s§r");
        translationBuilder.add("commands.daisy.asktrivia.feedback", "Chat question sent");

        translationBuilder.add("commands.daisy.sethome.success", "Home set successfully.");
        translationBuilder.add("commands.daisy.sethome.success.name", "Home '%s' set successfully.");
        translationBuilder.add("commands.daisy.sethome.fail.exists", "A home with that name already exists.");
        translationBuilder.add("commands.daisy.sethome.fail.maxhomes", "You have reached the maximum number of homes (%d).");
        translationBuilder.add("commands.daisy.home.success", "You've been teleported.");
        translationBuilder.add("commands.daisy.home.success.name", "You've been teleported to %s.");
        translationBuilder.add("commands.daisy.home.fail", "Couldn't find home.");
        translationBuilder.add("commands.daisy.homes.success", "Your homes are: %s.");
        translationBuilder.add("commands.daisy.homes.fail", "You don't have any homes!");
        translationBuilder.add("commands.daisy.delhome.success", "Deleted: %s.");
        translationBuilder.add("commands.daisy.delhome.fail", "Couldn't delete home.");
        translationBuilder.add("commands.daisy.setmaxhomes.success", "Set %s's max amount of homes to %d.");
        translationBuilder.add("commands.daisy.setmaxhomes.fail", "Couldn't find player.");

        translationBuilder.add("commands.tpa.success.sent", "Sent teleport request to %s.");
        translationBuilder.add("commands.tpa.request", "%s wants to teleport to you. [Click to accept]");
        translationBuilder.add("commands.tpa.hover", "Click to accept teleport request");
        translationBuilder.add("commands.tpa.fail.self", "You can't teleport to yourself!");
        translationBuilder.add("commands.tpa.fail.cooldown", "You must wait before sending another request.");

        translationBuilder.add("commands.tpahere.success.sent", "Requested %s to teleport to you.");
        translationBuilder.add("commands.tpahere.request", "%s wants you to teleport to them. [Click to accept]");
        translationBuilder.add("commands.tpahere.hover", "Click to accept teleport request");
        translationBuilder.add("commands.tpahere.fail.self", "You can't request yourself!");
        translationBuilder.add("commands.tpahere.fail.cooldown", "You must wait before sending another request.");

        translationBuilder.add("commands.tpaccept.success.tpa", "%s has teleported to you.");
        translationBuilder.add("commands.tpaccept.success.requester.tpa", "You have been teleported to %s.");
        translationBuilder.add("commands.tpaccept.success.tpahere", "You have teleported to %s.");
        translationBuilder.add("commands.tpaccept.success.requester.tpahere", "%s has teleported to you.");
        translationBuilder.add("commands.tpaccept.fail.noRequest", "No active request from %s.");
        translationBuilder.add("commands.tpaccept.fail.noRequests", "You have no pending requests.");
        translationBuilder.add("commands.tpaccept.pending.header", "Pending teleport requests:");
        translationBuilder.add("commands.tpaccept.pending.tpa", "- %s (wants to come to you)");
        translationBuilder.add("commands.tpaccept.pending.tpahere", "- %s (wants you to go to them)");
        translationBuilder.add("commands.tpaccept.hover.tpa", "Click to let them teleport to you");
        translationBuilder.add("commands.tpaccept.hover.tpahere", "Click to teleport to them");

        translationBuilder.add("commands.tpa.cooldown.remaining", "You must wait %d more seconds.");
        translationBuilder.add("commands.tpahere.cooldown.remaining", "You must wait %d more seconds.");

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
