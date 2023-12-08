package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.neoforged.fml.common.Mod;

@Mod(Fertilization.MOD_ID)
public class NeoForgeFertilization {
    public NeoForgeFertilization() {
        Balm.initialize(Fertilization.MOD_ID, Fertilization::initialize);
    }

}
