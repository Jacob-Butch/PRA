package com.jake.pra;

import com.jake.pra.Commands.*;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.jake.pra.Info.PRA.*;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(
        modid = MODID,
        name = NAME,
        version = VERSION,
        serverSideOnly = true,
        dependencies = "required-after:pixelmon",
        acceptableRemoteVersions = "*"
)
public class PixelmonReforgedAdditions
{
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("==================================");
        LOGGER.info("||  Loading Pixelmon Reforged Additions...  ||");
        LOGGER.info("==================================");
        EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ConfigManager.load(MODID, Type.INSTANCE);
        ConfigManager.sync(MODID, Type.INSTANCE);

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        LOGGER.info("=================================");
        LOGGER.info("||   Pixelmon Reforged Additions Loaded   ||");
        LOGGER.info("=================================");
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        registerCommands(event);
    }

    private void registerCommands(FMLServerStartingEvent event){
        event.registerServerCommand(new RandomIV());
        event.registerServerCommand(new RerollIV());
        event.registerServerCommand(new ClearParty());
        event.registerServerCommand(new PRAcommand());
        event.registerServerCommand(new PokeCry());
        event.registerServerCommand(new Release());
        event.registerServerCommand(new PokeSound());
    }

    @Config(modid = MODID, type = Type.INSTANCE)
    @LangKey("pra.config.title")
    public static class CONFIG {
        @Comment("When '/reroll' is used on a legend, that legend will still have 3 IVs that are 31. (Set to false to disable this)")
        public static boolean legendRerollKeepIVs = true;
        public CONFIG(boolean setting){
            legendRerollKeepIVs = setting;
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(final OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Type.INSTANCE);
        }
    }
}