/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.metalscaffolding;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.macuguita.daisy.reg.DaisyObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.FallLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallLocation.class)
public class FallLocationMixin {

    @Definition(id = "state", local = @Local(type = BlockState.class, argsOnly = true))
    @Definition(id = "isOf", method = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z")
    @Definition(id = "SCAFFOLDING", field = "Lnet/minecraft/block/Blocks;SCAFFOLDING:Lnet/minecraft/block/Block;")
    @Expression("state.isOf(SCAFFOLDING)")
    @WrapOperation(
            method = "fromBlockState",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean daisy$addMetalScaffoldingToFallLocation(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || original.call(instance, DaisyObjects.METAL_SCAFFOLDING.get());
    }
}
