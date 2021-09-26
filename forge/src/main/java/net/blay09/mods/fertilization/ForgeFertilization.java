package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.minecraftforge.fml.common.Mod;

@Mod(Fertilization.MOD_ID)
public class ForgeFertilization {
    public ForgeFertilization() {
        Fertilization.initialize();

        Balm.initialize(Fertilization.MOD_ID);
    }

}
