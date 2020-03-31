package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.commands.SpawnpointCommands;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameSettings;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.GameMapImporter;
import io.github.zap.zombiesplugin.map.spawn.SpawnManagerImporter;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalEscapeWindow;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalTargetPlayerUnbounded;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalUnboundedMeleeAttack;
import io.github.zap.zombiesplugin.pathfind.reflect.Hack;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.GunImporter;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public final class ZombiesPlugin extends JavaPlugin {
    public static ZombiesPlugin instance;

    private ConfigFileManager config;
    private ProtocolManager protocolManager;

    private Hashtable<String, GameManager> gameManagers;
    private Hashtable<String, GameMap> maps;

    public SpawnPoint lastSpawnpoint;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        gameManagers = new Hashtable<>();
        maps = new Hashtable<>();

        registerConfigs();
        registerCommands();

        gameManagers.put("test_game", new GameManager( "test_game", new GameSettings(), new GameMap("test_map")));

        try {
            HashSet<Class<?>> goals = new HashSet<>();
            goals.add(PathfinderGoalEscapeWindow.class);
            goals.add(PathfinderGoalUnboundedMeleeAttack.class);
            goals.add(PathfinderGoalTargetPlayerUnbounded.class);

            HashSet<Class<?>> targets = new HashSet<>();

            Hack.injectCustomAi(MythicMobs.inst(), goals, targets);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
        SpawnpointCommands spCmd = new SpawnpointCommands();
        getCommand("gunDebug").setExecutor(new GunDebugCommands());
        getCommand("testentity").setExecutor(spCmd);
        getCommand("newspawnpoint").setExecutor(spCmd);
    }

    public ConfigFileManager getConfigManager() {
        return config;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public boolean removeGameManager(String id) {
        return gameManagers.remove(id) != null;
    }

    public ArrayList<GameManager> getGameManagers() {
        return new ArrayList<>(gameManagers.values());
    }

    public GameManager getGameManager(String id) {
        return gameManagers.getOrDefault(id, null);
    }

    public GameMap getMap(String mapName) { return maps.get(mapName); }

    public boolean addGameManager(String id, GameManager manager) {
        if(!gameManagers.containsKey(id)) {
            gameManagers.put(id, manager);
            return true;
        }
        return false;
    }

    public boolean addMap(GameMap map, String name) {
        if(!maps.containsKey(name)) {
            maps.put(name, map);
            return true;
        }
        return false;
    }
}
