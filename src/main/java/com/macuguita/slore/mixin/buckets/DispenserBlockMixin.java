package com.macuguita.slore.mixin.buckets;

import com.macuguita.slore.utils.EmptyBucketDispenserBehavior;
import com.macuguita.slore.utils.FilledBucketDispenserBehavior;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
    @Inject(method = "registerBehavior", at = @At("HEAD"), cancellable = true)
    private static void replaceBucketBehavior(ItemConvertible provider, DispenserBehavior behavior, CallbackInfo ci) {
        Item item = provider.asItem();

        if (!(behavior instanceof FilledBucketDispenserBehavior) && !(behavior instanceof EmptyBucketDispenserBehavior)) {
            if (item == Items.BUCKET) {
                DispenserBlock.registerBehavior(provider, new EmptyBucketDispenserBehavior());
                ci.cancel();
            } else if (item == Items.WATER_BUCKET || item == Items.LAVA_BUCKET || item == Items.POWDER_SNOW_BUCKET) {
                DispenserBlock.registerBehavior(provider, new FilledBucketDispenserBehavior());
                ci.cancel();
            }
        }
    }
}

