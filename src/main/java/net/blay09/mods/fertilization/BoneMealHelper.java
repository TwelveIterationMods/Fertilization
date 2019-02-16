package net.blay09.mods.fertilization;

import net.blay09.mods.fertilization.worldgen.ExtremeTree;
import net.blay09.mods.fertilization.worldgen.ExtremeTreeFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.trees.AbstractTree;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BoneMealHelper {

    public static Method getSeedMethod;

    @Nullable
    public static Item getSeedFromCrop(IBlockState state) {
        if (state.getBlock() == Blocks.COCOA) {
            return Items.COCOA_BEANS;
        }

        if (getSeedMethod == null) {
            getSeedMethod = TempUtils.findMethod(BlockCrops.class, Arrays.asList("getSeedsItem", "func_199772_f"));
            if (getSeedMethod == null) {
                return null;
            }

            getSeedMethod.setAccessible(true);
        }

        try {
            return (Item) getSeedMethod.invoke(state.getBlock());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isGrassBlock(IBlockState state) {
        return state.getBlock() == Blocks.GRASS_BLOCK;
    }

    public static boolean isSapling(IBlockState state) {
        return state.getBlock() instanceof BlockSapling;
    }

    public static boolean tryHarvest(EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (tryHarvestGeneric(player, world, pos, state, it -> it.getBlock() instanceof BlockCrops && ((BlockCrops) it.getBlock()).isMaxAge(it), () -> ((BlockCrops) state.getBlock()).withAge(0), 0.25f)) {
            return true;
        }

        if (tryHarvestGeneric(player, world, pos, state, it -> it.getBlock() == Blocks.COCOA && it.get(BlockCocoa.AGE) >= 2, Blocks.COCOA::getDefaultState, -0.75f)) {
            return true;
        }

        return false;
    }

    public static boolean tryHarvestGeneric(EntityPlayer player, World world, BlockPos pos, IBlockState state, Predicate<IBlockState> isMature, Supplier<IBlockState> newCropState, float spawnOffsetY) {
        if (!isMature.test(state)) {
            return false;
        }

        NonNullList<ItemStack> drops = NonNullList.create();
        state.getBlock().getDrops(state, drops, world, pos, 0);

        Item seedItem = getSeedFromCrop(state);
        boolean foundSeed = false;
        for (ItemStack itemStack : drops) {
            if (!itemStack.isEmpty() && itemStack.getItem() == seedItem) {
                itemStack.shrink(1);
                foundSeed = true;
                break;
            }
        }

        ItemStack seedInInventory = findSeedInInventory(player, seedItem);
        if (!foundSeed && !seedInInventory.isEmpty()) {
            // If the crop did not drop a seed, try to find one in the player inventory and use that one instead.
            seedInInventory.shrink(1);
            foundSeed = true;
        }

        if (!foundSeed) {
            return false;
        }

        if (!world.isRemote) {
            world.setBlockState(pos, newCropState.get());

            for (ItemStack itemStack : drops) {
                if ((seedInInventory.isEmpty() && itemStack.getItem() == seedItem) || FertilizationConfig.COMMON.addDropsDirectlyToInventory.get() || (FertilizationConfig.COMMON.addDropsDirectlyToInventoryForFakePlayers.get() && player instanceof FakePlayer)) {
                    if (player.inventory.addItemStackToInventory(itemStack)) {
                        continue;
                    }
                }

                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + spawnOffsetY, pos.getZ() + 0.5, itemStack);
                entityItem.setPickupDelay(10);
                world.spawnEntity(entityItem);
            }
        }

        return true;
    }

    private static ItemStack findSeedInInventory(EntityPlayer player, @Nullable Item seedItem) {
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (!itemStack.isEmpty() && itemStack.getItem() == seedItem) {
                return itemStack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Nullable
    public static AbstractTree getExtremeTree(IBlockState state) {
        if (state.getBlock() instanceof BlockSapling) {
            Block block = state.getBlock();
            IBlockState logState;
            IBlockState leavesState;
            if (block == Blocks.OAK_SAPLING) {
                logState = Blocks.OAK_LOG.getDefaultState();
                leavesState = Blocks.OAK_LEAVES.getDefaultState();
            } else if (block == Blocks.SPRUCE_SAPLING) {
                logState = Blocks.SPRUCE_LOG.getDefaultState();
                leavesState = Blocks.SPRUCE_LEAVES.getDefaultState();
            } else if (block == Blocks.DARK_OAK_SAPLING) {
                logState = Blocks.DARK_OAK_LOG.getDefaultState();
                leavesState = Blocks.DARK_OAK_LEAVES.getDefaultState();
            } else if (block == Blocks.BIRCH_SAPLING) {
                logState = Blocks.BIRCH_LOG.getDefaultState();
                leavesState = Blocks.BIRCH_LEAVES.getDefaultState();
            } else if (block == Blocks.JUNGLE_SAPLING) {
                logState = Blocks.JUNGLE_LOG.getDefaultState();
                leavesState = Blocks.JUNGLE_LEAVES.getDefaultState();
            } else if (block == Blocks.ACACIA_SAPLING) {
                logState = Blocks.ACACIA_LOG.getDefaultState();
                leavesState = Blocks.ACACIA_LEAVES.getDefaultState();
            } else {
                return null;
            }

            return new ExtremeTree(new ExtremeTreeFeature(true, 20, 10, logState, leavesState));
        }

        return null;
    }

    public static boolean isStemCrop(IBlockState state) {
        return state.getBlock() == Blocks.MELON_STEM || state.getBlock() == Blocks.PUMPKIN_STEM;
    }

    public static boolean isGrowableDisabledForCompressed(IBlockState state) {
        return isGrassBlock(state) || state.getBlock() == Blocks.TALL_GRASS;
    }
}
