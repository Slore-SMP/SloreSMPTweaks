package net.macuguita.slore.item.custom;

import net.macuguita.slore.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class BibleItem extends Item {
    public BibleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Play sound when using the item
        world.playSound(user, user.getX(), user.getY(), user.getZ(), ModSounds.RACHEL, SoundCategory.PLAYERS);

        // Set a cooldown of 15 ticks for the player
        user.getItemCooldownManager().set(this, 10);

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.slore_tweaks.bible.tooltip"));
    }
}
