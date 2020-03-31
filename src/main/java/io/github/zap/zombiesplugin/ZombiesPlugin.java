package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.commands.DebugInventoryCommand;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.GunImporter;
import io.github.zap.zombiesplugin.shop.machine.TeamMachine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class ZombiesPlugin extends JavaPlugin implements Listener {
    public static ZombiesPlugin instance;

    private ConfigFileManager config;
    private ProtocolManager protocolManager;
    private ArrayList<GameManager> games;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();


        config = new ConfigFileManager(this, this.getDataFolder());
        config.addImporter("Gun", new GunImporter());
        config.reload();

        manager = new PlayerManager(null);
        Bukkit.getPluginManager().registerEvents(this,this);

        getCommand("gunDebug").setExecutor(new GunDebugCommands());
        getCommand("invDebug").setExecutor(new DebugInventoryCommand());
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

    public PlayerManager manager ;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        manager.addPlayer(e.getPlayer());
    }

}
