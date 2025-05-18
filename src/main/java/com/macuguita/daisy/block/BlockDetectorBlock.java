package com.macuguita.daisy.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class BlockDetectorBlock extends PillarBlock {

    public static final BooleanProperty POWERED = Properties.POWERED;

    public BlockDetectorBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction lookDir = ctx.getPlayerLookDirection().getOpposite();
        Direction.Axis axis = lookDir.getAxis();
        return this.getDefaultState().with(Properties.AXIS, axis);
    }

    private boolean areEndsEqual(Direction direction, WorldAccess world, BlockPos pos) {
        BlockState neighborState = world.getBlockState(pos.offset(direction));
        BlockState oppositeNeighbor = world.getBlockState(pos.offset(direction.getOpposite()));
        return neighborState.isOf(oppositeNeighbor.getBlock())
                && neighborState.getProperties().stream()
                .allMatch(prop -> Objects.equals(neighborState.get(prop), oppositeNeighbor.get(prop)));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis() == state.get(Properties.AXIS)) {
            world.scheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Axis axis = state.get(Properties.AXIS);
        Direction dir = Direction.from(axis, Direction.AxisDirection.POSITIVE);
        boolean powered = areEndsEqual(dir, world, pos);
        if (state.get(POWERED) != powered) {
            world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        world.scheduleBlockTick(pos, this, 1);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction side) {
        if (!state.get(POWERED)) return 0;
        return side.getAxis() != state.get(Properties.AXIS) ? 15 : 0;
    }
}
