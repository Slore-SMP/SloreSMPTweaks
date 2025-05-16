/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.cozycampfire;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CampfireBlock.class)
public interface CampfireBlockAccessor {

    @Invoker("isSignalFireBaseBlock")
    boolean daisy$isSignalFireBaseBlock(BlockState state);
}
