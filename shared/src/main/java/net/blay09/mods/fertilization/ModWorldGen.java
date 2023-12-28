package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModWorldGen {
    private static final Map<Block, TreeGrower> treeGrowers = new HashMap<>();

    public static void initialize(BalmWorldGen worldGen) {
        registerFancyTree(Blocks.OAK_SAPLING, "oak");
        registerFancyTree(Blocks.SPRUCE_SAPLING, "spruce");
        registerFancyTree(Blocks.BIRCH_SAPLING, "birch");
        registerFancyTree(Blocks.JUNGLE_SAPLING, "jungle");
        registerFancyTree(Blocks.ACACIA_SAPLING, "acacia");
        registerFancyTree(Blocks.DARK_OAK_SAPLING, "dark_oak");
    }

    private static void registerFancyTree(Block sapling, String name) {
        final var fancyTree = ResourceKey.create(Registries.CONFIGURED_FEATURE, id("fancy_" + name));
        final var fancyTreeBees = ResourceKey.create(Registries.CONFIGURED_FEATURE, id("fancy_" + name + "_bees"));
        treeGrowers.put(sapling, new TreeGrower(name, Optional.empty(), Optional.of(fancyTree), Optional.of(fancyTreeBees)));
    }

    @Nullable
    public static TreeGrower getFancyTreeForSapling(BlockState state) {
        return treeGrowers.get(state.getBlock());
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(Fertilization.MOD_ID, name);
    }

}
