package net.blay09.mods.fertilization.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.fertilization.Fertilization;
import net.fabricmc.api.ModInitializer;

public class FabricFertilization implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initializeMod(Fertilization.MOD_ID, EmptyLoadContext.INSTANCE, Fertilization::initialize);
    }
}
