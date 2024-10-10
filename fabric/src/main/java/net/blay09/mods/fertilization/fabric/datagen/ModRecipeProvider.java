package net.blay09.mods.fertilization.fabric.datagen;

import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, ModItems.compressedBoneMeal)
                        .pattern("BB")
                        .pattern("BB")
                        .define('B', Items.BONE_MEAL)
                        .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                        .save(exporter);

                shaped(RecipeCategory.MISC, ModItems.extremelyCompressedBoneMeal)
                        .pattern("BBB")
                        .pattern("BBB")
                        .pattern("BBB")
                        .define('B', ModItems.compressedBoneMeal)
                        .unlockedBy("has_compressed_bone_meal", has(ModItems.compressedBoneMeal))
                        .save(exporter);

                shaped(RecipeCategory.MISC, ModItems.floristsBoneMeal, 2)
                        .pattern("BF")
                        .pattern("FB")
                        .define('B', Items.BONE_MEAL)
                        .define('F', ItemTags.FLOWERS)
                        .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                        .save(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return Fertilization.MOD_ID;
    }
}
