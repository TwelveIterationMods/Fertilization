package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Set;

@Config(Fertilization.MOD_ID)
public class FertilizationConfigData implements BalmConfigData {

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
    @ExpectedType(ResourceLocation.class)
    public Set<ResourceLocation> flowerBlocks = Set.of(
            ResourceLocation.withDefaultNamespace("poppy"),
            ResourceLocation.withDefaultNamespace("dandelion"),
            ResourceLocation.withDefaultNamespace("blue_orchid"),
            ResourceLocation.withDefaultNamespace("allium"),
            ResourceLocation.withDefaultNamespace("azure_bluet"),
            ResourceLocation.withDefaultNamespace("red_tulip"),
            ResourceLocation.withDefaultNamespace("orange_tulip"),
            ResourceLocation.withDefaultNamespace("white_tulip"),
            ResourceLocation.withDefaultNamespace("pink_tulip"),
            ResourceLocation.withDefaultNamespace("oxeye_daisy"),
            ResourceLocation.withDefaultNamespace("rose_bush"),
            ResourceLocation.withDefaultNamespace("peony"),
            ResourceLocation.withDefaultNamespace("lilac"),
            ResourceLocation.withDefaultNamespace("sunflower"),
            ResourceLocation.withDefaultNamespace("cornflower"),
            ResourceLocation.withDefaultNamespace("lily_of_the_valley"));

    public boolean isFlowerBlock(Block block) {
        final var id = BuiltInRegistries.BLOCK.getKey(block);
        return flowerBlocks.contains(id);
    }

}
