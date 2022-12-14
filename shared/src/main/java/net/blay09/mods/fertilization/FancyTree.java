package net.blay09.mods.fertilization;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class FancyTree extends AbstractTreeGrower {
    private final ResourceKey<ConfiguredFeature<?, ?>> configuredFeature;
    private final ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureBees;

    public FancyTree(ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureBees) {
        this.configuredFeature = configuredFeature;
        this.configuredFeatureBees = configuredFeatureBees;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean beePopulated) {
        return beePopulated ? configuredFeatureBees : configuredFeature;
    }

}
