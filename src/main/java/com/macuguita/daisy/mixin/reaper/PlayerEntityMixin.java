/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.reaper;

import com.macuguita.daisy.item.ReaperItem;
import com.macuguita.daisy.reg.DaisyDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void daisy$overrideAttack(Entity target, CallbackInfo ci) {
        ItemStack stack = this.getMainHandStack();
        if (stack.getItem() instanceof ReaperItem && target instanceof LivingEntity livingTarget) {
            World world = this.getWorld();
            DamageSource source = DaisyDamageTypes.reaper(world, this);

            livingTarget.damage(source, Integer.MAX_VALUE);

            ci.cancel();
        }
    }
}
