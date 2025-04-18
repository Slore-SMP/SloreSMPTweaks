package com.macuguita.daisy.mixin.buckets;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.daisy.DaisyTweaks;
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
    private ItemStack daisy$bucketsWorkWithBigStackSize(ItemStack stack, PlayerEntity player, Operation<ItemStack> original) {
        return DaisyTweaks.handleStackableBucket(stack, player, new ItemStack(Items.BUCKET));
    }
}