package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.github.zap.zombiesplugin.commands.GameCommands;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.player.GunUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class ZombiesPlugin extends JavaPlugin implements Listener {
    public static ZombiesPlugin instance;

    private ProtocolManager protocolManager;
    private ArrayList<GameManager> games;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().log(Level.ALL, "Hello Minecraft!");
        instance = this;

        manager = new PlayerManager(null);
        Bukkit.getPluginManager().registerEvents(this,this);
        getCommand("gunDebug").setExecutor(new GameCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PlayerManager manager ;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        manager.addPlayer(e.getPlayer());
        try {
            manager.getAssociatedUser(e.getPlayer()).getGunUser().equipGun("z");
        } catch (Exception ex) {
        }
    }

}
