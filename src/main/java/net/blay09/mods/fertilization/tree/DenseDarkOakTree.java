package net.blay09.mods.fertilization.tree;

import net.blay09.mods.fertilization.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.DarkOakTree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

public class DenseDarkOakTree extends DarkOakTree {

    @Nullable
    @Override
    protected ConfiguredFeature<HugeTreeFeatureConfig, ?> func_225547_a_(Random p_225547_1_) {
        return Feature.DARK_OAK_TREE.withConfiguration(createTreeConfig());
    }

    private HugeTreeFeatureConfig createTreeConfig() {
        return new HugeTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseDarkOakLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.DARK_OAK_LEAVES.getDefaultState())
        ).baseHeight(6).setSapling((IPlantable) Blocks.DARK_OAK_SAPLING).build();
    }

}
