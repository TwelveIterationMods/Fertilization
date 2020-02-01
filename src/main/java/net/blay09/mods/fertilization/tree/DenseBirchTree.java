package net.blay09.mods.fertilization.tree;

import net.blay09.mods.fertilization.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.BirchTree;
import net.minecraft.block.trees.OakTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

public class DenseBirchTree extends BirchTree {

    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return Feature.NORMAL_TREE.withConfiguration(createTreeConfig());
    }

    private TreeFeatureConfig createTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseBirchLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.BIRCH_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(2, 0)
        ).baseHeight(5).heightRandA(2).foliageHeight(3).ignoreVines().setSapling(((IPlantable) Blocks.BIRCH_SAPLING)).build();
    }
}
