package net.blay09.mods.fertilization.tree;

import net.blay09.mods.fertilization.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.AcaciaTree;
import net.minecraft.block.trees.OakTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

public class DenseAcaciaTree extends AcaciaTree {

    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return Feature.ACACIA_TREE.withConfiguration(createTreeConfig());
    }

    private TreeFeatureConfig createTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseAcaciaLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.ACACIA_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(2, 0)
        ).baseHeight(5).heightRandA(2).foliageHeight(2).trunkHeight(0).ignoreVines().setSapling(((IPlantable) Blocks.ACACIA_SAPLING)).build();
    }
}
