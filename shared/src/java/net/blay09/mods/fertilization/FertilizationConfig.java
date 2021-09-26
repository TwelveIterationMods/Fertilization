package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;

public class FertilizationConfig {
    public static FertilizationConfigData getActive() {
        return Balm.getConfig().getActive(FertilizationConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(FertilizationConfigData.class, null);
    }

}
