package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.UseBlockEvent;
import net.blay09.mods.fertilization.item.ModItems;

public class Fertilization {

    public static final String MOD_ID = "fertilization";

    public static void initialize() {
        FertilizationConfig.initialize();

        ModItems.initialize(Balm.getItems());

        Balm.getEvents().onEvent(UseBlockEvent.class, BoneMealUseBlockHandler::onBonemealVinesAndSugarCanes);
    }

}
