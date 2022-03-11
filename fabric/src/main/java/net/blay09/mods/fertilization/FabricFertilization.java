package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.fabricmc.api.ModInitializer;

public class FabricFertilization implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(Fertilization.MOD_ID, Fertilization::initialize);
    }
}
