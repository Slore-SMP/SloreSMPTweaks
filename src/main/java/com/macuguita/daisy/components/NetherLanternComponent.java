/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.components;

import com.macuguita.daisy.block.NetherLanternBlock;
import com.macuguita.daisy.mixin.netherlantern.BeaconBlockEntityAccessor;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
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
    private boolean amplified = false;

    private final int MAX_CHARGE_TICKS = 360000;

    public NetherLanternComponent(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.chargeTicks = nbt.getInt("ChargeTicks");
        this.charging = nbt.getBoolean("Charging");
        this.amplified = nbt.getBoolean("Amplified");

        // Reset effects
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
        nbt.putBoolean("Amplified", this.amplified);

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

    public boolean getAmplified() {
        return this.amplified;
    }

    public void setAmplified(boolean amplified) {
        this.amplified = amplified;
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

    private static boolean isBeaconLit(BeaconBlockEntity beacon) {
        return ((BeaconBlockEntityAccessor) beacon).daisy$getLevel() > 0 && !((BeaconBlockEntityAccessor) beacon).daisy$getBeamSegments().isEmpty();
    }

    private static boolean isPoweredUp(BeaconBlockEntity beacon) {
        return ((BeaconBlockEntityAccessor) beacon).daisy$getLevel() >= 4 && (((BeaconBlockEntityAccessor) beacon).daisy$getPrimaryEffect() == ((BeaconBlockEntityAccessor) beacon).daisy$getSecondaryEffect());
    }

    private static BeaconBlockEntity getBeaconBlockEntityUnder(World world, BlockPos pos) {
        if (world == null) return null;

        BlockPos.Mutable mutablePos = pos.mutableCopy().move(0, -1, 0); // Start checking below

        while (mutablePos.getY() >= world.getBottomY()) {
            BlockEntity blockEntity = world.getBlockEntity(mutablePos);
            if (blockEntity instanceof BeaconBlockEntity beaconBlock) {
                return beaconBlock;
            }
            mutablePos.move(0, -1, 0); // Move down each iteration
        }
        return null;
    }

    private static boolean isBlockInsideBeaconBeam(World world, BlockPos pos) {
        BeaconBlockEntity beaconBlock = getBeaconBlockEntityUnder(world, pos);
        if (beaconBlock == null) {
            return false;
        } else if (!isBeaconLit(beaconBlock)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void serverTick() {
        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();

        if (world != null) {
            if (isBlockInsideBeaconBeam(world, pos)) {
                BeaconBlockEntity beacon = getBeaconBlockEntityUnder(world, pos);
                if (beacon != null) {
                    var accessor = (BeaconBlockEntityAccessor) beacon;
                    StatusEffect newPrimary = accessor.daisy$getPrimaryEffect();
                    StatusEffect newSecondary = accessor.daisy$getSecondaryEffect();
                    this.charging = true;
                    this.amplified = isPoweredUp(beacon);

                    if (newPrimary != this.primaryEffect || newSecondary != this.secondaryEffect) {
                        this.primaryEffect = newPrimary;
                        this.secondaryEffect = newSecondary;
                    }

                    if (this.chargeTicks < MAX_CHARGE_TICKS) {
                        this.chargeTicks++;
                    }
                }
            } else {
                this.charging = false;
                if (this.chargeTicks > 0) {
                    if (this.primaryEffect != null || this.secondaryEffect != null) {
                        Box box = new Box(pos).expand(50.0D);
                        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);

                        for (PlayerEntity player : players) {
                            if (this.primaryEffect != null) {
                                if (this.primaryEffect == this.secondaryEffect || this.amplified) {
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
        int amplifier = amplified? 1 : 0;

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
}
