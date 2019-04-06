package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.blay09.mods.fertilization.TempUtils;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoneMeal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCompressedBoneMeal extends Item {

    public static final String name = "compressed_bonemeal";
    public static final ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public ItemCompressedBoneMeal() {
        this(new Item.Properties());
    }

    public ItemCompressedBoneMeal(Item.Properties properties) {
        super(properties.group(Fertilization.itemGroup));
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext useContext) {
        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        EntityPlayer player = useContext.getPlayer();
        IBlockState state = world.getBlockState(pos);
        EnumHand hand = TempUtils.getItemUseHand(useContext);

        if(player == null) {
            return EnumActionResult.PASS;
        }

        if (!(state.getBlock() instanceof IGrowable) || !((IGrowable) state.getBlock()).canUseBonemeal(world, world.rand, pos, state)) {
            return EnumActionResult.PASS;
        }

        // Disable grass, no one would want to waste their hard-earned bone meal on that.
        if (BoneMealHelper.isGrowableDisabledForCompressed(state)) {
            player.swingArm(hand);
            return EnumActionResult.FAIL;
        }

        boolean isStem = BoneMealHelper.isStemCrop(state);

        ItemStack boneMealStack = player.getHeldItem(hand).copy();
        for (int i = 0; i < getBoneMealCount(); i++) {
            BoneMealHelper.tryHarvest(player, world, pos);

            if (!ItemBoneMeal.applyBonemeal(boneMealStack, world, pos, player) && !isStem) {
                break;
            }

            if (isStem && !world.isRemote) {
                state.getBlock().tick(state, world, pos, world.rand);
            }
        }

        world.playEvent(2005, pos, 0);

        if (!player.abilities.isCreativeMode) {
            player.getHeldItem(hand).shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    protected int getBoneMealCount() {
        return FertilizationConfig.COMMON.compressedBoneMealPower.get();
    }

}
