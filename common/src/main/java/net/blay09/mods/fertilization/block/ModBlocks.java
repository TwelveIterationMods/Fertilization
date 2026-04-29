package net.blay09.mods.fertilization.block;

import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

public class ModBlocks {

    public static DeferredBlock compressedBoneMealBlock;
    public static DeferredBlock extremelyCompressedBoneMealBlock;
    public static DeferredBlock floristsBoneMealBlock;

    public static void initialize(BalmBlockRegistrar blocks) {
        compressedBoneMealBlock = blocks.register("compressed_bonemeal_block", Block::new,
                        properties -> properties.mapColor(Blocks.BONE_BLOCK.defaultMapColor()).sound(SoundType.BONE_BLOCK).strength(2f, 3f))
                .withDefaultItem()
                .asDeferredBlock();
        extremelyCompressedBoneMealBlock = blocks.register("extremely_compressed_bonemeal_block", Block::new,
                        properties -> properties.mapColor(Blocks.BONE_BLOCK.defaultMapColor()).sound(SoundType.BONE_BLOCK).strength(2f, 3f))
                .withDefaultItem()
                .asDeferredBlock();
        floristsBoneMealBlock = blocks.register("florists_bonemeal_block", Block::new,
                        properties -> properties.mapColor(Blocks.BONE_BLOCK.defaultMapColor()).sound(SoundType.BONE_BLOCK).strength(2f, 3f))
                .withDefaultItem()
                .asDeferredBlock();
    }
}
