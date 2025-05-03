/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.yetanotherfurniture;


import com.starfish_studios.yaf.block.entity.WindChimeBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WindChimeBlockEntity.class, remap = false)
public class WindChimeBlockEntityMixin {

    @Inject(
            method = "commonTick",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void daisy$fixWindchimeDedicatedServer(World level, BlockState state, CallbackInfo ci) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) ci.cancel();
    }
}
