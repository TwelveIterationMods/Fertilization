package net.blay09.mods.fertilization;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Fertilization.MOD_ID)
public class NeoForgeFertilization {
    public NeoForgeFertilization(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        Balm.initialize(Fertilization.MOD_ID, context, Fertilization::initialize);
    }

}
