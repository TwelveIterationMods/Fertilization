package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.config.ConfigLocalization;

public class FertilizationConfig {
    public static FertilizationConfigData getActive() {
        return Balm.getConfig().getActive(FertilizationConfigData.class);
    }

    public static void initialize() {
        ConfigLocalization.enableModernTranslationKeys(Fertilization.MOD_ID);
        Balm.getConfig().registerConfig(FertilizationConfigData.class, null);
    }

}
