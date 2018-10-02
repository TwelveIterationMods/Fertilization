package net.blay09.mods.fertilization.item;

import net.blay09.mods.fertilization.Fertilization;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(Fertilization.MOD_ID)
public class ModItems {

    @GameRegistry.ObjectHolder(ItemCompressedBonemeal.name)
    public static Item compressedBonemeal;

    @GameRegistry.ObjectHolder(ItemExtremelyCompressedBonemeal.name)
    public static Item extremelyCompressedBonemeal;

    @GameRegistry.ObjectHolder(ItemFloristsBonemeal.name)
    public static Item floristsBonemeal;

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new ItemCompressedBonemeal().setRegistryName(ItemCompressedBonemeal.registryName),
                new ItemExtremelyCompressedBonemeal().setRegistryName(ItemExtremelyCompressedBonemeal.registryName),
                new ItemFloristsBonemeal().setRegistryName(ItemFloristsBonemeal.registryName)
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        ModelLoader.setCustomModelResourceLocation(compressedBonemeal, 0, new ModelResourceLocation(ItemCompressedBonemeal.registryName, "inventory"));
        ModelLoader.setCustomModelResourceLocation(extremelyCompressedBonemeal, 0, new ModelResourceLocation(ItemExtremelyCompressedBonemeal.registryName, "inventory"));
        ModelLoader.setCustomModelResourceLocation(floristsBonemeal, 0, new ModelResourceLocation(ItemFloristsBonemeal.registryName, "inventory"));
    }
}
