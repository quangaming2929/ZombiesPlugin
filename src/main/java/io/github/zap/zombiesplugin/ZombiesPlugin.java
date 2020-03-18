package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class ZombiesPlugin extends JavaPlugin {
    public static ZombiesPlugin instance;

    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private ArrayList<GameManager> games = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        games.add(new GameManager(new GameSettings()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
