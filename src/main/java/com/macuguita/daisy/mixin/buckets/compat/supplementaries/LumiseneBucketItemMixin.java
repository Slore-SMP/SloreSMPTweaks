package com.macuguita.daisy.mixin.buckets.compat.supplementaries;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.macuguita.daisy.DaisyTweaks;
import net.mehvahdjukaar.supplementaries.common.items.fabric.LumiseneBucketItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(LumiseneBucketItem.class)
public class LumiseneBucketItemMixin {

    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/mehvahdjukaar/supplementaries/common/items/fabric/LumiseneBucketItem;getEmptiedStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack daisy$LumiseneBucketsWorksWithBigStackSize(ItemStack stack, PlayerEntity player, Operation<ItemStack> original) {
        return DaisyTweaks.handleStackableBucket(stack, player, new ItemStack(Items.BUCKET));
    }
}
