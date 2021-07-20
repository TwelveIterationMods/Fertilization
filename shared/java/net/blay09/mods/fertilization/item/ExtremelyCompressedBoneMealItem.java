package net.blay09.mods.fertilization.item;

import net.blay09.mods.balm.BalmHooks;
import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class ExtremelyCompressedBoneMealItem extends CompressedBoneMealItem {

    public ExtremelyCompressedBoneMealItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);
        final AbstractTreeGrower tree = BoneMealHelper.getFancyTreeForSapling(state);
        if (FertilizationConfig.getActive().allowBoneMealOnSaplings && tree != null) {
            if (!level.isClientSide) {
                if (!BalmHooks.saplingGrowTree(level, level.random, pos)) {
                    return InteractionResult.FAIL;
                }

                tree.growTree(((ServerLevel) level), ((ServerLevel) level).getChunkSource().getGenerator(), pos, state, level.random);

                if (!player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    @Override
    protected int getBoneMealCount() {
        return FertilizationConfig.getActive().extremelyCompressedBoneMealPower;
    }

}