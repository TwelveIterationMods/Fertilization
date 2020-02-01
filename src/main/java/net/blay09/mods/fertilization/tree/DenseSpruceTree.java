package net.blay09.mods.fertilization.tree;

import com.google.common.collect.ImmutableList;
import net.blay09.mods.fertilization.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.SpruceTree;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class DenseSpruceTree extends SpruceTree {

    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean p_225546_2_) {
        return Feature.NORMAL_TREE.withConfiguration(createTreeConfig());
    }

    private TreeFeatureConfig createTreeConfig() {
        return new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseSpruceLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.SPRUCE_LEAVES.getDefaultState()),
                new SpruceFoliagePlacer(2, 1))
                .baseHeight(6).heightRandA(3).trunkHeight(1).trunkHeightRandom(1).trunkTopOffsetRandom(2).ignoreVines()
                .setSapling((IPlantable)Blocks.SPRUCE_SAPLING).build();
    }

    @Override
    protected ConfiguredFeature<HugeTreeFeatureConfig, ?> func_225547_a_(Random random) {
        return Feature.MEGA_SPRUCE_TREE.withConfiguration(random.nextBoolean() ? createHugePineTreeConfig() : createHugeSpruceTreeConfig());
    }

    private HugeTreeFeatureConfig createHugeSpruceTreeConfig() {
        ImmutableList<TreeDecorator> decorators = ImmutableList.of(new AlterGroundTreeDecorator(new SimpleBlockStateProvider(Blocks.PODZOL.getDefaultState())));
        return new HugeTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseSpruceLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.SPRUCE_LEAVES.getDefaultState()))
                .baseHeight(13)
                .func_227283_b_(15).func_227284_c_(13).func_227282_a_(decorators)
                .setSapling((IPlantable) Blocks.SPRUCE_SAPLING).build();
    }

    private HugeTreeFeatureConfig createHugePineTreeConfig() {
        return new HugeTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(ModBlocks.denseSpruceLog.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.SPRUCE_LEAVES.getDefaultState())
        ).baseHeight(6).setSapling((IPlantable) Blocks.SPRUCE_SAPLING).build();
    }

}
