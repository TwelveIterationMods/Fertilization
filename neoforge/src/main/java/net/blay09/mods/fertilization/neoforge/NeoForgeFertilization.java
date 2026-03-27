package net.blay09.mods.fertilization.neoforge;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.fertilization.Fertilization;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Fertilization.MOD_ID)
public class NeoForgeFertilization {
    public NeoForgeFertilization(ModContainer modContainer, IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modContainer, modEventBus);
        Balm.initializeMod(Fertilization.MOD_ID, context, Fertilization::initialize);
    }

}
