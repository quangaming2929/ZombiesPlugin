package io.github.zap.zombiesplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnpointCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if(command.getName().equals("testentity")) {

            }
            else if(command.getName().equals("testspawnpoint")) {

            }
        }
        return false;
    }
}