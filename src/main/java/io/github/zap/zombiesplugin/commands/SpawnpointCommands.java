package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.*;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class SpawnpointCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if(command.getName().equals("testentity")) {
                World world = player.getWorld();
                GameManager game = ZombiesPlugin.instance.getGameManager("test_game");
                GameMap map = game.getSettings().getGameMap();
                ArrayList<SpawnFilter> filters = map.getSpawnFilters();
                ArrayList<Room> rooms = map.getRooms();

                if(filters.size() > 0) {
                    ArrayList<MythicMob> mobs = new ArrayList<>();
                    mobs.add(MythicMobs.inst().getAPIHelper().getMythicMob("TestZombie"));
                    mobs.add(MythicMobs.inst().getAPIHelper().getMythicMob("TestZombie"));

                    for (SpawnFilter spawnFilter : map.getSpawnFilters()) {
                        ArrayList<SpawnPoint> available = new ArrayList<>();
                        for(Room room : map.getRooms()) {
                            if(room.isOpen()) {
                                available.addAll(room.getSpawnPoints());
                                room.getWindows().forEach(window -> available.add(window.getSpawnPoint()));
                            }
                        }

                        spawnFilter.spawn(game, mobs, available);
                    }
                }
                return true;
            }
            else if(command.getName().equals("testspawnpoint")) {
                World world = player.getWorld();
                GameManager game = ZombiesPlugin.instance.getGameManager("test_game");
                GameMap map = game.getSettings().getGameMap();
                ArrayList<SpawnFilter> filters = map.getSpawnFilters();

                if(filters.size() == 0) {
                    HashSet<MythicMob> allowedMobs = new HashSet<>();
                    allowedMobs.add(MythicMobs.inst().getAPIHelper().getMythicMob("TestZombie"));
                    filters.add(new SpawnFilter(allowedMobs));
                }

                if(map.getRooms().size() == 0) {
                    map.add(new Room("test_room"));
                }

                Room room = map.getRoom("test_room");
                room.open();

                if(room.getWindows().size() == 0) {
                    BoundingBox windowBounds = new BoundingBox(new Location(world, -1, 3, -1), new Location(world, -1, 4, 1));

                    MultiBoundingBox interiorBounds = new MultiBoundingBox();
                    interiorBounds.add(new BoundingBox(new Location(world, -1, 2, -1), new Location(world, -7, 4, 1)));
                    interiorBounds.add(new BoundingBox(new Location(world, -5, 2, 2), new Location(world, -7, 4, 4)));
                    interiorBounds.getBounds().forEach(boundingBox -> boundingBox.expand(0.3));

                    SpawnPoint spawnPoint = new SpawnPoint(new Location(world, -6, 2, 3), new Location(world, 0, 1, 0));

                    room.add(new Window(windowBounds, interiorBounds, spawnPoint, Material.OAK_SLAB));

                    BoundingBox windowBounds1 = new BoundingBox(new Location(world, 4, 3, 10), new Location(world, 6, 4, 10));

                    MultiBoundingBox interiorBounds1 = new MultiBoundingBox();
                    interiorBounds1.add(new BoundingBox(new Location(world, 4, 2, 10), new Location(world, 6, 4, 19)));
                    interiorBounds1.getBounds().forEach(boundingBox -> boundingBox.expand(0.3));

                    SpawnPoint spawnPoint1 = new SpawnPoint(new Location(world, 5, 2, 18), new Location(world, 5, 1, 9));

                    room.add(new Window(windowBounds1, interiorBounds1, spawnPoint1, Material.OAK_SLAB));
                }

                game.getUserManager().addUser(player);
                return true;
            }
        }
        return false;
    }
}