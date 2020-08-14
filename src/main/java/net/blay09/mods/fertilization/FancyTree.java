package net.blay09.mods.fertilization;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;

public class FancyTree extends Tree {
    private final BlockState logState;
    private final BlockState leavesState;

    public FancyTree(BlockState logState, BlockState leavesState) {
        this.logState = logState;
        this.leavesState = leavesState;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean beePopulated) {
        return Feature.field_236291_c_.withConfiguration(beePopulated ? getBeePopulatedTreeConfig() : getFancyTreeConfig());
    }

    private BaseTreeFeatureConfig getFancyTreeConfig() {
        final FancyFoliagePlacer foliagePlacer = new FancyFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(4), 4);
        return (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leavesState), foliagePlacer, new FancyTrunkPlacer(3, 11, 0), new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))).setIgnoreVines().func_236702_a_(Heightmap.Type.MOTION_BLOCKING).build();
    }

    private BaseTreeFeatureConfig getBeePopulatedTreeConfig() {
        return getFancyTreeConfig().func_236685_a_(ImmutableList.of(new BeehiveTreeDecorator(0.05F)));
    }
}
