package ru.lionzxy.startedpackmod;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = StartedPackMod.MODID, name = StartedPackMod.NAME, version = StartedPackMod.VERSION, acceptableRemoteVersions = "*")
public class StartedPackMod {
    public static final String MODID = "startedpack";
    public static final String NAME = "Started Pack Mod";
    public static final String VERSION = "1.0";
    private static StartedPackMod INSTANCE;
    private List<ItemStack> startedItems = new ArrayList<>();
    private LoginHandler loginHandler = new LoginHandler(startedItems);

    public StartedPackMod() {
        INSTANCE = this;
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        invalidate();
        MinecraftForge.EVENT_BUS.register(loginHandler);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new StartedPackCommand());
    }

    public void invalidate() {
        startedItems.clear();
        for (String item : StartedPack.startedItems) {
            String[] parts = item.split(",");
            if (parts.length != 2) {
                throw new RuntimeException("Item must be in format \"minecraft:dirt, 1\"");
            }
            startedItems.add(getItemStackFromString(parts[0].trim(), Integer.parseInt(parts[1].trim())));
        }
        loginHandler.invalidate(startedItems);
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    public static StartedPackMod getInstance() {
        return INSTANCE;
    }

    private static ItemStack getItemStackFromString(String in, int count) {
        String items[] = in.split(":");
        if (items.length == 2) {
            return GameRegistry.makeItemStack(items[0] + ":" + items[1], 0, count, null);
        }
        return GameRegistry.makeItemStack(items[0] + ":" + items[1], Integer.valueOf(items[2]), count, null);
    }
}
