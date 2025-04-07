package com.macuguita.slore.mixin.buckets.compat.meadow;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.satisfy.meadow.core.item.WoodenMilkBucket;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WoodenMilkBucket.class)
public class WoodenMilkBucketMixin {

    @WrapOperation(
            method = "finishUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
            )
    )
    private boolean slore$wrapIsEmptyCheck(ItemStack instance, Operation<Boolean> original, ItemStack stack, World world, LivingEntity user) {
        boolean isEmpty = original.call(instance);

        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            ItemStack emptyBucket = new ItemStack(ObjectRegistry.WOODEN_BUCKET.get());
            if (!player.getInventory().insertStack(emptyBucket)) {
                player.dropItem(emptyBucket, false);
            }
        }

        return isEmpty;
    }
}
