/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.reaper;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("experienceDroppingDisabled")
    void daisy$setExperienceDroppingDisabled(boolean val);
}
