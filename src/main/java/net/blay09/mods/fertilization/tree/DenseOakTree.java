package net.blay09.mods.fertilization.tree;

import net.blay09.mods.fertilization.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.OakTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

public class DenseOakTree extends OakTree {

    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return random.nextInt(10) == 0
                ? Feature.FANCY_TREE.withConfiguration(createFancyTreeConfig())
                : Feature.NORMAL_TREE.withConfiguration(createTreeConfig());
    }

    private TreeFeatureConfig createFancyTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseOakLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(0, 0)
        ).setSapling((IPlantable) Blocks.OAK_SAPLING).build();
    }

    private TreeFeatureConfig createTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseOakLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(2, 0)
        ).baseHeight(4).heightRandA(2).foliageHeight(3).ignoreVines().setSapling(((IPlantable) Blocks.OAK_SAPLING)).build();
    }
}
