/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import com.macuguita.daisy.DaisyTweaks;
import com.macuguita.daisy.block.NetherLanternBlock;
import com.macuguita.daisy.block.entity.NetherLanternBlockEntity;
import com.macuguita.daisy.mixin.netherlantern.BeaconBlockEntityAccessor;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NetherLanternComponent implements Component, ServerTickingComponent {

    private final BlockEntity blockEntity;
    private int chargeTicks = 0;
    private boolean charging = false;
    @Nullable
    private StatusEffect primaryEffect = null;
    @Nullable
    private StatusEffect secondaryEffect = null;

    public NetherLanternComponent(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    private static boolean isBeaconLit(BeaconBlockEntity beacon) {
        return ((BeaconBlockEntityAccessor) beacon).daisy$getLevel() > 0 && !((BeaconBlockEntityAccessor) beacon).daisy$getBeamSegments().isEmpty();
    }

    private static BeaconBlockEntity getBeaconBlockEntityUnder(World world, BlockPos pos) {
        if (world == null) return null;

        BlockPos.Mutable mutablePos = pos.mutableCopy().move(0, -1, 0);

        while (mutablePos.getY() >= world.getBottomY()) {
            BlockEntity blockEntity = world.getBlockEntity(mutablePos);
            if (blockEntity instanceof BeaconBlockEntity beaconBlock) {
                return beaconBlock;
            }
            mutablePos.move(0, -1, 0);
        }
        return null;
    }

    private static NetherLanternBlockEntity getNetherLanternBlockEntityUnder(World world, BlockPos pos) {
        if (world == null) return null;

        BlockPos.Mutable mutablePos = pos.mutableCopy().move(0, -1, 0);

        while (mutablePos.getY() >= world.getBottomY()) {
            BlockEntity blockEntity = world.getBlockEntity(mutablePos);
            if (blockEntity instanceof NetherLanternBlockEntity netherLanternBlock) {
                return netherLanternBlock;
            }
            mutablePos.move(0, -1, 0);
        }
        return null;
    }

    private static boolean isBlockInsideBeaconBeam(World world, BlockPos pos) {
        BeaconBlockEntity beaconBlock = getBeaconBlockEntityUnder(world, pos);
        if (beaconBlock == null) {
            return false;
        } else return isBeaconLit(beaconBlock);
    }

    private static void spawnEffectParticles(World world, Box box) {
        Random random = world.getRandom();
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 20; i++) {
                double x = box.minX + random.nextDouble() * (box.maxX - box.minX);
                double y = box.minY + random.nextDouble() * (box.maxY - box.minY);
                double z = box.minZ + random.nextDouble() * (box.maxZ - box.minZ);

                serverWorld.spawnParticles(ParticleTypes.END_ROD, x, y, z, 2, 0.0, 0.02, 0.0, 0.1);
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.chargeTicks = nbt.getInt("ChargeTicks");
        this.charging = nbt.getBoolean("Charging");

        this.primaryEffect = null;
        this.secondaryEffect = null;

        if (nbt.contains("PrimaryEffect")) {
            Identifier effectId = Identifier.tryParse(nbt.getString("PrimaryEffect"));
            if (effectId != null) {
                this.primaryEffect = Registries.STATUS_EFFECT.get(effectId);
            }
        }

        if (nbt.contains("SecondaryEffect")) {
            Identifier effectId = Identifier.tryParse(nbt.getString("SecondaryEffect"));
            if (effectId != null) {
                this.secondaryEffect = Registries.STATUS_EFFECT.get(effectId);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("ChargeTicks", this.chargeTicks);
        nbt.putBoolean("Charging", this.charging);

        if (this.primaryEffect != null) {
            Identifier effectId = Registries.STATUS_EFFECT.getId(this.primaryEffect);
            if (effectId != null) {
                nbt.putString("PrimaryEffect", effectId.toString());
            }
        } else {
            nbt.remove("PrimaryEffect");
        }

        if (this.secondaryEffect != null) {
            Identifier effectId = Registries.STATUS_EFFECT.getId(this.secondaryEffect);
            if (effectId != null) {
                nbt.putString("SecondaryEffect", effectId.toString());
            }
        } else {
            nbt.remove("SecondaryEffect");
        }
    }

    public int getChargeTicks() {
        return this.chargeTicks;
    }

    public void setChargeTicks(int chargeTicks) {
        this.chargeTicks = chargeTicks;
    }

    public boolean getCharging() {
        return this.charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public StatusEffect[] getStatusEffects() {
        if (primaryEffect != null && secondaryEffect != null) {
            return new StatusEffect[]{primaryEffect, secondaryEffect};
        } else if (primaryEffect != null) {
            return new StatusEffect[]{primaryEffect};
        } else if (secondaryEffect != null) {
            return new StatusEffect[]{secondaryEffect};
        }
        return new StatusEffect[0];
    }

    public void setStatusEffects(@Nullable StatusEffect primary, @Nullable StatusEffect secondary) {
        this.primaryEffect = primary;
        this.secondaryEffect = secondary;
    }

    @Override
    public void serverTick() {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();

        if (world != null) {
            if (isBlockInsideBeaconBeam(world, pos) && getNetherLanternBlockEntityUnder(world, pos) == null) {
                BeaconBlockEntity beacon = getBeaconBlockEntityUnder(world, pos);
                if (beacon != null) {
                    var accessor = (BeaconBlockEntityAccessor) beacon;
                    StatusEffect newPrimary = accessor.daisy$getPrimaryEffect();
                    StatusEffect newSecondary = accessor.daisy$getSecondaryEffect();

                    if (this.primaryEffect == null && this.secondaryEffect == null) {
                        this.primaryEffect = newPrimary;
                        this.secondaryEffect = newSecondary;
                    }
                    if (newPrimary == this.primaryEffect && newSecondary == this.secondaryEffect) {
                        this.charging = true;
                        if (!(world.getBlockEntity(pos.down()) instanceof BeaconBlockEntity)) {
                            spawnChargingParticles(world, pos);
                        }

                        if (this.chargeTicks < world.getGameRules().get(DaisyTweaks.MAX_CHARGE_TICKS).get()) {
                            this.chargeTicks++;
                        }
                    }
                }
            } else {
                this.charging = false;
                if (this.chargeTicks > 0) {
                    if (this.primaryEffect != null || this.secondaryEffect != null) {
                        Box box = new Box(pos).expand(50.0D);
                        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);
                        spawnEffectParticles(world, box);

                        for (PlayerEntity player : players) {
                            if (this.primaryEffect != null) {
                                if (this.primaryEffect == this.secondaryEffect) {
                                    applyEffectToPlayer(player, this.primaryEffect, true);
                                }
                                applyEffectToPlayer(player, this.primaryEffect, false);
                            }

                            if (this.secondaryEffect != null) {
                                applyEffectToPlayer(player, this.secondaryEffect, false);
                            }
                        }
                    }

                    this.chargeTicks--;

                    if (this.chargeTicks <= 0) {
                        this.primaryEffect = null;
                        this.secondaryEffect = null;
                    }
                }
            }
            NetherLanternBlock.updateState(blockEntity.getCachedState(), world, pos);
        }
    }

    private void applyEffectToPlayer(PlayerEntity player, StatusEffect effect, boolean amplified) {
        StatusEffectInstance current = player.getStatusEffect(effect);
        int newDuration = 20 * 15;
        int amplifier = amplified ? 1 : 0;

        if (current == null || current.getDuration() < 60) {
            player.addStatusEffect(new StatusEffectInstance(
                    effect,
                    newDuration,
                    amplifier,
                    true,
                    false,
                    true
            ));
        }
    }

    private void spawnChargingParticles(World world, BlockPos pos) {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.END_ROD, pos.toCenterPos().getX(), pos.getY(), pos.toCenterPos().getZ(),
                    2, Math.cos(this.chargeTicks) * 0.075D, -0.15, Math.sin(this.chargeTicks) * 0.075D, 0.01);
        }
    }
}
