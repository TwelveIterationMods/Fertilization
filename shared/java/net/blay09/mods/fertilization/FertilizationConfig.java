package net.blay09.mods.fertilization;

import net.blay09.mods.balm.config.BalmConfigHolder;

public class FertilizationConfig {
    public static FertilizationConfigData getActive() {
        return BalmConfigHolder.getActive(FertilizationConfigData.class);
    }

    public static void initialize() {
        BalmConfigHolder.registerConfig(FertilizationConfigData.class, null);
    }

}
