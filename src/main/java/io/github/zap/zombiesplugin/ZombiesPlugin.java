package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.commands.GunDebugCommands;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.GunImporter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public ConfigFileManager getConfigManager() {
        return config;
    }

    public PlayerManager manager ;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // manager.addPlayer(e.getPlayer());
    }

}
