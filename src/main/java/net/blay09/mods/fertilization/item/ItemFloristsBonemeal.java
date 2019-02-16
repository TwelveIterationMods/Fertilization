package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.blay09.mods.fertilization.TempUtils;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.CompositeFlowerFeature;

import java.util.List;
import java.util.Random;

public class ItemFloristsBonemeal extends Item {

    public static final String name = "florists_bonemeal";
    public static ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public ItemFloristsBonemeal() {
        super(new Item.Properties().group(Fertilization.itemGroup));
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext useContext) {
        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        EntityPlayer player = useContext.getPlayer();
        if (player == null) {
            return EnumActionResult.PASS;
        }
        EnumHand hand = TempUtils.getItemUseHand(useContext);

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockDoublePlant && state.get(BlockDoublePlant.HALF) == DoubleBlockHalf.UPPER) {
            state = world.getBlockState(pos.down());
        }

        if (FertilizationConfig.isFlowerBlock(state.getBlock())) {
            if (!world.isRemote) {
                NonNullList<ItemStack> drops = NonNullList.create();
                state.getBlock().getDrops(state, drops, world, pos, 0);
                for (ItemStack itemStack : drops) {
                    EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.25f, pos.getZ() + 0.5f, itemStack);
                    world.spawnEntity(entityItem);
                }

                if (!player.abilities.isCreativeMode) {
                    player.getHeldItem(hand).shrink(1);
                }

                world.playEvent(2005, pos, 0);
            }

            return EnumActionResult.SUCCESS;
        }

        if (BoneMealHelper.isGrassBlock(state)) {
            if (!world.isRemote) {
                Random random = world.rand;
                final int tries = FertilizationConfig.COMMON.floristsBoneMealMaxFlowers.get();
                final int range = FertilizationConfig.COMMON.floristsBoneMealMaxRange.get();
                boolean spawnedAnyFlower = false;
                for (int i = 0; i < tries; i++) {
                    BlockPos flowerPos = new BlockPos(pos.getX() + random.nextInt(range * 2) - range, pos.getY() + 1, pos.getZ() + random.nextInt(range * 2) - range);
                    if (world.isAirBlock(flowerPos) && BoneMealHelper.isGrassBlock(world.getBlockState(flowerPos.down()))) {
                        plantFlower(world, flowerPos, random);
                        spawnedAnyFlower = true;
                    }
                }

                if (spawnedAnyFlower) {
                    world.playEvent(2005, pos.up(), 0);
                } else {
                    world.playEvent(2000, pos.up(), 4);
                }
            }

            if (!player.abilities.isCreativeMode) {
                player.getHeldItem(hand).shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    private void plantFlower(World world, BlockPos pos, Random rand) {
        IBlockState state;
        List<CompositeFlowerFeature<?>> list = world.getBiome(pos).getFlowers();
        if (list.isEmpty()) {
            return;
        }

        state = list.get(0).getRandomFlower(rand, pos);
        if (state.isValidPosition(world, pos)) {
            world.setBlockState(pos, state, 3);
        }
    }
}
