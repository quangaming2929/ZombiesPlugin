package io.github.zap.zombiesplugin.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.bodies.DeadBody;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.player.User;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GameCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        /*if(command.getName().equals("gunDebug") && commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

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
        }*/

        if (commandSender instanceof Player) {
            DeadBody deadBody = new DeadBody((Player) commandSender);
            deadBody.displayTo((Player) commandSender);
        }

        return false;
    }
}
