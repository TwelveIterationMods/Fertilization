package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ExtremelyCompressedBoneMealItem extends CompressedBoneMealItem {

    public static final String name = "extremely_compressed_bonemeal";
    public static ResourceLocation registryName = new ResourceLocation(Fertilization.MOD_ID, name);

    public ExtremelyCompressedBoneMealItem() {
        super(new Item.Properties().group(Fertilization.itemGroup));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext useContext) {
        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        PlayerEntity player = useContext.getPlayer();
        if (player == null) {
            return ActionResultType.PASS;
        }

        BlockState state = world.getBlockState(pos);
        if (FertilizationConfig.COMMON.hugeTrees.get() && BoneMealHelper.isSapling(state)) {
            if (!ForgeEventFactory.saplingGrowTree(world, random, pos)) {
                return ActionResultType.FAIL;
            }

            Tree tree = BoneMealHelper.getExtremeTree(state);
            if (tree != null) {
                tree.spawn(world, pos, state, random);

                if (!player.playerAbilities.isCreativeMode) {
                    useContext.getItem().shrink(1);
                }
            }

            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(useContext);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    protected int getBoneMealCount() {
        return FertilizationConfig.COMMON.extremelyCompressedBoneMealPower.get();
    }

}
