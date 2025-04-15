package com.macuguita.daisy.mixin.reaper;

import com.macuguita.daisy.item.ReaperItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(
            method = "damageArmor(Lnet/minecraft/entity/damage/DamageSource;F[I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void daisy$reaperDoesNotDamageArmor(DamageSource damageSource, float amount, int[] slots, CallbackInfo ci) {
        if (damageSource.getAttacker() instanceof LivingEntity attacker) {
            if (attacker.getMainHandStack().getItem() instanceof ReaperItem) {
                ci.cancel();
            }
        }
    }
}
