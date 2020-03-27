package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.spawn.OrderedSpawnManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitWorld;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class SpawnpointCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if(command.getName().equals("testentity")) {
                MythicMob mob =  MythicMobs.inst().getAPIHelper().getMythicMob("TestZombie");
                HashSet<MythicMob> types = new HashSet<>();
                types.add(mob);

                GameManager testGame = ZombiesPlugin.instance.getGameManager("test_game");
                GameMap map = testGame.getMap();

                SpawnManager manager;
                if(map.getSpawnManagers().size() == 0) {
                    testGame.getMap().add(new OrderedSpawnManager("test", testGame.getMap(), types));
                    map.getSpawnManagers().get(0).add(mob);
                }

                manager = map.getSpawnManagers().get(0);
                manager.add(new SpawnPoint(testGame, new Room(map), player.getLocation(), player.getLocation().add(5, 0, 0)));

                ArrayList<MythicMob> mobs = new ArrayList<>();
                mobs.add(mob);

                manager.spawn(testGame, mobs);
            }
        }
        return false;
    }
}