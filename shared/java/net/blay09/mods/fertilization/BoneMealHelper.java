package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.fertilization.mixin.CropBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BoneMealHelper {

    @Nullable
    public static Item getSeedFromCrop(BlockState state) {
        if (state.getBlock() == Blocks.COCOA) {
            return Items.COCOA_BEANS;
        }

        if (state.getBlock() instanceof CropBlockAccessor) {
            return ((CropBlockAccessor) state.getBlock()).callGetBaseSeedId().asItem();
        }

        return null;
    }

    public static boolean isGrassBlock(BlockState state) {
        return state.getBlock() == Blocks.GRASS_BLOCK;
    }

    @Nullable
    public static AbstractTreeGrower getFancyTreeForSapling(BlockState state) {
        if (state.getBlock() == Blocks.OAK_SAPLING) {
            return new FancyTree(Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LEAVES.defaultBlockState(), Blocks.OAK_SAPLING.defaultBlockState());
        } else if (state.getBlock() == Blocks.SPRUCE_SAPLING) {
            return new FancyTree(Blocks.SPRUCE_LOG.defaultBlockState(), Blocks.SPRUCE_LEAVES.defaultBlockState(), Blocks.SPRUCE_SAPLING.defaultBlockState());
        } else if (state.getBlock() == Blocks.BIRCH_SAPLING) {
            return new FancyTree(Blocks.BIRCH_LOG.defaultBlockState(), Blocks.BIRCH_LEAVES.defaultBlockState(), Blocks.BIRCH_SAPLING.defaultBlockState());
        } else if (state.getBlock() == Blocks.JUNGLE_SAPLING) {
            return new FancyTree(Blocks.JUNGLE_LOG.defaultBlockState(), Blocks.JUNGLE_LEAVES.defaultBlockState(), Blocks.JUNGLE_SAPLING.defaultBlockState());
        } else if (state.getBlock() == Blocks.ACACIA_SAPLING) {
            return new FancyTree(Blocks.ACACIA_LOG.defaultBlockState(), Blocks.ACACIA_LEAVES.defaultBlockState(), Blocks.ACACIA_SAPLING.defaultBlockState());
        } else if (state.getBlock() == Blocks.DARK_OAK_SAPLING) {
            return new FancyTree(Blocks.DARK_OAK_LOG.defaultBlockState(), Blocks.DARK_OAK_LEAVES.defaultBlockState(), Blocks.DARK_OAK_SAPLING.defaultBlockState());
        }

        return null;
    }

    public static boolean tryHarvest(@Nullable Player player, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (tryHarvestGeneric(player, level, pos, state, it -> it.getBlock() instanceof CropBlock && ((CropBlock) it.getBlock()).isMaxAge(it), () -> ((CropBlock) state.getBlock()).getStateForAge(0), 0.25f)) {
            return true;
        }

        //noinspection RedundantIfStatement
        if (tryHarvestGeneric(player, level, pos, state, it -> it.getBlock() == Blocks.COCOA && it.getValue(CocoaBlock.AGE) >= 2, Blocks.COCOA::defaultBlockState, -0.75f)) {
            return true;
        }

        return false;
    }

    public static boolean tryHarvestGeneric(@Nullable Player player, Level level, BlockPos pos, BlockState state, Predicate<BlockState> isMature, Supplier<BlockState> newCropState, float spawnOffsetY) {
        if (!isMature.test(state)) {
            return false;
        }

        List<ItemStack> drops = level instanceof ServerLevel ? Block.getDrops(state, (ServerLevel) level, pos, null) : Collections.emptyList();

        Item seedItem = getSeedFromCrop(state);
        boolean foundSeed = false;
        for (ItemStack itemStack : drops) {
            if (!itemStack.isEmpty() && itemStack.getItem() == seedItem) {
                itemStack.shrink(1);
                foundSeed = true;
                break;
            }
        }

        ItemStack seedInInventory = player != null ? findSeedInInventory(player, seedItem) : ItemStack.EMPTY;
        if (!foundSeed && !seedInInventory.isEmpty()) {
            // If the crop did not drop a seed, try to find one in the player inventory and use that one instead.
            seedInInventory.shrink(1);
            foundSeed = true;
        }

        if (!foundSeed) {
            return false;
        }

        if (!level.isClientSide) {
            level.setBlockAndUpdate(pos, newCropState.get());

            for (ItemStack itemStack : drops) {
                if ((seedInInventory.isEmpty() && itemStack.getItem() == seedItem) || FertilizationConfig.getActive().addDropsDirectlyToInventory || (FertilizationConfig.getActive().addDropsDirectlyToInventoryForFakePlayers && Balm.getHooks().isFakePlayer(player)))
                {
                    if (player != null && player.getInventory().add(itemStack)) {
                        continue;
                    }
                }

                ItemEntity entityItem = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + spawnOffsetY, pos.getZ() + 0.5, itemStack);
                entityItem.setPickUpDelay(10);
                level.addFreshEntity(entityItem);
            }
        }

        return true;
    }

    private static ItemStack findSeedInInventory(Player player, @Nullable Item seedItem) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (!itemStack.isEmpty() && itemStack.getItem() == seedItem) {
                return itemStack;
            }
        }

        return ItemStack.EMPTY;
    }

    public static boolean isStemCrop(BlockState state) {
        return state.getBlock() == Blocks.MELON_STEM || state.getBlock() == Blocks.PUMPKIN_STEM;
    }

    public static boolean isGrowableDisabledForCompressed(BlockState state) {
        return isGrassBlock(state) || state.getBlock() == Blocks.TALL_GRASS;
    }
}
