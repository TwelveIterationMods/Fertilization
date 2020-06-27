package net.blay09.mods.fertilization;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class FertilizationConfig {

    public static class Common {
        public final ForgeConfigSpec.BooleanValue addDropsDirectlyToInventory;
        public final ForgeConfigSpec.BooleanValue addDropsDirectlyToInventoryForFakePlayers;
        public final ForgeConfigSpec.BooleanValue hugeTrees;
        public final ForgeConfigSpec.BooleanValue allowBoneMealOnVines;
        public final ForgeConfigSpec.BooleanValue allowBoneMealOnSugarCanes;
        public final ForgeConfigSpec.ConfigValue<Integer> compressedBoneMealPower;
        public final ForgeConfigSpec.ConfigValue<Integer> extremelyCompressedBoneMealPower;
        public final ForgeConfigSpec.ConfigValue<Integer> floristsBoneMealMaxFlowers;
        public final ForgeConfigSpec.ConfigValue<Integer> floristsBoneMealMaxRange;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> flowerBlocks;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Configuration for Fertilization").push("common");

            addDropsDirectlyToInventory = builder
                    .comment("Set to true if compressed bone meal drops should go straight into the player's inventory.")
                    .translation("fertilization.config.addDropsDirectlyToInventory")
                    .define("addDropsDirectlyToInventory", false);

            addDropsDirectlyToInventoryForFakePlayers = builder
                    .comment("Set to true if compressed bone meal drops should go straight into the machine's inventory when used automatically.")
                    .translation("fertilization.config.addDropsDirectlyToInventoryForFakePlayers")
                    .define("addDropsDirectlyToInventoryForFakePlayers", true);

            hugeTrees = builder
                    .comment("Set to true if Extremely Compressed Bone Meal should cause huge trees to grow.")
                    .translation("fertilization.config.hugeTrees")
                    .define("hugeTrees", true);

            allowBoneMealOnVines = builder
                    .comment("This enables use of normal Bone Meal on vines in order to grow them downwards.")
                    .translation("fertilization.config.allowBoneMealOnVines")
                    .define("allowBoneMealOnVines", true);

            allowBoneMealOnSugarCanes = builder
                    .comment("This enables use of normal Bone Meal on sugar canes in order to grow them upwards.")
                    .translation("fertilization.config.allowBoneMealOnSugarCanes")
                    .define("allowBoneMealOnSugarCanes", true);

            compressedBoneMealPower = builder
                    .comment("The amount of bone meal applied to the plant when using compressed bone meal.")
                    .translation("fertilization.config.compressedBoneMealPower")
                    .define("compressedBoneMealPower", 4);

            extremelyCompressedBoneMealPower = builder
                    .comment("The amount of bone meal applied to the plant when using extremely compressed bone meal.")
                    .translation("fertilization.config.extremelyCompressedBoneMealPower")
                    .define("extremelyCompressedBoneMealPower", 36);

            floristsBoneMealMaxFlowers = builder
                    .comment("The maximum amount of flowers that can spawn when using Florist's Bone Meal on grass.")
                    .translation("fertilization.config.floristsBoneMealMaxFlowers")
                    .define("floristsBoneMealMaxFlowers", 5);

            floristsBoneMealMaxRange = builder
                    .comment("The maximum range that flowers can spawn when using Florist's Bone Meal on grass.")
                    .translation("fertilization.config.floristsBoneMealMaxRange")
                    .define("floristsBoneMealMaxRange", 3);

            flowerBlocks = builder
                    .comment("List of blocks that can be duplicated by using Florist's Bone Meal on them.")
                    .translation("fertilization.config.flowerBlocks")
                    .defineList("flowerBlocks", Lists.newArrayList(
                            "minecraft:poppy",
                            "minecraft:dandelion",
                            "minecraft:blue_orchid",
                            "minecraft:allium",
                            "minecraft:azure_bluet",
                            "minecraft:red_tulip",
                            "minecraft:orange_tulip",
                            "minecraft:white_tulip",
                            "minecraft:pink_tulip",
                            "minecraft_oxeye_daisy",
                            "minecraft:rose_bush",
                            "minecraft:peony"), it -> it instanceof String);
        }
    }

    public static boolean isFlowerBlock(Block block) {
        ResourceLocation registryName = block.getRegistryName();
        if (registryName != null) {
            return COMMON.flowerBlocks.get().contains(registryName.toString());
        }

        return false;
    }

    static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

}
