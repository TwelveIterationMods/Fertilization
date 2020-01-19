package net.blay09.mods.fertilization.worldgen;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class ExtremeTree extends Tree {
    private final AbstractTreeFeature<TreeFeatureConfig> treeFeature;

    public ExtremeTree(AbstractTreeFeature<TreeFeatureConfig> treeFeature) {
        this.treeFeature = treeFeature;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> func_225546_b_(Random random) {
        return treeFeature.func_225566_b_(DefaultBiomeFeatures.field_226815_j_);
    }
}
