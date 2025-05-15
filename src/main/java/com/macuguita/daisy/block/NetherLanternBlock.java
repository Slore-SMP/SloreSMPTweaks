/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.block;

import com.macuguita.daisy.block.entity.NetherLanternBlockEntity;
import com.macuguita.daisy.components.DaisyComponents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class NetherLanternBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final IntProperty CHARGE_STATE = IntProperty.of("charge_state", 0, 2);
    // 0 = Not charged, not charging
    // 1 = Charged, not charging
    // 2 = Charging (regardless of current charge)

    public NetherLanternBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(CHARGE_STATE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGE_STATE);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetherLanternBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt != null) {
            int chargeTicks = nbt.getInt("ChargeTicks");

            Identifier primaryEffect = nbt.contains("PrimaryEffect", NbtElement.STRING_TYPE)
                    ? new Identifier(nbt.getString("PrimaryEffect"))
                    : null;

            Identifier secondaryEffect = nbt.contains("SecondaryEffect", NbtElement.STRING_TYPE)
                    ? new Identifier(nbt.getString("SecondaryEffect"))
                    : null;

            MutableText chargeTimeText = Text.translatable("tooltip.nether_lantern.charge_ticks", formatDurationFromTicks(chargeTicks));
            tooltip.add(chargeTimeText);

            if (primaryEffect != null) {
                MutableText primaryEffectText = getStatusEffectName(primaryEffect);
                if (primaryEffect.equals(secondaryEffect)) {
                    primaryEffectText.append(Text.literal(" II"));
                }
                tooltip.add(primaryEffectText.formatted(Formatting.BLUE));
            }

            if (secondaryEffect != null && !secondaryEffect.equals(primaryEffect)) {
                MutableText secondaryEffectText = getStatusEffectName(secondaryEffect);
                tooltip.add(secondaryEffectText.formatted(Formatting.BLUE));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.nether_lantern").formatted(Formatting.DARK_GRAY));
        }
    }

    public static String formatDurationFromTicks(int ticks) {
        int totalSeconds = ticks / 20;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public MutableText getStatusEffectName(Identifier id) {
        StatusEffect effect = Registries.STATUS_EFFECT.get(id);
        if (effect == null) {
            return Text.literal("Unknown Effect");
        }
        return (MutableText) effect.getName();
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(CHARGE_STATE, 0);
    }

    public static void updateState(BlockState state, World world, BlockPos pos) {
        if (world == null || world.isClient) return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NetherLanternBlockEntity lantern) {
            var component = DaisyComponents.NETHER_LANTERN_COMPONENT.get(lantern);
            int newState = component.getCharging() ? 2 :
                    (component.getChargeTicks() > 0 ? 1 : 0);

            if (state.get(CHARGE_STATE) != newState) {
                world.setBlockState(pos, state.with(CHARGE_STATE, newState), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            updateState(state, world, pos);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClient && blockEntity != null) {
            ItemStack stack = new ItemStack(this);

            var component = DaisyComponents.NETHER_LANTERN_COMPONENT.get(blockEntity);
            component.setCharging(false);
            NbtCompound tag = new NbtCompound();
            component.writeToNbt(tag);
            tag.remove("Charging");

            stack.getOrCreateSubNbt("BlockEntityTag").copyFrom(tag);

            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            NbtCompound tag = BlockItem.getBlockEntityNbt(stack);
            if (tag != null) {
                var component = DaisyComponents.NETHER_LANTERN_COMPONENT.get(blockEntity);
                component.readFromNbt(tag);
            }
        }
    }
}