package com.macuguita.slore.mixin.cozy_campfire;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CampfireBlock.class)
public interface CampfireBlockAccessor {
    @Invoker("isSignalFireBaseBlock")
    boolean slore$isSignalFireBaseBlock(BlockState state);
}
