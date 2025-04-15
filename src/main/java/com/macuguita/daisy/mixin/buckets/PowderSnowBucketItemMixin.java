package com.macuguita.daisy.mixin.buckets;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBucketItem.class)
public class PowderSnowBucketItemMixin extends BlockItem {

    public PowderSnowBucketItemMixin(Block block, Settings settings) {
        super(block, settings);
    }

    @Inject(
            method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void daisy$pwderSnowLikeWorksWithBigStackSize(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return;

        Hand hand = context.getHand();
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getCount() > 1 && !player.isCreative()) {
            ActionResult result = super.useOnBlock(context);
            if (result.isAccepted()) {
                player.giveItemStack(Items.BUCKET.getDefaultStack());
                ci.setReturnValue(result);
            }
        }
    }
}
