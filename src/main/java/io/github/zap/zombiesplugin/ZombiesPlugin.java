package io.github.zap.zombiesplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.commands.SpawnpointCommands;
import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameSettings;
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
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    //these are needed for communication between pathfinders and the plugin
    public SpawnPoint lastSpawnpoint;
    public GameManager lastManager;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        gameManagers = new Hashtable<>();
        maps = new Hashtable<>();

        registerConfigs();
        registerCommands();

        gameManagers.put("test_game", new GameManager( "test_game", new GameSettings(GameDifficulty.NORMAL, new GameMap("test_map"), 4)));

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

        //creates client-side only barriers in windows
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.BLOCK_CHANGE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                StructureModifier<BlockPosition> structureModifier = event.getPacket().getBlockPositionModifier();
                BlockPosition pos = structureModifier.read(0);
                Player player = event.getPlayer();

                for(String key : gameManagers.keySet()) {
                    GameManager manager = gameManagers.get(key);
                    User user = manager.getPlayerManager().getAssociatedUser(player);
                    if(user != null) {
                        Location currentLocation = new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ());
                        Window window = manager.getMap().getWindowAt(currentLocation);
                        if(window != null) {
                            Material currentMaterial = currentLocation.getBlock().getType();
                            Material newMaterial = event.getPacket().getBlockData().read(0).getType();

                            if(currentMaterial == newMaterial && !window.getJustRepaired()) {
                                event.setCancelled(true);
                            }

                            window.setJustRepaired(false);
                        }
                    }
                }
            }
        });
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
