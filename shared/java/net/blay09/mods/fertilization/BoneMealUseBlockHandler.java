package net.blay09.mods.fertilization;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BoneMealUseBlockHandler {

    public static InteractionResult onBonemealVinesAndSugarCanes(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockPos pos = hit.getBlockPos();
        if (itemStack.getItem() != Items.BONE_MEAL) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        Block growthBlock = state.getBlock();
        if (growthBlock != Blocks.VINE && growthBlock != Blocks.SUGAR_CANE) {
            return InteractionResult.PASS;
        }

        if (growthBlock == Blocks.SUGAR_CANE && !FertilizationConfig.getActive().allowBoneMealOnSugarCanes) {
            return InteractionResult.PASS;
        } else if (growthBlock == Blocks.VINE && !FertilizationConfig.getActive().allowBoneMealOnVines) {
            return InteractionResult.PASS;
        }

        boolean growUpwards = growthBlock == Blocks.SUGAR_CANE;

        BlockPos candidatePos = pos;
        while (level.getBlockState(candidatePos).getBlock() == growthBlock) {
            candidatePos = growUpwards ? candidatePos.above() : candidatePos.below();
        }

        if (!level.isEmptyBlock(candidatePos) || level.isOutsideBuildHeight(candidatePos)) {
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide) {
            level.setBlock(candidatePos, state, 3);

            level.levelEvent(2005, pos, 0);
        }

        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

}
