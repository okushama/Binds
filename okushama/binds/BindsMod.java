package okushama.binds;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "binds", name = "Binds Mod", version = "0.0.1")

public class BindsMod {

    @Instance("binds")
    public static BindsMod instance;
    
    public static Console console;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
    	if(event.getSide() == Side.CLIENT){
	    	log("Initializing Binds Mod!");
	    	log("#######################");
	    	console = new Console();
	    	TickRegistry.registerTickHandler(new Ticker(), Side.CLIENT);
	    	KeyBindingRegistry.registerKeyBinding(new Keybinds());
	    	Binds.init();
    	}
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {

    }
    
    public static Logger logger = Logger.getLogger("Binds Mod");
    
    static{
    	logger.setParent(FMLLog.getLogger());
    }


    public static void log(String s, boolean warning) {
        logger.log(warning ? Level.WARNING : Level.INFO, s);
    }

    public static void log(String s) {
        log(s, false);
    }
}
