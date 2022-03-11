package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class FloristsBoneMealItem extends Item {

    public FloristsBoneMealItem(Properties properties) {
        super(properties);

        DispenserBlock.registerBehavior(this, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack itemStack) {
                this.setSuccess(true);
                Level level = source.getLevel();
                BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                BlockState state = level.getBlockState(pos);

                // When facing air, target the block below instead to spawn flowers nearby
                if (state.isAir()) {
                    pos = pos.below();
                    state = level.getBlockState(pos);
                }

                if (!applyBoneMeal(level, pos, state, itemStack, null)) {
                    this.setSuccess(false);
                }

                return itemStack;
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        InteractionHand hand = context.getHand();

        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            state = level.getBlockState(pos.below());
        }

        if (applyBoneMeal(level, pos, state, player.getItemInHand(hand), player)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public boolean applyBoneMeal(Level level, BlockPos pos, BlockState state, ItemStack itemStack, @Nullable Player player) {
        if (FertilizationConfig.getActive().isFlowerBlock(state.getBlock())) {
            if (!level.isClientSide) {
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null);
                for (ItemStack drop : drops) {
                    ItemEntity entityItem = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.25f, pos.getZ() + 0.5f, drop);
                    level.addFreshEntity(entityItem);
                }

                if (player == null || !player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                level.levelEvent(2005, pos, 0);
            }

            return true;
        }

        if (BoneMealHelper.isGrassBlock(state)) {
            if (!level.isClientSide) {
                Random random = level.random;
                final int tries = FertilizationConfig.getActive().floristsBoneMealMaxFlowers;
                final int range = FertilizationConfig.getActive().floristsBoneMealMaxRange;
                boolean spawnedAnyFlower = false;
                for (int i = 0; i < tries; i++) {
                    BlockPos flowerPos = new BlockPos(pos.getX() + random.nextInt(range * 2) - range, pos.getY() + 1, pos.getZ() + random.nextInt(range * 2) - range);
                    if (level.isEmptyBlock(flowerPos) && BoneMealHelper.isGrassBlock(level.getBlockState(flowerPos.below()))) {
                        plantFlower(((ServerLevel) level), flowerPos, random);
                        spawnedAnyFlower = true;
                    }
                }

                if (spawnedAnyFlower) {
                    level.levelEvent(2005, pos, 0);
                } else {
                    level.levelEvent(2000, pos.above(), 4);
                }
            }

            if (player == null || !player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            return true;
        }
        return false;
    }

    private void plantFlower(ServerLevel level, BlockPos pos, Random rand) {
        List<ConfiguredFeature<?, ?>> list = level.getBiome(pos).value().getGenerationSettings().getFlowerFeatures();
        if (list.isEmpty()) {
            return;
        }

        ConfiguredFeature<?, ?> configuredFeature = list.get(0);
        FeatureConfiguration config = configuredFeature.config();
        PlacedFeature placedFeature = ((RandomPatchConfiguration) config).feature().value();
        placedFeature.place(level, level.getChunkSource().getGenerator(), rand, pos);
    }
}
