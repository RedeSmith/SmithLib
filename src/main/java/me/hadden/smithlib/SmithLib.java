package me.hadden.smithlib;

import org.bukkit.plugin.java.JavaPlugin;

public class SmithLib extends JavaPlugin {

    private static SmithLib instance;
    public static final Logger LOGGER = new Logger();

    public SmithLib() {
        instance = this;
    }

    @Override
    public void onEnable() {
        instance = null;

        LOGGER.log(Logger.SmithLevel.INFO, "The plugin has been enabled!");

    }

    @Override
    public void onDisable() {

        LOGGER.log(Logger.SmithLevel.INFO, "The plugin has been disabled!");

    }

    public static SmithLib getInstance() {
        return instance;
    }
}
