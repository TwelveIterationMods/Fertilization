package net.blay09.mods.fertilization;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.reflection.Comment;
import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.NestedType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Set;

@Config(Fertilization.MOD_ID)
public class FertilizationConfig {

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
    public int extremelyCompressedBoneMealPower = 16;

    @Comment("The maximum amount of flowers that can spawn when using Florist's Bone Meal on grass.")
    public int floristsBoneMealMaxFlowers = 5;

    @Comment("The maximum range that flowers can spawn when using Florist's Bone Meal on grass.")
    public int floristsBoneMealMaxRange = 3;

    @Comment("The biome whose flowers are used when the current biome has no flowers configured. Leave empty to disable the fallback.")
    public String floristsBoneMealFallbackBiome = "minecraft:plains";

    @Comment("List of blocks that can be duplicated by using Florist's Bone Meal on them.")
    @NestedType(Identifier.class)
    public Set<Identifier> flowerBlocks = Set.of(
            Identifier.withDefaultNamespace("poppy"),
            Identifier.withDefaultNamespace("dandelion"),
            Identifier.withDefaultNamespace("blue_orchid"),
            Identifier.withDefaultNamespace("allium"),
            Identifier.withDefaultNamespace("azure_bluet"),
            Identifier.withDefaultNamespace("red_tulip"),
            Identifier.withDefaultNamespace("orange_tulip"),
            Identifier.withDefaultNamespace("white_tulip"),
            Identifier.withDefaultNamespace("pink_tulip"),
            Identifier.withDefaultNamespace("oxeye_daisy"),
            Identifier.withDefaultNamespace("rose_bush"),
            Identifier.withDefaultNamespace("peony"),
            Identifier.withDefaultNamespace("lilac"),
            Identifier.withDefaultNamespace("sunflower"),
            Identifier.withDefaultNamespace("cornflower"),
            Identifier.withDefaultNamespace("lily_of_the_valley"));

    public boolean isFlowerBlock(Block block) {
        final var id = BuiltInRegistries.BLOCK.getKey(block);
        return flowerBlocks.contains(id);
    }

    public static FertilizationConfig getActive() {
        return Balm.config().getActiveConfig(FertilizationConfig.class);
    }

    public static void initialize() {
        Balm.config().registerConfig(FertilizationConfig.class);
    }

}
