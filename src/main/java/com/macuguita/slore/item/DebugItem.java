package com.macuguita.slore.item;

import com.macuguita.slore.SloreTweaks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugItem extends Item {
    public DebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if ((user.getStackInHand(Hand.MAIN_HAND).getItem() == SloreTweaks.DEBUG_ITEM.get()) && world instanceof ServerWorld serverWorld) {
            user.sendMessage(Text.literal(String.format("%s in breakable tag? %b", user.getStackInHand(Hand.OFF_HAND).getItem().toString(), user.getStackInHand(Hand.OFF_HAND).isIn(SloreTweaks.BREAKABLE))));
            user.sendMessage(Text.literal(String.format("%s is unbreakable? %b", user.getStackInHand(Hand.OFF_HAND).getItem().toString(), SloreTweaks.isUnbreakable(user.getStackInHand(Hand.OFF_HAND)))));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
