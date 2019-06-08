package net.blay09.mods.fertilization.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static Item compressedBonemeal;
    public static Item extremelyCompressedBonemeal;
    public static Item floristsBonemeal;

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(
                compressedBonemeal = new CompressedBoneMealItem().setRegistryName(CompressedBoneMealItem.registryName),
                extremelyCompressedBonemeal = new ExtremelyCompressedBoneMealItem().setRegistryName(ExtremelyCompressedBoneMealItem.registryName),
                floristsBonemeal = new FloristsBonemealItem().setRegistryName(FloristsBonemealItem.registryName)
        );
    }
}
