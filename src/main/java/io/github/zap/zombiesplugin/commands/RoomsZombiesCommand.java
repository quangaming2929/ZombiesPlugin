package io.github.zap.zombiesplugin.commands;

import fr.minuskube.inv.SmartInventory;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.gamecreator.ZombiesRoomManager;
import io.github.zap.zombiesplugin.gamecreator.gui.GuiRoomList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RoomsZombiesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            ZombiesPlugin.instance.getRoomManager().openGUI((Player)commandSender);
        }


        return false;
    }
}
