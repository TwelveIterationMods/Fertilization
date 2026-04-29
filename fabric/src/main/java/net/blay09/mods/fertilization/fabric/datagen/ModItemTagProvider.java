package net.blay09.mods.fertilization.fabric.datagen;

import net.blay09.mods.fertilization.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        final var fertilizers = builder(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "fertilizers")));
        fertilizers.add(ModItems.compressedBoneMeal.asResourceKey());
        fertilizers.add(ModItems.extremelyCompressedBoneMeal.asResourceKey());
        fertilizers.add(ModItems.floristsBoneMeal.asResourceKey());
    }

}