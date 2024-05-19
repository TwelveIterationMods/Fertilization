package net.blay09.mods.fertilization.fabric.datagen;

import net.blay09.mods.fertilization.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
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
}
