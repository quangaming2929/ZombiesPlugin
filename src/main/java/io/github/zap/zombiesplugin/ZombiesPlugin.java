package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.manager.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class ZombiesPlugin extends JavaPlugin {
    public static ZombiesPlugin instance;

    private ProtocolManager protocolManager;
    private ArrayList<GameManager> games;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().log(Level.ALL, "Hello Minecraft!");
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
