package net.blay09.mods.fertilization.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItem;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.fertilization.Fertilization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ModItems {

    public static CreativeModeTab creativeModeTab = Balm.getItems().createCreativeModeTab(id("fertilization"), () -> new ItemStack(ModItems.compressedBoneMeal));

    public static CompressedBoneMealItem compressedBoneMeal = new CompressedBoneMealItem(Balm.getItems().itemProperties(creativeModeTab));
    public static ExtremelyCompressedBoneMealItem extremelyCompressedBoneMeal = new ExtremelyCompressedBoneMealItem(Balm.getItems().itemProperties(creativeModeTab));
    public static FloristsBoneMealItem floristsBoneMeal = new FloristsBoneMealItem(Balm.getItems().itemProperties(creativeModeTab));
    public static void initialize(BalmItems items) {
        items.registerItem(() -> compressedBoneMeal, id("compressed_bonemeal"));
        items.registerItem(() -> extremelyCompressedBoneMeal, id("extremely_compressed_bonemeal"));
        items.registerItem(() -> floristsBoneMeal, id("florists_bonemeal"));

        registerBoneMealDispenseBehaviour(compressedBoneMeal);
        registerBoneMealDispenseBehaviour(extremelyCompressedBoneMeal);
        DispenserBlock.registerBehavior(floristsBoneMeal, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack itemStack) {
                this.setSuccess(true);
                Level level = source.getLevel();
                BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                BlockState state = level.getBlockState(pos);

                // When facing air, target the block below instead to spawn flowers nearby
                if (state.isAir()) {
                    pos = pos.below();
                    state = level.getBlockState(pos);
                }

                if (!floristsBoneMeal.applyBoneMeal(level, pos, state, itemStack, null)) {
                    this.setSuccess(false);
                }

                return itemStack;
            }
        });
    }

    private static void registerBoneMealDispenseBehaviour(CompressedBoneMealItem boneMealItem) {
        DispenserBlock.registerBehavior(boneMealItem, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack itemStack) {
                this.setSuccess(true);
                Level level = source.getLevel();
                BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
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
