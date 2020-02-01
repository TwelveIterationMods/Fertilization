package net.blay09.mods.fertilization.tree;

import com.google.common.collect.ImmutableList;
import net.blay09.mods.fertilization.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.JungleTree;
import net.minecraft.block.trees.SpruceTree;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class DenseJungleTree extends JungleTree {

    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean p_225546_2_) {
        return new TreeFeature(TreeFeatureConfig::deserializeJungle).withConfiguration(createTreeConfig());
    }

    private TreeFeatureConfig createTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseJungleLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()),
                new BlobFoliagePlacer(2, 0))
                .baseHeight(4).heightRandA(8).foliageHeight(3).ignoreVines()
                .setSapling((IPlantable)Blocks.JUNGLE_SAPLING).build();
    }

    @Override
    protected ConfiguredFeature<HugeTreeFeatureConfig, ?> func_225547_a_(Random random) {
        return Feature.MEGA_JUNGLE_TREE.withConfiguration(createHugeTreeConfig());
    }

    private HugeTreeFeatureConfig createHugeTreeConfig() {
        ImmutableList<TreeDecorator> decorators = ImmutableList.of(new TrunkVineTreeDecorator(), new LeaveVineTreeDecorator());
        return new HugeTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseJungleLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()))
                .baseHeight(10).func_227283_b_(20).func_227282_a_(decorators)
                .setSapling((IPlantable)Blocks.JUNGLE_SAPLING).build();
    }

}
