package net.blay09.mods.fertilization.item;

import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.blay09.mods.fertilization.Fertilization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ModItems {

    public static DeferredItem compressedBoneMeal;
    public static DeferredItem extremelyCompressedBoneMeal;
    public static DeferredItem floristsBoneMeal;

    public static void initialize(BalmItemRegistrar items) {
        compressedBoneMeal = items.register("compressed_bonemeal", CompressedBoneMealItem::new).asDeferredItem();
        extremelyCompressedBoneMeal = items.register("extremely_compressed_bonemeal", ExtremelyCompressedBoneMealItem::new).asDeferredItem();
        floristsBoneMeal = items.register("florists_bonemeal", FloristsBoneMealItem::new).asDeferredItem();
    }

    public static void initialize(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register(Fertilization.MOD_ID, (id, builder) ->
                builder.title(Component.translatable(id.toLanguageKey("itemGroup")))
                        .icon(() -> ModItems.compressedBoneMeal.createStack())
                        .displayItems(((itemDisplayParameters, output) -> {
                                    output.accept(ModItems.compressedBoneMeal.createStack());
                                    output.accept(ModItems.extremelyCompressedBoneMeal.createStack());
                                    output.accept(ModItems.floristsBoneMeal.createStack());
                                })
                        ));
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
                } else if (!level.isClientSide()) {
                    level.levelEvent(2005, pos, 0);
                }

                if (this.isSuccess()) {
                    itemStack.shrink(1);
                }

                return itemStack;
            }
        });
    }

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(Fertilization.MOD_ID, name);
    }

    private static ResourceKey<Item> itemId(Identifier identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    private static Item.Properties defaultProperties(Identifier identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }
}
