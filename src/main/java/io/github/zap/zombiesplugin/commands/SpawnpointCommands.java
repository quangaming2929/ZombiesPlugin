package io.github.zap.zombiesplugin.commands;

import com.comphenix.protocol.PacketType;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameSettings;
import io.github.zap.zombiesplugin.map.Door;
import io.github.zap.zombiesplugin.map.Map;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.spawn.RandomizedSpawnPointManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.map.spawn.SpawnPointManager;
import io.github.zap.zombiesplugin.mob.Mob;
import io.github.zap.zombiesplugin.mob.Mobs;
import io.github.zap.zombiesplugin.mob.TestZombie;
import io.github.zap.zombiesplugin.shop.Shop;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.Location;
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

            if(command.getName().equals("gamemanager")) {
                if(args.length == 1 && args[0].equals("list"))  {
                    if(ZombiesPlugin.instance.getManagers().size() > 0) {
                        for(GameManager manager : ZombiesPlugin.instance.getManagers()) {
                            player.sendMessage(manager.id);
                        }
                    }
                    else player.sendMessage("There are no GameManagers currently.");
                    return true;
                }
                else if(args.length == 2 && args[0].equals("add")) {
                    if(ZombiesPlugin.instance.getManager(args[1]) == null) {
                        GameManager testManager = new GameManager(args[1]);
                        Map map = new Map(testManager);
                        testManager.build(new GameSettings(), map);

                        ZombiesPlugin.instance.addManager(args[1], testManager);
                    }
                    else {
                        player.sendMessage("A GameManager with that ID already exists.");
                    }
                    return true;
                }
            }
            else if(command.getName().equals("spawnmanager")) {
                if(args.length == 2) {
                    if(args[0].equals("add")) {
                        GameManager manager = ZombiesPlugin.instance.getManager(args[1]);
                        if(manager != null) {
                            RandomizedSpawnPointManager spawnManager = new RandomizedSpawnPointManager(manager, new HashSet<>());
                            spawnManager.add(Mobs.TEST_ZOMBIE);
                            manager.getMap().add(spawnManager);
                            return true;
                        }
                        player.sendMessage("There is no GameManager with that ID.");
                        return true;
                    }
                    else if(args[0].equals("addpoint")) {
                        GameManager manager = ZombiesPlugin.instance.getManager(args[1]);
                        if(manager != null) {
                            manager.getMap().getSpawnPointManagers().get(0).add(new SpawnPoint(manager, player.getLocation()));
                            return true;
                        }
                        player.sendMessage("There is no GameManager with that ID.");
                        return true;
                    }
                }
                return false;
            }
            else if(command.getName().equals("spawn")) {
                if(args.length == 2) {
                    GameManager manager = ZombiesPlugin.instance.getManager(args[0]);
                    if(manager != null) {
                        int count = MathUtils.tryParseInt(args[1], -1);
                        if(count != -1) {
                            ArrayList<Mob> mobs = new ArrayList<>();
                            for(int i = 0; i < count; i++) {
                                mobs.add(new TestZombie());
                            }
                            manager.getMap().getSpawnPointManagers().get(0).spawn(mobs);
                            player.sendMessage("Spawned " + count + " zombies.");
                            return true;
                        }
                        return false;
                    }
                    player.sendMessage("There is no GameManager with that ID.");
                    return true;
                }
            }
        }
        return false;
    }
}