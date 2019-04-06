package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BonemealHelper;
import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.blay09.mods.fertilization.worldgen.WorldGenExtremeTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ItemExtremelyCompressedBonemeal extends ItemCompressedBonemeal {

    public static final String name = "extremely_compressed_bonemeal";
    public static ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public ItemExtremelyCompressedBonemeal() {
        setUnlocalizedName(registryName.toString());
        setCreativeTab(Fertilization.creativeTab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (FertilizationConfig.hugeTrees && BonemealHelper.isSapling(state)) {
            if (!TerrainGen.saplingGrowTree(world, world.rand, pos)) {
                return EnumActionResult.FAIL;
            }

            IBlockState logState = BonemealHelper.getSaplingLog(state);
            IBlockState leavesState = BonemealHelper.getSaplingLeaves(state);

            WorldGenerator worldGen = new WorldGenExtremeTree(true, 20, 10, logState, leavesState);

            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

            if (!worldGen.generate(world, world.rand, pos)) {
                world.setBlockState(pos, state, 4);

                if (!player.abilities.isCreativeMode) {
                    player.getHeldItem(hand).shrink(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    protected int getBoneMealCount() {
        return FertilizationConfig.extremelyCompressedBoneMealPower;
    }

}
