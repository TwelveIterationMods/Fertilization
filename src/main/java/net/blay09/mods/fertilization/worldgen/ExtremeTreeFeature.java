package net.blay09.mods.fertilization.worldgen;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.HugeTreesFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

public class ExtremeTreeFeature extends HugeTreesFeature<NoFeatureConfig> {

    public ExtremeTreeFeature(boolean notify, int baseHeight, int extraRandomHeight, IBlockState logState, IBlockState leavesState) {
        super(notify, baseHeight, extraRandomHeight, logState, leavesState);
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorld world, Random rand, BlockPos pos) {
        boolean isJungleTree = woodMetadata.getBlock() == Blocks.JUNGLE_LOG;
        int height = getHeight(rand);

        if (!func_203427_a(world, pos, height)) { // ensureGrowable
            return false;
        }

        createCrown(world, pos.up(height), 1);
        createCrown(world, pos.up(height - 5), 2);
        createCrown(world, pos.up(height - 7), 3);
        createCrown(world, pos.up(height - 10), 5);

        // Spawn branches
        int startY = pos.getY() + height - 2 - rand.nextInt(4);
        int endY = pos.getY() + height / 4;
        for (int y = startY; y > endY; y -= 2 + rand.nextInt(1)) {
            for (int i = 0; i < 1 + rand.nextInt(4); i++) {
                float f = rand.nextFloat() * ((float) Math.PI * 2f);
                int x = pos.getX() + (int) (0.5f + MathHelper.cos(f) * 4f);
                int z = pos.getZ() + (int) (0.5f + MathHelper.sin(f) * 4f);

                for (int j = 0; j < 8; j++) {
                    x = pos.getX() + (int) (1.5f + MathHelper.cos(f) * (float) j);
                    z = pos.getZ() + (int) (1.5f + MathHelper.sin(f) * (float) j);
                    func_208520_a(changedBlocks, world, new BlockPos(x, y - 3 + j / 2, z), woodMetadata); // setBlockAndNotifyAdequately
                }

                int leavesStartY = 1 + rand.nextInt(2);
                for (int leavesY = y - leavesStartY; leavesY <= y; leavesY++) {
                    int leavesLayer = leavesY - y;
                    growLeavesLayer(world, new BlockPos(x, leavesY, z), 1 - leavesLayer);
                }
            }
        }

        for (int y = 0; y < height; ++y) {
            BlockPos abovePos = pos.up(y);

            // Spawn the center wood block
            if (isAirOrLeaves(world, abovePos)) {
                func_208520_a(changedBlocks, world, abovePos, woodMetadata); // setBlockAndNotifyAdequately
            }

            // Spawn the outer lower logs
            if (y < height - 1) {
                for (int offsetX = -1; offsetX <= 1; offsetX++) {
                    for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                        BlockPos currentPos = new BlockPos(pos.getX() + offsetX, pos.getY() + y, pos.getZ() + offsetZ);

                        // In corners, only put vines, no wood
                        if (offsetX != 0 && offsetZ != 0) {
                            // And only put vines for jungle trees
                            if (isJungleTree) {
                                BooleanProperty vineDirectionProperty = getVineDirectionProperty(rand, offsetX, offsetZ);
                                if (vineDirectionProperty != null) {
                                    placeVine(changedBlocks, world, rand, currentPos, vineDirectionProperty);
                                }
                            }

                            continue;
                        }

                        if (isAirOrLeaves(world, currentPos)) {
                            func_208520_a(changedBlocks, world, currentPos, woodMetadata); // setBlockAndNotifyAdequately
                        }
                    }
                }
            }
        }

        return true;
    }

    @Nullable
    private BooleanProperty getVineDirectionProperty(Random random, int offsetX, int offsetZ) {
        if (random.nextBoolean()) {
            if (offsetX == 1) {
                return BlockVine.WEST;
            } else if (offsetX == -1) {
                return BlockVine.EAST;
            }
        } else {
            if (offsetZ == 1) {
                return BlockVine.NORTH;
            } else if (offsetZ == -1) {
                return BlockVine.SOUTH;
            }
        }

        return null;
    }

    private void placeVine(Set<BlockPos> changedBlocks, IWorld world, Random random, BlockPos pos, BooleanProperty vineProperty) {
        if (random.nextInt(3) > 0 && world.isAirBlock(pos)) {
            func_208520_a(changedBlocks, world, pos, Blocks.VINE.getDefaultState().with(vineProperty, true)); // setBlockAndNotifyAdequately
        }
    }

    private void createCrown(IWorld world, BlockPos pos, int width) {
        for (int i = -2; i <= 0; i++) {
            growLeavesLayerStrict(world, pos.up(i), width + 1 - i);
        }
    }

    private boolean isAirOrLeaves(IWorld world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock() instanceof BlockLeaves;
    }

}
