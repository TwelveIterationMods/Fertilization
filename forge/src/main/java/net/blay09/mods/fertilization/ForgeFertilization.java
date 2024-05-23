package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.minecraftforge.fml.common.Mod;

@Mod(Fertilization.MOD_ID)
public class ForgeFertilization {
    public ForgeFertilization() {
        Balm.initialize(Fertilization.MOD_ID, EmptyLoadContext.INSTANCE, Fertilization::initialize);
    }

}
