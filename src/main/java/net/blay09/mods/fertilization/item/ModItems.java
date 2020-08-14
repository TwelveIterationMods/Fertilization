package net.blay09.mods.fertilization.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static CompressedBoneMealItem compressedBoneMeal;
    public static ExtremelyCompressedBoneMealItem extremelyCompressedBoneMeal;
    public static FloristsBonemealItem floristsBoneMeal;

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(
                compressedBoneMeal = ((CompressedBoneMealItem) new CompressedBoneMealItem().setRegistryName(CompressedBoneMealItem.registryName)),
                extremelyCompressedBoneMeal = (ExtremelyCompressedBoneMealItem) new ExtremelyCompressedBoneMealItem().setRegistryName(ExtremelyCompressedBoneMealItem.registryName),
                floristsBoneMeal = (FloristsBonemealItem) new FloristsBonemealItem().setRegistryName(FloristsBonemealItem.registryName)
        );

        registerBoneMealDispenseBehaviour(compressedBoneMeal);
        registerBoneMealDispenseBehaviour(extremelyCompressedBoneMeal);
        DispenserBlock.registerDispenseBehavior(floristsBoneMeal, new OptionalDispenseBehavior() {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                this.setSuccessful(true);
                World world = source.getWorld();
                BlockPos pos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                BlockState state = world.getBlockState(pos);

                // When facing air, target the block below instead to spawn flowers nearby
                if (state.isAir(world, pos)) {
                    pos = pos.down();
                    state = world.getBlockState(pos);
                }

                if (!floristsBoneMeal.applyBoneMeal(world, pos, state, stack, null)) {
                    this.setSuccessful(false);
                }

                return stack;
            }
        });
    }

    private static void registerBoneMealDispenseBehaviour(CompressedBoneMealItem boneMealItem) {
        DispenserBlock.registerDispenseBehavior(boneMealItem, new OptionalDispenseBehavior() {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                this.setSuccessful(true);
                World world = source.getWorld();
                BlockPos pos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                BlockState state = world.getBlockState(pos);
                if (boneMealItem.applyBoneMeal(world, pos, state, stack, null) != ActionResultType.SUCCESS) {
                    this.setSuccessful(false);
                } else if (!world.isRemote) {
                    world.playEvent(2005, pos, 0);
                }

                if (this.isSuccessful()) {
                    stack.shrink(1);
                }

                return stack;
            }
        });
    }
}
