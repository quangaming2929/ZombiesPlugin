package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.commands.SpawnpointCommands;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.GunImporter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class ZombiesPlugin extends JavaPlugin implements Listener {
    public static ZombiesPlugin instance;

    private ConfigFileManager config;
    private ProtocolManager protocolManager;
    public PlayerManager playerManager;

    private HashMap<String,GameManager> gameManagers;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        gameManagers = new HashMap<>();

        config = new ConfigFileManager(this, this.getDataFolder());
        config.addImporter("Gun", new GunImporter());
        config.reload();

        playerManager = new PlayerManager(null);
        Bukkit.getPluginManager().registerEvents(this,this);

        getCommand("gunDebug").setExecutor(new GunDebugCommands());

        SpawnpointCommands spawnpointCommands = new SpawnpointCommands();
        getCommand("gamemanager").setExecutor(spawnpointCommands);
        getCommand("spawnmanager").setExecutor(spawnpointCommands);
        getCommand("spawn").setExecutor(spawnpointCommands);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigFileManager getConfigManager() {
        return config;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // manager.addPlayer(e.getPlayer());
    }

    public GameManager getManager(String id) {
        return gameManagers.getOrDefault(id, null);
    }

    public boolean removeManager(String id) {
        return gameManagers.remove(id) != null;
    }

    public boolean addManager(String id, GameManager manager) {
        return gameManagers.put(id, manager) != null;
    }

    public ArrayList<GameManager> getManagers() {
        return new ArrayList<>(gameManagers.values());
    }
}
