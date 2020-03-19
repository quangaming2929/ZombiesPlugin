package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GameCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(command.getName().equals("gunDebug") && commandSender instanceof Player) {
            Player playerSender = (Player)commandSender;

            if (strings.length > 0) {
                User user = ZombiesPlugin.instance.manager.getAssociatedUser(playerSender);
                if (user != null) {
                    if (strings[0].equals("ult")) {
                        ItemStack mainHandItem = playerSender.getInventory().getItemInMainHand();
                        Gun targetedGun = user.getGunUser().getGunByItemStack(mainHandItem);
                        if(targetedGun != null) {
                            targetedGun.ultimate();
                        } else {
                            playerSender.sendMessage(ChatColor.RED + "Error: please hold your gun to ultimate");
                        }

                    }
                }
            }
        }

        return false;
    }
}
