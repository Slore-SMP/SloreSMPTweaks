package com.macuguita.daisy.mixin.durability;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.daisy.DaisyTweaks;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

    @WrapOperation(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"
            )
    )
    private boolean daisy$unbreakableStuffDoesNotGoInTheAnvil(ItemStack instance, Operation<Boolean> original) {
        return original.call(instance) || DaisyTweaks.isUnbreakable(instance);
    }
}
