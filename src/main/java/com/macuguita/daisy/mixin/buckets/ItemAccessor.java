package com.macuguita.daisy.mixin.buckets;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor("maxCount") @Mutable
    void daisy$setMaxCount(int maxCount);
}
