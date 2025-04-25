/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.netherlantern;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.macuguita.daisy.reg.DaisyObjects;
import com.macuguita.daisy.utils.BeamSegmentMixinAccessor;
import com.macuguita.daisy.utils.SpoofBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Arrays;
import java.util.List;

import static com.macuguita.daisy.utils.SpoofBlock.*;

@Mixin(BeaconBlockEntity.class)
abstract class BeaconBlockEntityMixin extends BlockEntity {
    @Shadow private List<BeaconBlockEntity.BeamSegment> field_19178;

    @Unique private boolean beamIsVisible;

    private BeaconBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        throw new IllegalStateException("BeaconBlockEntityMixin's dummy constructor called!");
    }

    @ModifyExpressionValue(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"
    ))
    private static Block updateBeamStateAndSpoofBlock(final Block original, World world, BlockPos pos, BlockState unused, BeaconBlockEntity beaconBlockEntity) {
        if (!(world.isClient) || original == Blocks.BEDROCK) return original;

        return switch (updateBeamStateAndSpoofBlockImpl(original, (BeaconBlockEntityMixin) (Object) beaconBlockEntity)) {
            case BLOCK_BEAM -> Blocks.WHITE_STAINED_GLASS;
            case PASS_UNTINTED -> Blocks.GLASS;
            case UNCHANGED -> original;
        };
    }

    @ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static void beamModifier(Args args, World world, BlockPos pos, BlockState state, BeaconBlockEntity beaconBlockEntity) {
        BeamSegmentMixinAccessor beamSegment = args.get(0);
        final BeaconBlockEntityMixin beaconBlockEntityMixin = (BeaconBlockEntityMixin) (Object) beaconBlockEntity;
        if (getTopSegment(beaconBlockEntityMixin) == null) return;

        if (!beaconBlockEntityMixin.beamIsVisible)
            beamSegment.discontinuous_beacon_beams$setInvisible();
    }


    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;equals([F[F)Z"))
    private static boolean equalAndNotTogglingVisibility(float[] colorA, float[] colorB, World world, BlockPos pos, BlockState state, BeaconBlockEntity beaconBlockEntity) {
        final BeaconBlockEntityMixin beaconBlockEntityMixin = (BeaconBlockEntityMixin) (Object) beaconBlockEntity;
        BeaconBlockEntity.BeamSegment beamSegment = getTopSegment(beaconBlockEntityMixin);
        if (beaconBlockEntityMixin.beamIsVisible != (beamSegment == null || ((BeamSegmentMixinAccessor) beamSegment).discontinuous_beacon_beams$isVisible())) // toggling visibility
            return false;

        return Arrays.equals(colorA, colorB);
    }

    @Unique
    private static SpoofBlock updateBeamStateAndSpoofBlockImpl(final Block original, BeaconBlockEntityMixin beaconBlockEntityMixin) {
        BeaconBlockEntity.BeamSegment beamSegment = getTopSegment(beaconBlockEntityMixin);
        if (beamSegment == null) {
            // inside beacon block, initialize
            beaconBlockEntityMixin.beamIsVisible = true;
            return UNCHANGED;
        }

        //noinspection AssignmentUsedAsCondition
        if (beaconBlockEntityMixin.beamIsVisible = ((BeamSegmentMixinAccessor) beamSegment).discontinuous_beacon_beams$isVisible()) {
            if (original.getDefaultState().isOf(DaisyObjects.NETHER_LANTERN.get())) {
                beaconBlockEntityMixin.beamIsVisible = false;
                return BLOCK_BEAM;
            } else {
                return UNCHANGED;
            }
        }
        return PASS_UNTINTED;
    }

    @Unique
    private static BeaconBlockEntity.BeamSegment getTopSegment(BeaconBlockEntityMixin beaconBlockEntityMixin) {
        final int size = beaconBlockEntityMixin.field_19178.size();
        return size < 1 ? null : beaconBlockEntityMixin.field_19178.get(size - 1);
    }
}
