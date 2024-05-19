package net.blay09.mods.fertilization.item;

import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.fertilization.Fertilization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ModItems {

    public static CompressedBoneMealItem compressedBoneMeal;
    public static ExtremelyCompressedBoneMealItem extremelyCompressedBoneMeal;
    public static FloristsBoneMealItem floristsBoneMeal;

    public static void initialize(BalmItems items) {
        items.registerItem(() -> compressedBoneMeal = new CompressedBoneMealItem(items.itemProperties()), id("compressed_bonemeal"));
        items.registerItem(() -> extremelyCompressedBoneMeal = new ExtremelyCompressedBoneMealItem(items.itemProperties()), id("extremely_compressed_bonemeal"));
        items.registerItem(() -> floristsBoneMeal = new FloristsBoneMealItem(items.itemProperties()), id("florists_bonemeal"));

        items.registerCreativeModeTab(() -> new ItemStack(ModItems.compressedBoneMeal), id("fertilization"));
    }

    public static void registerBoneMealDispenseBehaviour(CompressedBoneMealItem boneMealItem) {
        DispenserBlock.registerBehavior(boneMealItem, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack itemStack) {
                this.setSuccess(true);
                Level level = source.level();
                BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                BlockState state = level.getBlockState(pos);
                if (boneMealItem.applyBoneMeal(level, pos, state, itemStack, null) != InteractionResult.SUCCESS) {
                    this.setSuccess(false);
                } else if (!level.isClientSide) {
                    level.levelEvent(2005, pos, 0);
                }

                if (this.isSuccess()) {
                    itemStack.shrink(1);
                }

                return itemStack;
            }
        });
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(Fertilization.MOD_ID, name);
    }

}
