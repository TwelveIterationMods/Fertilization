package net.blay09.mods.fertilization.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.HugeTreesFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class ExtremeTreeFeature extends HugeTreesFeature<NoFeatureConfig> {

    public ExtremeTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config, boolean p_i51481_2_, int baseHeight, int extraRandomHeight, BlockState trunkState, BlockState leavesState) {
        super(config, p_i51481_2_, baseHeight, extraRandomHeight, trunkState, leavesState);
    }

    @Override
    protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos pos, MutableBoundingBox boundingBox) {
        boolean isJungleTree = trunk.getBlock() == Blocks.JUNGLE_LOG;
        int height = getHeight(rand);

        if (!func_203427_a(world, pos, height)) { // ensureGrowable
            return false;
        }

        createCrown(changedBlocks, world, pos.up(height), 1, boundingBox);
        createCrown(changedBlocks, world, pos.up(height - 5), 2, boundingBox);
        createCrown(changedBlocks, world, pos.up(height - 7), 3, boundingBox);
        createCrown(changedBlocks, world, pos.up(height - 10), 5, boundingBox);

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
                    setLogState(changedBlocks, world, new BlockPos(x, y - 3 + j / 2, z), trunk, boundingBox);
                }

                int leavesStartY = 1 + rand.nextInt(2);
                for (int leavesY = y - leavesStartY; leavesY <= y; leavesY++) {
                    int leavesLayer = leavesY - y;
                    func_203427_a(world, new BlockPos(x, leavesY, z), 1 - leavesLayer); // growLeavesLayer
                }
            }
        }

        for (int y = 0; y < height; ++y) {
            BlockPos abovePos = pos.up(y);

            // Spawn the center wood block
            if (isAirOrLeaves(world, abovePos)) {
                setLogState(changedBlocks, world, abovePos, trunk, boundingBox);
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
                                    placeVine(changedBlocks, world, rand, currentPos, vineDirectionProperty, boundingBox);
                                }
                            }

                            continue;
                        }

                        if (isAirOrLeaves(world, currentPos)) {
                            setLogState(changedBlocks, world, currentPos, trunk, boundingBox);
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
                return VineBlock.WEST;
            } else if (offsetX == -1) {
                return VineBlock.EAST;
            }
        } else {
            if (offsetZ == 1) {
                return VineBlock.NORTH;
            } else if (offsetZ == -1) {
                return VineBlock.SOUTH;
            }
        }

        return null;
    }

    private void placeVine(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random random, BlockPos pos, BooleanProperty vineProperty, MutableBoundingBox boundingBox) {
        if (random.nextInt(3) > 0 && isAir(world, pos)) {
            setLogState(changedBlocks, world, pos, Blocks.VINE.getDefaultState().with(vineProperty, true), boundingBox);
        }
    }

    private void createCrown(Set<BlockPos> changedBlocks, IWorldGenerationReader world, BlockPos pos, int width, MutableBoundingBox boundingBox) {
        for (int i = -2; i <= 0; i++) {
            func_222839_a(world, pos.up(i), width + 1 - i, boundingBox, changedBlocks); // growLeavesLayerStrict
        }
    }

    private boolean isAirOrLeaves(IWorldGenerationReader world, BlockPos pos) {
        return isAir(world, pos) || world.hasBlockState(pos, it -> it.getBlock() instanceof LeavesBlock);
    }

}
