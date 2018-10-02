package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BonemealHelper;
import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCompressedBonemeal extends Item {

    public static final String name = "compressed_bonemeal";
    public static final ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public ItemCompressedBonemeal() {
        setUnlocalizedName(registryName.toString());
        setCreativeTab(Fertilization.creativeTab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof IGrowable) || !((IGrowable) state.getBlock()).canUseBonemeal(world, world.rand, pos, state)) {
            return EnumActionResult.PASS;
        }

        // Disable grass, no one would want to waste their hard-earned bone meal on that.
        if (BonemealHelper.isGrowableDisabledForCompressed(state)) {
            player.swingArm(hand);
            return EnumActionResult.FAIL;
        }

        boolean isStem = BonemealHelper.isStemCrop(state);

        ItemStack boneMealStack = player.getHeldItem(hand).copy();
        for (int i = 0; i < getBoneMealCount(); i++) {
            BonemealHelper.tryHarvest(player, world, pos);

            if (!ItemDye.applyBonemeal(boneMealStack, world, pos) && !isStem) {
                break;
            }

            if (isStem && !world.isRemote) {
                state.getBlock().updateTick(world, pos, state, world.rand);
            }
        }

        world.playEvent(2005, pos, 0);

        if (!player.capabilities.isCreativeMode) {
            player.getHeldItem(hand).shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    protected int getBoneMealCount() {
        return FertilizationConfig.compressedBoneMealPower;
    }

}
