package net.blay09.mods.fertilization.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.fertilization.BoneMealHelper;
import net.blay09.mods.fertilization.FertilizationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CompressedBoneMealItem extends Item {

    public CompressedBoneMealItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);
        InteractionHand hand = context.getHand();

        if (player == null) {
            return InteractionResult.PASS;
        }

        ItemStack handItem = player.getItemInHand(hand);
        InteractionResult result = applyBoneMeal(level, pos, state, handItem, player);
        if (result == InteractionResult.FAIL) {
            player.swing(hand);
        } else if (result == InteractionResult.SUCCESS) {
            if (!player.getAbilities().instabuild) {
                handItem.shrink(1);
            }
        }

        return result;
    }

    public InteractionResult applyBoneMeal(Level level, BlockPos pos, BlockState state, ItemStack itemStack, @Nullable Player player) {
        if (!(state.getBlock() instanceof BonemealableBlock) || !((BonemealableBlock) state.getBlock()).isBonemealSuccess(level, level.random, pos, state)) {
            return InteractionResult.PASS;
        }

        // Disable grass, no one would want to waste their hard-earned bone meal on that.
        if (BoneMealHelper.isGrowableDisabledForCompressed(state)) {
            return InteractionResult.FAIL;
        }

        boolean isStem = BoneMealHelper.isStemCrop(state);
        ItemStack boneMealStack = itemStack.copy();
        for (int i = 0; i < getBoneMealCount(); i++) {
            BoneMealHelper.tryHarvest(player, level, pos);

            boolean boneMealApplied = Balm.getHooks().growCrop(boneMealStack, level, pos, player);
            if (!boneMealApplied && !isStem) {
                break;
            }

            if (isStem && !level.isClientSide) {
                state.tick((ServerLevel) level, pos, level.random);
            }
        }

        return InteractionResult.SUCCESS;
    }

    protected int getBoneMealCount() {
        return FertilizationConfig.getActive().compressedBoneMealPower;
    }

}
