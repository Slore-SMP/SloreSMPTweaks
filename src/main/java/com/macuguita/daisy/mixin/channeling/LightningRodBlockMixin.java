package com.macuguita.daisy.mixin.channeling;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {

    @Definition(id = "world", local = @Local(type = World.class, argsOnly = true))
    @Definition(id = "isThundering", method = "Lnet/minecraft/world/World;isThundering()Z")
    @Expression("world.isThundering()")
    @ModifyExpressionValue(
            method = "onProjectileHit",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    public boolean slore$channelingAnytime(boolean original) {
        return true;
    }
}
