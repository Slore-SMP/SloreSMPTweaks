/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.channelinganytime;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentEntity.class)
public class TridentMixin {

    @Definition(id = "getWorld", method = "Lnet/minecraft/entity/projectile/TridentEntity;getWorld()Lnet/minecraft/world/World;")
    @Definition(id = "isThundering", method = "Lnet/minecraft/world/World;isThundering()Z")
    @Expression("this.getWorld().isThundering()")
    @ModifyExpressionValue(
            method = "onEntityHit",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean daisy$channelingAnytime(boolean original) {
        return true;
    }
}
