package com.macuguita.slore.mixin.reaper;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("experienceDroppingDisabled")
    void slore$setExperienceDroppingDisabled(boolean val);
}
