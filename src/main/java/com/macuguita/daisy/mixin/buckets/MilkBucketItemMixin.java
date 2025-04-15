package com.macuguita.daisy.mixin.buckets;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

    @WrapOperation(
            method = "finishUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
            )
    )
    private boolean daisy$milkWorksWithBigStackSize(ItemStack instance, Operation<Boolean> original, ItemStack stack, World world, LivingEntity user) {
        boolean isEmpty = original.call(instance);

        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            if (!player.getInventory().insertStack(emptyBucket)) {
                player.dropItem(emptyBucket, false);
            }
        }

        return isEmpty;
    }
}
