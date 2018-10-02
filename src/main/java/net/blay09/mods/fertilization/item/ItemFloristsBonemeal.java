package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemFloristsBonemeal extends Item {

    public static final String name = "florists_bonemeal";
    public static ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public ItemFloristsBonemeal() {
        setUnlocalizedName(registryName.toString());
        setCreativeTab(Fertilization.creativeTab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.DOUBLE_PLANT && state.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            state = world.getBlockState(pos.down());
        }

        if (FertilizationConfig.isFlowerBlock(state.getBlock())) {
            if (!world.isRemote) {
                NonNullList<ItemStack> drops = NonNullList.create();
                state.getBlock().getDrops(drops, world, pos, state, 0);
                for (ItemStack itemStack : drops) {
                    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.25f, pos.getZ() + 0.5f, itemStack);
                    world.spawnEntity(entityItem);
                }

                if (!player.capabilities.isCreativeMode) {
                    player.getHeldItem(hand).shrink(1);
                }

                world.playEvent(2005, pos, 0);
            }

            return EnumActionResult.SUCCESS;
        }

        if (isGrassBlock(state)) {
            if (!world.isRemote) {
                Random random = world.rand;
                final int tries = FertilizationConfig.floristsBoneMealMaxFlowers;
                final int range = FertilizationConfig.floristsBoneMealMaxRange;
                boolean spawnedAnyFlower = false;
                for (int i = 0; i < tries; i++) {
                    BlockPos flowerPos = new BlockPos(pos.getX() + random.nextInt(range * 2) - range, pos.getY() + 1, pos.getZ() + random.nextInt(range * 2) - range);
                    if (world.isAirBlock(flowerPos) && isGrassBlock(world.getBlockState(flowerPos.down()))) {
                        world.getBiome(flowerPos).plantFlower(world, random, flowerPos);
                        spawnedAnyFlower = true;
                    }
                }

                if (spawnedAnyFlower) {
                    world.playEvent(2005, pos.up(), 0);
                } else {
                    world.playEvent(2000, pos.up(), 4);
                }
            }

            if (!player.capabilities.isCreativeMode) {
                player.getHeldItem(hand).shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    private boolean isGrassBlock(IBlockState state) {
        return state.getBlock() == Blocks.GRASS;
    }

}
