package net.blay09.mods.fertilization;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BonemealHelper {

    public static Method getSeedMethod;

    @Nullable
    public static Item getSeedFromCrop(IBlockState state) {
        if (state.getBlock() == Blocks.COCOA) {
            return Items.DYE;
        }

        if (getSeedMethod == null) {
            try {
                getSeedMethod = ReflectionHelper.findMethod(BlockCrops.class, "getSeed", "func_149866_i");
            } catch (ReflectionHelper.UnableToFindMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            return (Item) getSeedMethod.invoke(state.getBlock());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isGrassBlock(IBlockState state) {
        return state.getBlock() == Blocks.GRASS;
    }

    public static boolean isSapling(IBlockState state) {
        return state.getBlock() == Blocks.SAPLING;
    }

    public static boolean tryHarvest(EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (tryHarvestGeneric(player, world, pos, state, it -> it.getBlock() instanceof BlockCrops && ((BlockCrops) it.getBlock()).isMaxAge(it), () -> ((BlockCrops) state.getBlock()).withAge(0), 0.25f)) {
            return true;
        }

        if (tryHarvestGeneric(player, world, pos, state, it -> it.getBlock() == Blocks.COCOA && it.getValue(BlockCocoa.AGE) >= 2, Blocks.COCOA::getDefaultState, -0.75f)) {
            return true;
        }

        return false;
    }

    public static boolean tryHarvestGeneric(EntityPlayer player, World world, BlockPos pos, IBlockState state, Predicate<IBlockState> isMature, Supplier<IBlockState> newCropState, float spawnOffsetY) {
        if (!isMature.test(state)) {
            return false;
        }

        NonNullList<ItemStack> drops = NonNullList.create();
        state.getBlock().getDrops(drops, world, pos, state, 0);

        Item seedItem = getSeedFromCrop(state);
        boolean foundSeed = false;
        for (ItemStack itemStack : drops) {
            if (!itemStack.isEmpty() && itemStack.getItem() == seedItem) {
                itemStack.shrink(1);
                foundSeed = true;
                break;
            }
        }

        if (!foundSeed) {
            return false;
        }

        if (!world.isRemote) {
            world.setBlockState(pos, newCropState.get());

            for (ItemStack itemStack : drops) {
                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + spawnOffsetY, pos.getZ() + 0.5, itemStack);
                entityItem.setPickupDelay(10);
                world.spawnEntity(entityItem);
            }
        }

        return true;
    }

    public static IBlockState getSaplingLog(IBlockState state) {
        if (state.getBlock() instanceof BlockSapling) {
            switch (state.getValue(BlockSapling.TYPE)) {
                case OAK:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
                case SPRUCE:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
                case DARK_OAK:
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                case BIRCH:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
                case JUNGLE:
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                case ACACIA:
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
            }
        }

        return Blocks.AIR.getDefaultState();
    }

    public static IBlockState getSaplingLeaves(IBlockState state) {
        if (state.getBlock() instanceof BlockSapling) {
            switch (state.getValue(BlockSapling.TYPE)) {
                case OAK:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK);
                case SPRUCE:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE);
                case DARK_OAK:
                    return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK);
                case BIRCH:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH);
                case JUNGLE:
                    return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE);
                case ACACIA:
                    return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA);
            }
        }

        return Blocks.AIR.getDefaultState();
    }

    public static boolean isStemCrop(IBlockState state) {
        return state.getBlock() == Blocks.MELON_STEM || state.getBlock() == Blocks.PUMPKIN_STEM;
    }

    public static boolean isGrowableDisabledForCompressed(IBlockState state) {
        return isGrassBlock(state) || state.getBlock() == Blocks.TALLGRASS;
    }
}
