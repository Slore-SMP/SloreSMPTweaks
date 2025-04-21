/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.item;

import com.macuguita.daisy.reg.DaisySounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MovieScriptItem extends Item {

    public MovieScriptItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Random random = world.random;
            SoundEvent randomSound = DaisySounds.MOVIE_SOUNDS.get(random.nextInt(DaisySounds.MOVIE_SOUNDS.size()));
            world.playSound(null, user.getX(), user.getY(), user.getZ(), randomSound, SoundCategory.PLAYERS, 1.0F, 1.0F);

            if (!user.isCreative()) {
                user.getItemCooldownManager().set(this, 20);
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.daisy.movie_script.tooltip").formatted(Formatting.GRAY));
    }
}
