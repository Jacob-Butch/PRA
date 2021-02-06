package com.jake.pra;

import com.jake.pra.command.*;
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

@Mod(modid = "pradditions", name = "PRAdditions", version = "1.6", serverSideOnly = true,
        dependencies = "required-after:pixelmon", acceptableRemoteVersions = "*"
)
public class PixelmonReforgedAdditions {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Load and sync the config
        ConfigManager.load("pradditions", Type.INSTANCE);
        ConfigManager.sync("pradditions", Type.INSTANCE);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        this.registerCommands(event);
    }

    private void registerCommands(FMLServerStartingEvent event){
        // Register commands
        event.registerServerCommand(new RandomIV());
        event.registerServerCommand(new RerollIV());
        event.registerServerCommand(new ClearParty());
        event.registerServerCommand(new PRAcommand());
        event.registerServerCommand(new PokeCry());
        event.registerServerCommand(new Release());
        event.registerServerCommand(new PokeSound());
        event.registerServerCommand(new ClearBox());
    }

    // Plugin config
    @Config(modid = "pradditions", type = Type.INSTANCE)
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
        // Sync changes when the config is changed
        if (event.getModID().equals("pradditions")) {
            ConfigManager.sync("pradditions", Type.INSTANCE);
        }
    }
}