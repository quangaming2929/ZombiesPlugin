package io.github.zap.zombiesplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.commands.SpawnpointCommands;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameSettings;
import io.github.zap.zombiesplugin.manager.TickManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.GameMapImporter;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.map.spawn.SpawnManagerImporter;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalEscapeWindow;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalTargetPlayerUnbounded;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalUnboundedMeleeAttack;
import io.github.zap.zombiesplugin.pathfind.reflect.Hack;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.GunImporter;
import io.github.zap.zombiesplugin.utils.Tuple;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public final class ZombiesPlugin extends JavaPlugin implements Listener {
    public static ZombiesPlugin instance;

    private ConfigFileManager config;
    private ProtocolManager protocolManager;

    private Hashtable<String, GameManager> gameManagers;
    private Hashtable<String, GameMap> maps;

    private TickManager tickManager;

    //these are needed for communication between pathfinders and the plugin
    public SpawnPoint lastSpawnpoint;
    public GameManager lastManager;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        gameManagers = new Hashtable<>();
        maps = new Hashtable<>();
        tickManager = new TickManager(2); //runs at 10 TPS
        getServer().getPluginManager().registerEvents(this, this);

        registerConfigs();
        registerCommands();

        //TODO: remove this after testing
        gameManagers.put("test_game", new GameManager( "test_game", new GameSettings(GameDifficulty.NORMAL, new GameMap("test_map"), 4)));

        //inject custom AI pathfinders into MythicMobs
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

        tickManager.start();
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

    public TickManager getTickManager() { return tickManager; }
}
