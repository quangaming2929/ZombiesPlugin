package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.manager.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class ZombiesPlugin extends JavaPlugin {
    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private ArrayList<GameManager> games;

    /**
     * Registers all custom listeners with Spigot.
     */
    private void registerListeners() {

    }

    @Override
    public void onEnable() {
        getLogger().log(Level.ALL, "Hello Minecraft!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
