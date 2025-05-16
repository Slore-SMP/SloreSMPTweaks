/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.mixin.netherlantern;

import com.macuguita.daisy.utils.BeamSegmentMixinAccessor;
import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconBlockEntity.BeamSegment.class)
abstract class BeamSegmentMixin implements BeamSegmentMixinAccessor {

    @Shadow
    @Final
    @Mutable
    float[] color;
    @Unique
    private boolean visible;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initVisible(CallbackInfo ci) {
        visible = true;
    }

    @Override
    public void discontinuous_beacon_beams$setInvisible() {
        visible = false;
    }

    @Override
    public boolean discontinuous_beacon_beams$isVisible() {
        return visible;
    }

    @Override
    public void discontinuous_beacon_beams$setColor(float[] color) {
        this.color = color;
    }
}

