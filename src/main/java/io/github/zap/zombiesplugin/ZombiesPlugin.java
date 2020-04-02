package io.github.zap.zombiesplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
import io.github.zap.zombiesplugin.utils.TabDecorator;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        sayHelloToTester(e.getPlayer());

        String header = ChatColor.AQUA + "Welcome to " +  ChatColor.YELLOW + ChatColor.BOLD +  "ZAP " + ChatColor.GOLD +  ChatColor.BOLD + "closed beta test 1";
        String footer = ChatColor.YELLOW + "To improve the game experience, \n" +
                "please report bug on our\n" + ChatColor.GOLD + ChatColor.BOLD + "github issues.\n\n" +
                ChatColor.GRAY + "Test version. Not indicative of the final product";
        TabDecorator.sendTabTitle(e.getPlayer(), header, footer);
    }

    private void sayHelloToTester(Player player) {
        JsonObject helloMsg = new JsonObject();
        JsonArray extra = new JsonArray();

        helloMsg.add("text", new JsonPrimitive("=== Welcome to Zombies Amendment Project (ZAP) ===\n"));
        helloMsg.add("color", new JsonPrimitive("gold"));
        helloMsg.add("extra", extra);

        StringBuilder sb = new StringBuilder();
        sb.append("Hi, Thank you for joining our closed beta test. This\n");
        sb.append("project is still under heavy development but it's in\n");
        sb.append("playable state. If you have any idea for feature\n");
        sb.append("or found a bug, please submit it on our ");

        JsonObject msgPart1 = new JsonObject();
        msgPart1.add("text", new JsonPrimitive(sb.toString()));
        msgPart1.add("color", new JsonPrimitive("green"));
        extra.add(msgPart1);
        sb.setLength(0);

        JsonObject githubClickable = new JsonObject();
        githubClickable.add("action", new JsonPrimitive("open_url"));
        githubClickable.add("value", new JsonPrimitive("https://github.com/John-DND/ZombiesPlugin/issues/new/choose"));

        JsonObject githubHover = new JsonObject();
        sb.append(ChatColor.GOLD + "" + ChatColor.BOLD + "Github Issue Tracker\n\n");
        sb.append(ChatColor.GRAY + "Click here to navigate to our github issue tracker page.");
        githubHover.add("action", new JsonPrimitive("show_text"));
        githubHover.add("value", new JsonPrimitive(sb.toString()));
        sb.setLength(0);

        JsonObject githubText = new JsonObject();
        githubText.add("text", new JsonPrimitive("github\nissue tracker"));
        githubText.add("bold", new JsonPrimitive(true));
        githubText.add("color", new JsonPrimitive("green"));
        githubText.add("clickEvent", githubClickable);
        githubText.add("hoverEvent", githubHover);
        extra.add(githubText);

        JsonObject msgPart2 = new JsonObject();
        msgPart2.add("text", new JsonPrimitive(". Don't forget to join our "));
        msgPart2.add("color", new JsonPrimitive("green"));
        extra.add(msgPart2);

        JsonObject discordClickable = new JsonObject();
        discordClickable.add("action", new JsonPrimitive("open_url"));
        discordClickable.add("value", new JsonPrimitive("https://discord.gg/r63e9c6"));

        JsonObject discordHover = new JsonObject();
        sb.append(ChatColor.GOLD + "" + ChatColor.BOLD + "Discord Invite Link\n\n");
        sb.append(ChatColor.GRAY + "Click here to join our discord invite");
        discordHover.add("action", new JsonPrimitive("show_text"));
        discordHover.add("value", new JsonPrimitive(sb.toString()));
        sb.setLength(0);

        JsonObject discordText = new JsonObject();
        discordText.add("text", new JsonPrimitive("discord\n"));
        discordText.add("bold", new JsonPrimitive(true));
        discordText.add("color", new JsonPrimitive("green"));
        discordText.add("clickEvent", discordClickable);
        discordText.add("hoverEvent", discordHover);
        extra.add(discordText);

        JsonObject msgPart3 = new JsonObject();
        msgPart3.add("text", new JsonPrimitive(" for future updates. Enjoy your time in ZAP"));
        msgPart3.add("color", new JsonPrimitive("green"));
        extra.add(msgPart3);

        JsonObject footer = new JsonObject();
        footer.add("text", new JsonPrimitive(ChatColor.GOLD + "=============================================\n"));
        footer.add("color", new JsonPrimitive("gold"));
        extra.add(footer);

        PacketPlayOutChat chatPacket = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(helloMsg));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(chatPacket);
    }

}
