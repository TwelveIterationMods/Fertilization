package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.blay09.mods.fertilization.FlowerDuplication;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class FloristsBoneMealItem extends Item {

    public FloristsBoneMealItem(Properties properties) {
        super(properties);

        DispenserBlock.registerBehavior(this, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack itemStack) {
                this.setSuccess(true);
                Level level = source.level();
                BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
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
        pos = normalizeFlowerPos(pos, state);
        state = level.getBlockState(pos);

        if (applyBoneMeal(level, pos, state, player.getItemInHand(hand), player)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public boolean applyBoneMeal(Level level, BlockPos pos, BlockState state, ItemStack itemStack, @Nullable Player player) {
        if (FertilizationConfig.getActive().isFlowerBlock(state.getBlock())) {
            if (!level.isClientSide()) {
                final ServerLevel serverLevel = (ServerLevel) level;
                final BlockPos sourcePos = normalizeFlowerPos(pos, state);
                final BlockState sourceState = level.getBlockState(sourcePos);
                final BlockPos targetPos = findDuplicateFlowerPosition(serverLevel, sourcePos, sourceState);
                if (targetPos == null) {
                    level.levelEvent(2000, sourcePos, 4);
                    return false;
                }

                FlowerDuplication.spreadDuplicateFlower(serverLevel, sourcePos, sourceState, targetPos);

                if (player == null || !player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            }

            return true;
        }

        if (BoneMealHelper.isGrassBlock(state)) {
            if (!level.isClientSide()) {
                RandomSource random = level.getRandom();
                final int maxFlowers = FertilizationConfig.getActive().floristsBoneMealMaxFlowers;
                final int maxRange = Math.max(0, FertilizationConfig.getActive().floristsBoneMealMaxRange);
                final BlockPos origin = pos.above();
                boolean spawnedAnyFlower = false;
                int spawnedFlowers = 0;

                candidateLoop:
                for (int candidate = 0; candidate < 128 && spawnedFlowers < maxFlowers; candidate++) {
                    BlockPos flowerPos = origin;
                    final int walkLength = Math.min(candidate / 16, maxRange);
                    for (int step = 0; step < walkLength; step++) {
                        flowerPos = flowerPos.offset(
                                random.nextInt(3) - 1,
                                (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                                random.nextInt(3) - 1);
                        if (!BoneMealHelper.isGrassBlock(level.getBlockState(flowerPos.below()))
                                || level.getBlockState(flowerPos).isCollisionShapeFullBlock(level, flowerPos)) {
                            continue candidateLoop;
                        }
                    }

                    if (level.getBlockState(flowerPos).isAir()
                            && !level.isOutsideBuildHeight(flowerPos)
                            && plantFlower((ServerLevel) level, flowerPos, random)) {
                        spawnedAnyFlower = true;
                        spawnedFlowers++;
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

    private BlockPos normalizeFlowerPos(BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            return pos.below();
        }

        return pos;
    }

    @Nullable
    private BlockPos findDuplicateFlowerPosition(ServerLevel level, BlockPos sourcePos, BlockState sourceState) {
        final RandomSource random = level.getRandom();
        final var range = 4;
        for (int i = 0; i < 48; i++) {
            final BlockPos targetPos = sourcePos.offset(
                    random.nextInt(range * 2 + 1) - range,
                    random.nextInt(3) - 1,
                    random.nextInt(range * 2 + 1) - range);
            if (!targetPos.equals(sourcePos) && FlowerDuplication.canPlaceFlower(level, targetPos, sourceState)) {
                return targetPos;
            }
        }

        return null;
    }

    private boolean plantFlower(ServerLevel level, BlockPos pos, RandomSource random) {
        var features = level.getBiome(pos).value().getGenerationSettings().getBoneMealFeatures();
        if (features.isEmpty()) {
            features = getFallbackFlowerFeatures(level);
        }

        if (!features.isEmpty()) {
            final var placementFeature = Util.getRandom(features, random);
            return placementFeature.place(level, level.getChunkSource().getGenerator(), random, pos);
        }

        return false;
    }

    private List<ConfiguredFeature<?, ?>> getFallbackFlowerFeatures(ServerLevel level) {
        final var fallbackBiome = FertilizationConfig.getActive().floristsBoneMealFallbackBiome;
        if (fallbackBiome.isBlank()) {
            return List.of();
        }

        final var fallbackBiomeId = Identifier.tryParse(fallbackBiome);
        if (fallbackBiomeId == null) {
            return List.of();
        }

        return level.registryAccess()
                .lookupOrThrow(Registries.BIOME)
                .getOptional(fallbackBiomeId)
                .map(Biome::getGenerationSettings)
                .map(BiomeGenerationSettings::getBoneMealFeatures)
                .orElseGet(List::of);
    }
}
