package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class ModWorldGen {
    private static final Map<Block, ResourceKey<ConfiguredFeature<?, ?>>> fancyTrees = new HashMap<>();
    private static final Map<Block, ResourceKey<ConfiguredFeature<?, ?>>> fancyBeeTrees = new HashMap<>();

    public static void initialize(BalmWorldGen worldGen) {
        registerFancyTree(Blocks.OAK_SAPLING, "oak");
        registerFancyTree(Blocks.SPRUCE_SAPLING, "spruce");
        registerFancyTree(Blocks.BIRCH_SAPLING, "birch");
        registerFancyTree(Blocks.JUNGLE_SAPLING, "jungle");
        registerFancyTree(Blocks.ACACIA_SAPLING, "acacia");
        registerFancyTree(Blocks.DARK_OAK_SAPLING, "dark_oak");
    }

    private static void registerFancyTree(Block sapling, String name) {
        fancyTrees.put(sapling, ResourceKey.create(Registries.CONFIGURED_FEATURE, id("fancy_" + name)));
        fancyBeeTrees.put(sapling, ResourceKey.create(Registries.CONFIGURED_FEATURE, id("fancy_" + name + "_bees")));
    }

    @Nullable
    public static AbstractTreeGrower getFancyTreeForSapling(BlockState state) {
        ResourceKey<ConfiguredFeature<?, ?>> fancyTree = fancyTrees.get(state.getBlock());
        ResourceKey<ConfiguredFeature<?, ?>> fancyBeeTree = fancyBeeTrees.get(state.getBlock());
        if (fancyTree != null && fancyBeeTree != null) {
            return new FancyTree(fancyTree, fancyBeeTree);
        }

        return null;
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
