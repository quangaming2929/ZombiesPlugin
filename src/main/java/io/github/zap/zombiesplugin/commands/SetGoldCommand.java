package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGoldCommand implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        int amount = Integer.parseInt(strings[0]);
        ZombiesPlugin.instance.manager.getAssociatedUser((Player) commandSender).setGold(amount);
        return false;
    }
}
