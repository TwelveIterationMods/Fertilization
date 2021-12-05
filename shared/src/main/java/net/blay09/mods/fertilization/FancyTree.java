package net.blay09.mods.fertilization;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.Random;

public class FancyTree extends AbstractTreeGrower {
    private final BlockState logState;
    private final BlockState leavesState;
    private final BlockState saplingState;

    public FancyTree(BlockState logState, BlockState leavesState, BlockState saplingState) {
        this.logState = logState;
        this.leavesState = leavesState;
        this.saplingState = saplingState;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean beePopulated) {
        return Feature.TREE.configured(beePopulated ? getBeePopulatedTreeConfig() : getFancyTreeConfig());
    }

    private TreeConfiguration getFancyTreeConfig() {
        final FancyFoliagePlacer foliagePlacer = new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4);
        return (new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(logState),
                new FancyTrunkPlacer(3, 11, 0),
                new SimpleStateProvider(leavesState),
                new SimpleStateProvider(saplingState),
                foliagePlacer,
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))))
                .ignoreVines()
                .build();
    }

    private TreeConfiguration getBeePopulatedTreeConfig() {
        return getFancyTreeConfig().withDecorators(ImmutableList.of(new BeehiveDecorator(0.05f)));
    }
}
