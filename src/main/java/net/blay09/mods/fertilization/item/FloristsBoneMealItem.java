package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class FloristsBoneMealItem extends Item {

    public static final String name = "florists_bonemeal";
    public static ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public FloristsBoneMealItem() {
        super(new Item.Properties().group(Fertilization.itemGroup));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext useContext) {
        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        PlayerEntity player = useContext.getPlayer();
        if (player == null) {
            return ActionResultType.PASS;
        }

        Hand hand = useContext.getHand();

        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof DoublePlantBlock && state.get(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            state = world.getBlockState(pos.down());
        }

        if (applyBoneMeal(world, pos, state, player.getHeldItem(hand), player)) {
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public boolean applyBoneMeal(World world, BlockPos pos, BlockState state, ItemStack itemStack, @Nullable PlayerEntity player) {
        if (FertilizationConfig.isFlowerBlock(state.getBlock())) {
            if (!world.isRemote) {
                List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
                for (ItemStack drop : drops) {
                    ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.25f, pos.getZ() + 0.5f, drop);
                    world.addEntity(entityItem);
                }

                if (player == null || !player.abilities.isCreativeMode) {
                    itemStack.shrink(1);
                }

                world.playEvent(2005, pos, 0);
            }

            return true;
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
                    world.playEvent(2005, pos, 0);
                } else {
                    world.playEvent(2000, pos.up(), 4);
                }
            }

            if (player == null || !player.abilities.isCreativeMode) {
                itemStack.shrink(1);
            }

            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void plantFlower(World world, BlockPos pos, Random rand) {
        BlockState state;
        List<ConfiguredFeature<?, ?>> list = world.getBiome(pos).func_242440_e().func_242496_b();
        if (list.isEmpty()) {
            return;
        }

        final ConfiguredFeature<?, ?> config = list.get(0);
        final FlowersFeature<BlockClusterFeatureConfig> flowerFeature = (FlowersFeature<BlockClusterFeatureConfig>) config.feature;
        state = flowerFeature.getFlowerToPlace(rand, pos, ((BlockClusterFeatureConfig) config.func_242767_c()));
        if (state.isValidPosition(world, pos)) {
            world.setBlockState(pos, state, 3);
        }
    }
}
