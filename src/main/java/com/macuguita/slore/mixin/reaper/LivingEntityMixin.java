package com.macuguita.slore.mixin.reaper;

import com.macuguita.slore.item.ReaperItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract @Nullable LivingEntity getAttacker();

    @Shadow public abstract void remove(Entity.RemovalReason reason);

    @Shadow public int deathTime;

    @Shadow private boolean experienceDroppingDisabled;

    @Inject(
            method = "updatePostDeath()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void slore$onUpdatePostDeath(CallbackInfo ci) {
        LivingEntity thisObject = (LivingEntity) (Object)this;
        var attacker = this.getAttacker();
        if (attacker != null && attacker.getMainHandStack().getItem() instanceof ReaperItem) {
            ReaperItem.spawnGhostParticle(thisObject);
            this.remove(Entity.RemovalReason.KILLED);
            ci.cancel();
        }
    }

    @Inject(
            method = "onDeath",
            at = @At("HEAD")
    )
    private void slore$onOnDeath(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.getAttacker() instanceof LivingEntity attacker && attacker.getMainHandStack().getItem() instanceof ReaperItem) {
            this.deathTime = 20;
            this.experienceDroppingDisabled = true;
        }
    }
}
