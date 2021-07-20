package net.blay09.mods.fertilization;

import net.fabricmc.api.ModInitializer;

public class FabricFertilization implements ModInitializer {
    @Override
    public void onInitialize() {
        Fertilization.initialize();
    }
}
