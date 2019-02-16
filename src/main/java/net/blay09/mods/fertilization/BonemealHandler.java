package net.blay09.mods.fertilization;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
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

        IBlockState state = event.getWorld().getBlockState(event.getPos());
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

        event.setCancellationResult(EnumActionResult.SUCCESS);
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

        if (!event.getEntityPlayer().abilities.isCreativeMode) {
            event.getItemStack().shrink(1);
        }
    }

}
