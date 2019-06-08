package net.blay09.mods.fertilization;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BonemealHandler {

    @SubscribeEvent
    public void onBonemealVinesAndSugarCanes(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() != Items.BONE_MEAL) {
            return;
        }

        BlockState state = event.getWorld().getBlockState(event.getPos());
        Block growthBlock = state.getBlock();
        if (growthBlock != Blocks.VINE && growthBlock != Blocks.SUGAR_CANE) {
            return;
        }

        if (growthBlock == Blocks.SUGAR_CANE && !FertilizationConfig.COMMON.allowBoneMealOnSugarCanes.get()) {
            return;
        } else if (growthBlock == Blocks.VINE && !FertilizationConfig.COMMON.allowBoneMealOnVines.get()) {
            return;
        }

        boolean growUpwards = growthBlock == Blocks.SUGAR_CANE;

        event.setCancellationResult(ActionResultType.SUCCESS);
        event.setCanceled(true);

        World world = event.getWorld();

        BlockPos candidatePos = event.getPos();
        while (world.getBlockState(candidatePos).getBlock() == growthBlock) {
            candidatePos = growUpwards ? candidatePos.up() : candidatePos.down();
        }

        if (!world.isAirBlock(candidatePos) || World.isOutsideBuildHeight(candidatePos)) {
            return;
        }

        if (!world.isRemote) {
            world.setBlockState(candidatePos, state, 3);

            world.playEvent(2005, event.getPos(), 0);
        }

        if (!event.getEntityPlayer().playerAbilities.isCreativeMode) {
            event.getItemStack().shrink(1);
        }
    }

}
