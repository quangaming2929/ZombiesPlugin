package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.commands.SpawnpointCommands;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.GameMapImporter;
import io.github.zap.zombiesplugin.map.spawn.SpawnManagerImporter;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.GunImporter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class ZombiesPlugin extends JavaPlugin implements Listener {
    public static ZombiesPlugin instance;

    private ConfigFileManager config;
    private ProtocolManager protocolManager;

    private HashMap<String, GameManager> gameManagers;
    private HashMap<String, GameMap> maps;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        gameManagers = new HashMap<>();
        maps = new HashMap<>();

        registerConfigs();
        registerCommands();

        //test game for testing purposes that can be used for testing
        gameManagers.put("test_game", new GameManager("test_game"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerConfigs() {
        config = new ConfigFileManager(this, this.getDataFolder());
        config.addImporter("Gun", new GunImporter());
        config.addImporter("SpawnPointManager", new SpawnManagerImporter());
        config.addImporter("GameMapImporter", new GameMapImporter());
        config.reload();
    }

    private void registerCommands() {
        getCommand("gunDebug").setExecutor(new GunDebugCommands());
        getCommand("testentity").setExecutor(new SpawnpointCommands());
    }

    public ConfigFileManager getConfigManager() {
        return config;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public GameManager getGameManager(String id) {
        return gameManagers.getOrDefault(id, null);
    }

    public boolean removeGameManager(String id) {
        return gameManagers.remove(id) != null;
    }

    public boolean addGameManager(String id, GameManager manager) {
        return gameManagers.put(id, manager) != null;
    }

    public ArrayList<GameManager> getGameManagers() {
        return new ArrayList<>(gameManagers.values());
    }

    public GameMap getMap(String mapName) { return maps.get(mapName); }

    public boolean addMap(GameMap map, String name) {
        if(!maps.containsKey(name)) {
            maps.put(name, map);
            return true;
        }
        return false;
    }
}
