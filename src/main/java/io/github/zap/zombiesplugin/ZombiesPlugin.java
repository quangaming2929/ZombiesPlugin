package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.zap.zombiesplugin.commands.DebugInventoryCommand;
import io.github.zap.zombiesplugin.commands.EquipmentDebugCommands;
import io.github.zap.zombiesplugin.commands.ScoreboardDebugCommand;
import io.github.zap.zombiesplugin.commands.SetGoldCommand;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.TMTaskImporter;
import io.github.zap.zombiesplugin.scoreboard.InGameScoreBoard;
import io.github.zap.zombiesplugin.shop.machine.TeamMachine;
import io.github.zap.zombiesplugin.provider.equipments.GunImporter;
import io.github.zap.zombiesplugin.provider.equipments.MeleeImporter;
import io.github.zap.zombiesplugin.provider.equipments.PerkImporter;
import io.github.zap.zombiesplugin.provider.equipments.SkillImporter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;

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

        // Equipments
        config.addImporter("Melee", new MeleeImporter());
        config.addImporter("Gun", new GunImporter());
        config.addImporter("Skill", new SkillImporter());
        config.addImporter("Perk", new PerkImporter());

        // Team Machine
        config.addImporter("TMTasks", new TMTaskImporter());


        config.reload();

        try {
            gm = new GameManager(null, null);
            gm.setScoreboard(new InGameScoreBoard(gm));
            manager = gm.getPlayerManager();
            TMTaskImporter tmTasks = (TMTaskImporter) config.getImporter("TMTasks");
            tm = new TeamMachine(gm, Arrays.asList(tmTasks.createTask("tmt_ammo_supply", gm),  tmTasks.createTask("tmt_full_revive", gm), tmTasks.createTask("tmt_dragon_wrath", gm)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(this,this);

        getCommand("equipmentDebug").setExecutor(new EquipmentDebugCommands());
        getCommand("invDebug").setExecutor(new DebugInventoryCommand());
        getCommand("setGold").setExecutor(new SetGoldCommand());
        getCommand("scoreBoardDebug").setExecutor(new ScoreboardDebugCommand());


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

    // Test fields
    public GameManager gm;
    public PlayerManager manager;
    public TeamMachine tm;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        manager.addPlayer(e.getPlayer());


    }

}
