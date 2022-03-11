package net.blay09.mods.fertilization;

import com.google.common.collect.ImmutableList;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class ModWorldGen {
    private static final Map<Block, DeferredObject<ConfiguredFeature<TreeConfiguration, TreeFeature>>> fancyTrees = new HashMap<>();
    private static final Map<Block, DeferredObject<ConfiguredFeature<TreeConfiguration, TreeFeature>>> fancyBeeTrees = new HashMap<>();

    public static void initialize(BalmWorldGen worldGen) {
        registerFancyTree(worldGen, Blocks.OAK_SAPLING, "oak", Blocks.OAK_LOG, Blocks.OAK_LEAVES);
        registerFancyTree(worldGen, Blocks.SPRUCE_SAPLING, "spruce", Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES);
        registerFancyTree(worldGen, Blocks.BIRCH_SAPLING, "birch", Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES);
        registerFancyTree(worldGen, Blocks.JUNGLE_SAPLING, "jungle", Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES);
        registerFancyTree(worldGen, Blocks.ACACIA_SAPLING, "acacia", Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES);
        registerFancyTree(worldGen, Blocks.DARK_OAK_SAPLING, "dark_oak", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES);

        registerFancyBeeTree(worldGen, Blocks.OAK_SAPLING, "oak", Blocks.OAK_LOG, Blocks.OAK_LEAVES);
        registerFancyBeeTree(worldGen, Blocks.SPRUCE_SAPLING, "spruce", Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES);
        registerFancyBeeTree(worldGen, Blocks.BIRCH_SAPLING, "birch", Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES);
        registerFancyBeeTree(worldGen, Blocks.JUNGLE_SAPLING, "jungle", Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES);
        registerFancyBeeTree(worldGen, Blocks.ACACIA_SAPLING, "acacia", Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES);
        registerFancyBeeTree(worldGen, Blocks.DARK_OAK_SAPLING, "dark_oak", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES);
    }

    @Nullable
    public static AbstractTreeGrower getFancyTreeForSapling(BlockState state) {
        DeferredObject<ConfiguredFeature<TreeConfiguration, TreeFeature>> fancyTree = fancyTrees.get(state.getBlock());
        DeferredObject<ConfiguredFeature<TreeConfiguration, TreeFeature>> fancyBeeTree = fancyBeeTrees.get(state.getBlock());
        if (fancyTree != null && fancyBeeTree != null) {
            return new FancyTree(fancyTree.get(), fancyBeeTree.get());
        }

        return null;
    }

    private static void registerFancyTree(BalmWorldGen worldGen, Block saplingBlock, String name, Block logBlock, Block leavesBlock) {
        fancyTrees.put(saplingBlock, worldGen.registerConfiguredFeature(id("fancy_" + name), () -> (TreeFeature) Feature.TREE, () -> createFancyTreeConfig(logBlock, leavesBlock)));
    }

    private static void registerFancyBeeTree(BalmWorldGen worldGen, Block saplingBlock, String name, Block logBlock, Block leavesBlock) {
        fancyBeeTrees.put(saplingBlock, worldGen.registerConfiguredFeature(id("fancy_" + name + "_bee"), () -> (TreeFeature) Feature.TREE, () -> createBeePopulatedFancyTreeConfig(logBlock, leavesBlock)));
    }

    private static TreeConfiguration createFancyTreeConfig(Block logBlock, Block leavesBlock) {
        return createFancyTreeConfigBuilder(logBlock, leavesBlock).build();
    }

    private static TreeConfiguration createBeePopulatedFancyTreeConfig(Block logBlock, Block leavesBlock) {
        return createFancyTreeConfigBuilder(logBlock, leavesBlock)
                .decorators(ImmutableList.of(new BeehiveDecorator(0.05f)))
                .build();
    }

    private static TreeConfiguration.TreeConfigurationBuilder createFancyTreeConfigBuilder(Block logBlock, Block leavesBlock) {
        final FancyFoliagePlacer foliagePlacer = new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4);
        return (new TreeConfiguration.TreeConfigurationBuilder(
                SimpleStateProvider.simple(logBlock.defaultBlockState()),
                new FancyTrunkPlacer(3, 11, 0),
                SimpleStateProvider.simple(leavesBlock.defaultBlockState()),
                foliagePlacer,
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))))
                .ignoreVines();
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(Fertilization.MOD_ID, name);
    }

}
