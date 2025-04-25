/*
 * Copyright (c) 2025 macuguita. All Rights Reserved.
 */

package com.macuguita.daisy.block.entity;

import com.macuguita.daisy.reg.DaisyBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class NetherLanternBlockEntity extends BlockEntity {

    public NetherLanternBlockEntity(BlockPos pos, BlockState state) {
        super(DaisyBlockEntities.NETHER_LANTERN.get(), pos, state);
    }
}
