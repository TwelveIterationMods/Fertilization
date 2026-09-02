package net.blay09.mods.fertilization.fabric.datagen;

import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.block.ModBlocks;
import net.blay09.mods.fertilization.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.compressedBoneMealBlock)
                        .pattern("BBB")
                        .pattern("BBB")
                        .pattern("BBB")
                        .define('B', ModItems.compressedBoneMeal)
                        .unlockedBy("has_compressed_bone_meal", has(ModItems.compressedBoneMeal))
                        .save(output);

                shaped(RecipeCategory.MISC, ModItems.compressedBoneMeal)
                        .pattern("BB")
                        .pattern("BB")
                        .define('B', Items.BONE_MEAL)
                        .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.compressedBoneMeal, 9)
                        .requires(ModBlocks.compressedBoneMealBlock)
                        .unlockedBy("has_compressed_bonemeal_block", has(ModBlocks.compressedBoneMealBlock))
                        .save(output, Fertilization.MOD_ID + ":compressed_bonemeal_from_block");

                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.extremelyCompressedBoneMealBlock)
                        .pattern("BBB")
                        .pattern("BBB")
                        .pattern("BBB")
                        .define('B', ModItems.extremelyCompressedBoneMeal)
                        .unlockedBy("has_extremely_compressed_bone_meal", has(ModItems.extremelyCompressedBoneMeal))
                        .save(output);

                shaped(RecipeCategory.MISC, ModItems.extremelyCompressedBoneMeal)
                        .pattern("BB")
                        .pattern("BB")
                        .define('B', ModItems.compressedBoneMeal)
                        .unlockedBy("has_compressed_bone_meal", has(ModItems.compressedBoneMeal))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.extremelyCompressedBoneMeal, 9)
                        .requires(ModBlocks.extremelyCompressedBoneMealBlock)
                        .unlockedBy("has_extremely_compressed_bonemeal_block", has(ModBlocks.extremelyCompressedBoneMealBlock))
                        .save(output, Fertilization.MOD_ID + ":extremely_compressed_bonemeal_from_block");

                shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.floristsBoneMealBlock)
                        .pattern("BBB")
                        .pattern("BBB")
                        .pattern("BBB")
                        .define('B', ModItems.floristsBoneMeal)
                        .unlockedBy("has_florists_bone_meal", has(ModItems.floristsBoneMeal))
                        .save(output);

                shaped(RecipeCategory.MISC, ModItems.floristsBoneMeal, 2)
                        .pattern("BF")
                        .pattern("FB")
                        .define('B', Items.BONE_MEAL)
                        .define('F', BlockItemTags.SMALL_FLOWERS.item())
                        .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.floristsBoneMeal, 9)
                        .requires(ModBlocks.floristsBoneMealBlock)
                        .unlockedBy("has_florists_bonemeal_block", has(ModBlocks.floristsBoneMealBlock))
                        .save(output, Fertilization.MOD_ID + ":florists_bonemeal_from_block");
            }
        };
    }

    @Override
    public String getName() {
        return Fertilization.MOD_ID;
    }
}
