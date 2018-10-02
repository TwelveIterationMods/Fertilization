package net.blay09.mods.fertilization;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.List;

@Config(modid = Fertilization.MOD_ID)
public class FertilizationConfig {


    @Config.Name("Flower Blocks")
    @Config.Comment("List of blocks that can be duplicated by using Florist's Bone Meal on them.")
    public static String[] flowerBlocks = {"minecraft:red_flower", "minecraft:yellow_flower", "minecraft:double_plant"};

    @Config.Name("Compressed Bone Meal Power")
    @Config.Comment("The amount of bone meal applied to the plant when using compressed bone meal.")
    public static int compressedBoneMealPower = 4;

    @Config.Name("Extremely Compressed Bone Meal Power")
    @Config.Comment("The amount of bone meal applied to the plant when using extremely compressed bone meal.")
    public static int extremelyCompressedBoneMealPower = 36;

    @Config.Name("Enable Huge Trees")
    @Config.Comment("Set to true if Extremely Compressed Bone Meal should cause huge trees to grow.")
    public static boolean hugeTrees = true;

    @Config.Name("Max Flowers to Spawn from Florist's Bonemeal")
    @Config.Comment("The maximum amount of flowers that can spawn when using Florist's Bone Meal on grass.")
    public static int floristsBoneMealMaxFlowers = 5;

    @Config.Name("Max Range to Spawn from Florist's Bonemeal")
    @Config.Comment("The maximum range that flowers can spawn when using Florist's Bone Meal on grass.")
    public static int floristsBoneMealMaxRange = 3;

    @Config.Name("Allow Bone Meal on Vines")
    @Config.Comment("This enables use of normal Bone Meal on vines in order to grow them downwards.")
    public static boolean allowBoneMealOnVines = true;

    private static List<Block> flowerBlocksList = new ArrayList<>();

    public static void onConfigReload() {
        flowerBlocksList.clear();

        for (String block : flowerBlocks) {
            flowerBlocksList.add(Block.getBlockFromName(block));
        }
    }

    public static boolean isFlowerBlock(Block block) {
        return flowerBlocksList.contains(block);
    }

}
