package net.blay09.mods.fertilization.worldgen;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class ExtremeTree extends Tree {
    private final AbstractTreeFeature<NoFeatureConfig> treeFeature;

    public ExtremeTree(AbstractTreeFeature<NoFeatureConfig> treeFeature) {
        this.treeFeature = treeFeature;
    }

    @Nullable
    @Override
    protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
        return treeFeature;
    }
}
