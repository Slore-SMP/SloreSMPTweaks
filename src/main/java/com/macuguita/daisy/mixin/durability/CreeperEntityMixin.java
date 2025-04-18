package com.macuguita.daisy.mixin.durability;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.daisy.DaisyTweaks;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {

    @WrapOperation(
            method = "interactMob",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"
            )
    )
    private boolean daisy$creeperLightupAndUnbreakableCompat(ItemStack instance, Operation<Boolean> original) {
        return original.call(instance) || DaisyTweaks.isUnbreakable(instance);
    }
}
