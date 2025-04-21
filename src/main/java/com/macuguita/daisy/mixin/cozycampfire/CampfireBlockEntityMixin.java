/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.cozycampfire;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    @Inject(
            method = "litServerTick",
            at = @At("HEAD")
    )
    private static void daisy$campfireRegen(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        if (!world.isClient && !FabricLoader.getInstance().isModLoaded("heartymeals")) {
            Box box = new Box(pos).expand(10D);

            BlockPos blockPosUnder = pos.offset(Direction.Axis.Y, -1);
            BlockState blockStateUnder = world.getBlockState(blockPosUnder);

            if (state.getBlock() instanceof CampfireBlock campfireBlock && ((CampfireBlockAccessor) campfireBlock).daisy$isSignalFireBaseBlock(blockStateUnder)) {
                box = box.expand(10D);
            }

            List<PlayerEntity> playerEntityList = world.getNonSpectatingEntities(PlayerEntity.class, box);

            for (PlayerEntity playerEntity : playerEntityList) {
                if (playerEntity.hasStatusEffect(StatusEffects.REGENERATION)) {
                    if (Objects.requireNonNull(playerEntity.getStatusEffect(StatusEffects.REGENERATION)).getDuration() < 60) {
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 0, true, false, true));
                    }
                } else {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 0, true, false, true));
                }
            }
        }
    }
}
