package net.blay09.mods.fertilization.tree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;

import javax.annotation.Nullable;

public class TreeHelper {

    @Nullable
    public static Tree getDenseTree(BlockState state) {
        if (state.getBlock() instanceof SaplingBlock) {
            Block block = state.getBlock();
            if (block == Blocks.OAK_SAPLING) {
                return new DenseOakTree();
            } else if (block == Blocks.SPRUCE_SAPLING) {
                return new DenseSpruceTree();
            } else if (block == Blocks.DARK_OAK_SAPLING) {
                return new DenseDarkOakTree();
            } else if (block == Blocks.BIRCH_SAPLING) {
                return new DenseBirchTree();
            } else if (block == Blocks.JUNGLE_SAPLING) {
                return new DenseJungleTree();
            } else if (block == Blocks.ACACIA_SAPLING) {
                return new DenseAcaciaTree();
            }
        }
        return null;
    }

}
