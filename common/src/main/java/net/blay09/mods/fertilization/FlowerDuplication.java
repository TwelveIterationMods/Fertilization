package net.blay09.mods.fertilization;

import net.blay09.mods.balm.platform.event.callback.ServerTickCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FlowerDuplication {

    private static final int TRAIL_DURATION_TICKS = 12;
    private static final Queue<PendingFlowerDuplicate> pendingDuplicates = new ConcurrentLinkedQueue<>();

    public static void initialize() {
        ServerTickCallback.ServerLevelTick.AFTER.register(FlowerDuplication::onServerLevelTick);
    }

    public static void spreadDuplicateFlower(ServerLevel level, BlockPos sourcePos, BlockState sourceState, BlockPos targetPos) {
        pendingDuplicates.add(new PendingFlowerDuplicate(level.dimension(), level.getGameTime(), sourcePos.immutable(), sourceState, targetPos.immutable()));
    }

    private static void onServerLevelTick(ServerLevel level) {
        if (pendingDuplicates.isEmpty()) {
            return;
        }

        final long gameTime = level.getGameTime();
        final ResourceKey<Level> dimension = level.dimension();
        final Iterator<PendingFlowerDuplicate> iterator = pendingDuplicates.iterator();
        while (iterator.hasNext()) {
            final PendingFlowerDuplicate pendingDuplicate = iterator.next();
            if (!pendingDuplicate.dimension.equals(dimension)) {
                continue;
            }

            final int age = (int) (gameTime - pendingDuplicate.startTime);
            if (age < 0) {
                continue;
            }

            spawnTrailParticle(level, pendingDuplicate, Math.min(age, TRAIL_DURATION_TICKS));

            if (age >= TRAIL_DURATION_TICKS) {
                placeDuplicateFlower(level, pendingDuplicate);
                iterator.remove();
            }
        }
    }

    private static void spawnTrailParticle(ServerLevel level, PendingFlowerDuplicate pendingDuplicate, int age) {
        final RandomSource random = level.getRandom();
        final double progress = age / (double) TRAIL_DURATION_TICKS;
        final double sourceX = pendingDuplicate.sourcePos.getX() + 0.5;
        final double sourceY = pendingDuplicate.sourcePos.getY() + 0.65;
        final double sourceZ = pendingDuplicate.sourcePos.getZ() + 0.5;
        final double targetX = pendingDuplicate.targetPos.getX() + 0.5;
        final double targetY = pendingDuplicate.targetPos.getY() + 0.35;
        final double targetZ = pendingDuplicate.targetPos.getZ() + 0.5;
        final double arcHeight = 0.75 + pendingDuplicate.sourcePos.distManhattan(pendingDuplicate.targetPos) * 0.08;
        final double arcY = Math.sin(progress * Math.PI) * arcHeight;

        final double x = Mth.lerp(progress, sourceX, targetX);
        final double y = Mth.lerp(progress, sourceY, targetY) + arcY;
        final double z = Mth.lerp(progress, sourceZ, targetZ);
        level.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                x,
                y,
                z,
                3,
                0.04 + random.nextDouble() * 0.02,
                0.04 + random.nextDouble() * 0.02,
                0.04 + random.nextDouble() * 0.02,
                0.02);
    }

    private static void placeDuplicateFlower(ServerLevel level, PendingFlowerDuplicate pendingDuplicate) {
        final BlockPos targetPos = pendingDuplicate.targetPos;
        if (!canPlaceFlower(level, targetPos, pendingDuplicate.sourceState)) {
            level.levelEvent(2000, targetPos, 4);
            return;
        }

        if (pendingDuplicate.sourceState.getBlock() instanceof DoublePlantBlock) {
            level.setBlockAndUpdate(targetPos, pendingDuplicate.sourceState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
            level.setBlockAndUpdate(targetPos.above(), pendingDuplicate.sourceState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
        } else {
            level.setBlockAndUpdate(targetPos, pendingDuplicate.sourceState);
        }

        level.sendParticles(ParticleTypes.POOF, targetPos.getX() + 0.5, targetPos.getY() + 0.35, targetPos.getZ() + 0.5, 12, 0.25, 0.2, 0.25, 0.02);
    }

    public static boolean canPlaceFlower(ServerLevel level, BlockPos pos, BlockState state) {
        if (level.isOutsideBuildHeight(pos) || !level.getBlockState(pos).isAir()) {
            return false;
        }

        if (state.getBlock() instanceof DoublePlantBlock) {
            return !level.isOutsideBuildHeight(pos.above())
                    && level.getBlockState(pos.above()).isAir()
                    && state.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).canSurvive(level, pos);
        }

        return state.canSurvive(level, pos);
    }

    private record PendingFlowerDuplicate(ResourceKey<Level> dimension, long startTime, BlockPos sourcePos, BlockState sourceState, BlockPos targetPos) {
    }
}
