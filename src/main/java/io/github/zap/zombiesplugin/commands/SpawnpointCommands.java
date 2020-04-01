package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.map.spawn.RandomizedSpawnManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
                    testGame.getMap().add(new RandomizedSpawnManager("test_spawner", testGame.getMap(), types));
                    map.getSpawnManagers().get(0).add(mob);
                }
                manager = map.getSpawnManagers().get(0);

                ArrayList<MythicMob> mobs = new ArrayList<>();
                mobs.add(mob);

                manager.spawn(testGame, mobs);
            }
            else if(command.getName().equals("newspawnpoint")){
                MythicMob mob =  MythicMobs.inst().getAPIHelper().getMythicMob("TestZombie");

                HashSet<MythicMob> types = new HashSet<>();
                types.add(mob);

                GameManager testGame = ZombiesPlugin.instance.getGameManager("test_game");
                testGame.getPlayerManager().addPlayer(player);
                System.out.println("Players: " + testGame.getPlayerManager().getPlayers().size());

                GameMap map = testGame.getMap();
                if(map.getWindows().size() == 0) {
                    map.add(new Window(new Location(player.getWorld(), -1, 3, -1), new Location(player.getWorld(), -1, 4, 1), Material.OAK_SLAB));
                }

                SpawnManager manager;
                if(map.getSpawnManagers().size() == 0) {
                    testGame.getMap().add(new RandomizedSpawnManager("test_spawner", testGame.getMap(), types));
                    map.getSpawnManagers().get(0).add(mob);
                }
                manager = map.getSpawnManagers().get(0);
                manager.add(new SpawnPoint(new Room(map), new Location(player.getWorld(), -6, 2, 3), new Location(player.getWorld(), 0, 1, 0)));
            }
        }
        return false;
    }
}