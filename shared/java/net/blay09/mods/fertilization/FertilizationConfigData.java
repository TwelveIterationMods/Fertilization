package net.blay09.mods.fertilization;

import com.google.common.collect.Lists;
import me.shedaniel.autoconfig.annotation.Config;
import net.blay09.mods.balm.config.BalmConfig;
import net.blay09.mods.balm.config.Comment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;

@Config(name = Fertilization.MOD_ID)
public class FertilizationConfigData extends BalmConfig {

    @Comment("Set to true if compressed bone meal drops should go straight into the player's inventory.")
    public boolean addDropsDirectlyToInventory = false;

    @Comment("Set to true if compressed bone meal drops should go straight into the machine's inventory when used automatically.")
    public boolean addDropsDirectlyToInventoryForFakePlayers = true;

    @Comment("This enables use of extremely compressed bone meal to turn saplings into large fancy trees.")
    public boolean allowBoneMealOnSaplings = true;

    @Comment("This enables use of normal Bone Meal on vines in order to grow them downwards.")
    public boolean allowBoneMealOnVines = true;

    @Comment("This enables use of normal Bone Meal on sugar canes in order to grow them upwards.")
    public boolean allowBoneMealOnSugarCanes = true;

    @Comment("The amount of bone meal applied to the plant when using compressed bone meal.")
    public int compressedBoneMealPower = 4;

    @Comment("The amount of bone meal applied to the plant when using extremely compressed bone meal.")
    public int extremelyCompressedBoneMealPower = 36;

    @Comment("The maximum amount of flowers that can spawn when using Florist's Bone Meal on grass.")
    public int floristsBoneMealMaxFlowers = 5;

    @Comment("The maximum range that flowers can spawn when using Florist's Bone Meal on grass.")
    public int floristsBoneMealMaxRange = 3;

    @Comment("List of blocks that can be duplicated by using Florist's Bone Meal on them.")
    public List<String> flowerBlocks = Lists.newArrayList(
            "minecraft:poppy",
            "minecraft:dandelion",
            "minecraft:blue_orchid",
            "minecraft:allium",
            "minecraft:azure_bluet",
            "minecraft:red_tulip",
            "minecraft:orange_tulip",
            "minecraft:white_tulip",
            "minecraft:pink_tulip",
            "minecraft:oxeye_daisy",
            "minecraft:rose_bush",
            "minecraft:peony",
            "minecraft:lilac",
            "minecraft:sunflower",
            "minecraft:cornflower",
            "minecraft:lily_of_the_valley");

    public boolean isFlowerBlock(Block block) {
        ResourceLocation registryName = Registry.BLOCK.getKey(block);
        return flowerBlocks.contains(registryName.toString());
    }

}
