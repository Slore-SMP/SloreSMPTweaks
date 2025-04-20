package com.macuguita.daisy.mixin.mysticsbiomes;

import com.mysticsbiomes.common.world.feature.MysticTreeFeatures;
import com.mysticsbiomes.common.world.feature.trunk.CherryTrunkPlacer;
import com.mysticsbiomes.init.MysticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MysticTreeFeatures.class)
public abstract class MysticTreeFeaturesMixin {

    @Shadow
    private static TreeFeatureConfig.Builder base(BlockStateProvider log, TrunkPlacer trunkPlacer, BlockStateProvider leaves, FoliagePlacer foliagePlacer, FeatureSize size) {
        return null;
    }

    /**
     * @author macuguita
     * @reason fix decaying leaves
     */
    @Overwrite
    private static TreeFeatureConfig.Builder bushyTree(Block log, Block leaves, int baseHeight, int heightRandomA, int foliageAttempts) {
        return new TreeFeatureConfig.Builder(BlockStateProvider.of(log), new LargeOakTrunkPlacer(baseHeight, heightRandomA, 0), BlockStateProvider.of(leaves.getDefaultState().with(Properties.PERSISTENT, true)), new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), ConstantIntProvider.create(3), foliageAttempts), new TwoLayersFeatureSize(1, 0, 1));
    }

    /**
     * @author macuguita
     * @reason fix decaying leaves
     */
    @Overwrite
    private static TreeFeatureConfig.Builder cherryTree(Block leaves) {
        return base(BlockStateProvider.of(MysticBlocks.CHERRY_LOG), new CherryTrunkPlacer(7, 3, 0, new WeightedListIntProvider(DataPool.<IntProvider>builder().add(ConstantIntProvider.create(2), 1).add(ConstantIntProvider.create(2), 1).add(ConstantIntProvider.create(3), 1).build()), UniformIntProvider.create(2, 3), UniformIntProvider.create(-4, -3), UniformIntProvider.create(-1, 0)), BlockStateProvider.of(leaves.getDefaultState().with(Properties.PERSISTENT, true)), new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), ConstantIntProvider.create(3), 164), new TwoLayersFeatureSize(1, 0, 2)).ignoreVines();
    }

    /**
     * @author macuguita
     * @reason fix decaying leaves
     */
    @Overwrite
    private static BlockStateProvider randomFoliage(Block leaves, int weight, Block leaves2, int weight2) {
        return new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(leaves.getDefaultState().with(Properties.PERSISTENT, true), weight).add(leaves2.getDefaultState().with(Properties.PERSISTENT, true), weight2));
    }
}
