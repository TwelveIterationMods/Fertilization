package net.blay09.mods.fertilization;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BonemealHandler {

    @SubscribeEvent
    public void onBonemealVines(PlayerInteractEvent.RightClickBlock event) {
        if (!FertilizationConfig.allowBoneMealOnVines) {
            return;
        }

        if (event.getItemStack().getItem() != Items.DYE || event.getItemStack().getMetadata() != 15) {
            return;
        }

        IBlockState state = event.getWorld().getBlockState(event.getPos());
        if (state.getBlock() != Blocks.VINE) {
            return;
        }

        event.setCancellationResult(EnumActionResult.SUCCESS);
        event.setCanceled(true);

        World world = event.getWorld();

        BlockPos candidatePos = event.getPos();
        while (world.getBlockState(candidatePos).getBlock() == Blocks.VINE) {
            candidatePos = candidatePos.down();
        }

        if (!world.isAirBlock(candidatePos)) {
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


    @SubscribeEvent
    public void onBonemealReeds(PlayerInteractEvent.RightClickBlock event) {
        if (!FertilizationConfig.allowBoneMealOnSugarCanes) {
            return;
        }

        if (event.getItemStack().getItem() != Items.DYE || event.getItemStack().getMetadata() != 15) {
            return;
        }

        IBlockState state = event.getWorld().getBlockState(event.getPos());
        if (state.getBlock() != Blocks.REEDS) {
            return;
        }

        event.setCancellationResult(EnumActionResult.SUCCESS);
        event.setCanceled(true);

        World world = event.getWorld();

        BlockPos candidatePos = event.getPos();
        while (world.getBlockState(candidatePos).getBlock() == Blocks.REEDS) {
            candidatePos = candidatePos.up();
        }

        if (!world.isAirBlock(candidatePos)) {
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
