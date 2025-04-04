package com.macuguita.slore.mixin.buckets;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {

    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BucketItem;getEmptiedStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack slore$modifyEmptiedStack(ItemStack stack, PlayerEntity player, Operation<ItemStack> original) {
        if (player.getAbilities().creativeMode) {
            return stack;
        }

        if (stack.getCount() == 1) {
            return original.call(stack, player);
        }

        stack.decrement(1);
        ItemStack emptyBucket = new ItemStack(Items.BUCKET);
        if (!player.getInventory().insertStack(emptyBucket)) {
            player.dropItem(emptyBucket, false);
        }

        return stack;
    }
}