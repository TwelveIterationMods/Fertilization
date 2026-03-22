package net.blay09.mods.fertilization;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.callback.BlockCallback;
import net.blay09.mods.fertilization.block.ModBlocks;
import net.blay09.mods.fertilization.item.ModItems;
import net.minecraft.resources.Identifier;

public class Fertilization {

    public static final String MOD_ID = "fertilization";

    public static void initialize(BalmRegistrars registrars) {
        FertilizationConfig.initialize();

        registrars.blocks(ModBlocks::initialize);
        registrars.items(ModItems::initialize);
        registrars.creativeModeTabs(ModItems::initialize);
        ModWorldGen.initialize(Balm.biomeModifications());

        BlockCallback.Use.EVENT.register(BoneMealUseBlockHandler::onBonemealVinesAndSugarCanes);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
