package net.blay09.mods.fertilization;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FancyTree extends AbstractTreeGrower {
    private final ConfiguredFeature<?, ?> configuredFeature;
    private final ConfiguredFeature<?, ?> configuredFeatureBees;

    public FancyTree(ConfiguredFeature<?, ?> configuredFeature, ConfiguredFeature<?, ?> configuredFeatureBees) {
        this.configuredFeature = configuredFeature;
        this.configuredFeatureBees = configuredFeatureBees;
    }

    @Nullable
    @Override
    protected Holder<ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean beePopulated) {
        return beePopulated ? Holder.direct(configuredFeatureBees) : Holder.direct(configuredFeature);
    }

}
