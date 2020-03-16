package io.github.zap.zombiesplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ZombiesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().log(Level.ALL, "Hello Minecraft!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
