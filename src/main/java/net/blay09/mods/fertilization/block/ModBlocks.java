package net.blay09.mods.fertilization.block;

import net.blay09.mods.fertilization.Fertilization;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static Block denseOakLog;
    public static Block denseBirchLog;
    public static Block denseSpruceLog;
    public static Block denseDarkOakLog;
    public static Block denseJungleLog;
    public static Block denseAcaciaLog;

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll(
                denseOakLog = new DenseLog(createDenseLogBlockProperties()).setRegistryName("dense_oak_log"),
                denseBirchLog = new DenseLog(createDenseLogBlockProperties()).setRegistryName("dense_birch_log"),
                denseSpruceLog = new DenseLog(createDenseLogBlockProperties()).setRegistryName("dense_spruce_log"),
                denseDarkOakLog = new DenseLog(createDenseLogBlockProperties()).setRegistryName("dense_dark_oak_log"),
                denseJungleLog = new DenseLog(createDenseLogBlockProperties()).setRegistryName("dense_jungle_log"),
                denseAcaciaLog = new DenseLog(createDenseLogBlockProperties()).setRegistryName("dense_acacia_log")
        );
    }

    private static Block.Properties createDenseLogBlockProperties() {
        return Block.Properties.create(Material.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(3f).sound(SoundType.WOOD);
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new BlockItem(denseOakLog, createItemBlockProperties()).setRegistryName("dense_oak_log"),
                new BlockItem(denseBirchLog, createItemBlockProperties()).setRegistryName("dense_birch_log"),
                new BlockItem(denseSpruceLog, createItemBlockProperties()).setRegistryName("dense_spruce_log"),
                new BlockItem(denseDarkOakLog, createItemBlockProperties()).setRegistryName("dense_dark_oak_log"),
                new BlockItem(denseJungleLog, createItemBlockProperties()).setRegistryName("dense_jungle_log"),
                new BlockItem(denseAcaciaLog, createItemBlockProperties()).setRegistryName("dense_acacia_log")
        );
    }

    private static Item.Properties createItemBlockProperties() {
        return new Item.Properties().group(Fertilization.itemGroup);
    }
}
