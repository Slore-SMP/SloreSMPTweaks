package com.macuguita.slore.mixin.metalscaffolding;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.slore.SloreTweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapOperation(
            method = "applyClimbingSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean slore$addMetalScaffoldingToClimbingSpeed(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || original.call(instance, SloreTweaks.METAL_SCAFFOLDING.get());
    }
}
