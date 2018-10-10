package net.blay09.mods.fertilization;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BonemealHandler {

    @SubscribeEvent
    public void onBonemealVinesAndSugarCanes(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() != Items.DYE || event.getItemStack().getMetadata() != 15) {
            return;
        }

        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block growthBlock = state.getBlock();
        if (growthBlock != Blocks.VINE && growthBlock != Blocks.REEDS) {
            return;
        }

        if (growthBlock == Blocks.REEDS && !FertilizationConfig.allowBoneMealOnSugarCanes) {
            return;
        } else if (growthBlock == Blocks.VINE && !FertilizationConfig.allowBoneMealOnVines) {
            return;
        }

        boolean growUpwards = growthBlock == Blocks.REEDS;

        event.setCancellationResult(EnumActionResult.SUCCESS);
        event.setCanceled(true);

        World world = event.getWorld();

        BlockPos candidatePos = event.getPos();
        while (world.getBlockState(candidatePos).getBlock() == growthBlock) {
            candidatePos = growUpwards ? candidatePos.up() : candidatePos.down();
        }

        if (!world.isAirBlock(candidatePos) || world.isOutsideBuildHeight(candidatePos)) {
            return;
        }

        if (!world.isRemote) {
            world.setBlockState(candidatePos, state, 3);

            world.playEvent(2005, event.getPos(), 0);
        }

        if (!event.getEntityPlayer().capabilities.isCreativeMode) {
            event.getItemStack().shrink(1);
        }
    }

}
