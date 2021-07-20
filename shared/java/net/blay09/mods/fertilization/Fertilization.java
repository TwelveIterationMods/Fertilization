package net.blay09.mods.fertilization;

import net.blay09.mods.fertilization.item.ModItems;

public class Fertilization {

    public static final String MOD_ID = "fertilization";

    public static void initialize() {
        FertilizationConfig.initialize();
        ModItems.initialize();

        // TODO BonemealHandler
    }

}