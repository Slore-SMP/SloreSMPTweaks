package com.macuguita.slore.mixin.cozy_campfire;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CampfireBlockEntity.class)
public class CamfireBlockEntityMixin {

    @Inject(
            method = "litServerTick",
            at = @At(value = "HEAD")
    )
    private static void slore$campfireRegen(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        if (!world.isClient) {
            Box box = new Box(pos).expand(10D).stretch(0D, 10D, 0D);
            List<PlayerEntity> playerEntityList = world.getNonSpectatingEntities(PlayerEntity.class, box);
            //RegistryEntry<StatusEffect> regen = StatusEffects.REGENERATION;
        }
    }
}
