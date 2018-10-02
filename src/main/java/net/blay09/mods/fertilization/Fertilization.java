package net.blay09.mods.fertilization;

import net.blay09.mods.fertilization.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Fertilization.MOD_ID, name = "Fertilization", acceptedMinecraftVersions = "[1.12]", acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber
public class Fertilization {

    public static final String MOD_ID = "fertilization";

    @Mod.Instance(MOD_ID)
    public static Fertilization instance;

    public static Logger logger = LogManager.getLogger(MOD_ID);

    public static CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.compressedBonemeal);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new BonemealHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        OreDictionary.registerOre("flower", new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("flower", new ItemStack(Blocks.YELLOW_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("flower", new ItemStack(Blocks.DOUBLE_PLANT, 1, OreDictionary.WILDCARD_VALUE));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        FertilizationConfig.onConfigReload();
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            FertilizationConfig.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerModels();
    }

}
