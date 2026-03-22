package net.blay09.mods.fertilization.fabric.datagen;

import net.blay09.mods.fertilization.Fertilization;
import net.blay09.mods.fertilization.block.ModBlocks;
import net.blay09.mods.fertilization.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;


public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        createColumnBlock(blockStateModelGenerator, ModBlocks.compressedBoneMealBlock.asBlock(), "compressed_bonemeal");
        createColumnBlock(blockStateModelGenerator, ModBlocks.extremelyCompressedBoneMealBlock.asBlock(), "extremely_compressed_bonemeal");
        createColumnBlock(blockStateModelGenerator, ModBlocks.floristsBoneMealBlock.asBlock(), "florists_bonemeal");
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.compressedBoneMeal.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.extremelyCompressedBoneMeal.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.floristsBoneMeal.asItem(), ModelTemplates.FLAT_ITEM);
    }

    private void createColumnBlock(BlockModelGenerators blockStateModelGenerator, Block block, String textureName) {
        final var sideTexture = Identifier.fromNamespaceAndPath(Fertilization.MOD_ID, "block/" + textureName + "_side");
        final var endTexture = Identifier.fromNamespaceAndPath(Fertilization.MOD_ID, "block/" + textureName + "_top");
        final var textureMapping = new TextureMapping()
                .put(TextureSlot.SIDE, sideTexture)
                .put(TextureSlot.END, endTexture)
                .put(TextureSlot.PARTICLE, sideTexture);
        final var model = ModelTemplates.CUBE_COLUMN.create(block, textureMapping, blockStateModelGenerator.modelOutput);
        blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, plainVariant(model)));
        blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
    }
}
