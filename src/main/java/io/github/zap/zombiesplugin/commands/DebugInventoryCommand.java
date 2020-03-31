package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.TeamMachine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        Integer requestedSlot = Integer.parseInt(strings[0]);
        User user = ZombiesPlugin.instance.manager.getAssociatedUser((Player)commandSender);
        TeamMachine.access(user, requestedSlot);

        return false;
    }
}
