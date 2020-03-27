package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.provider.GunImporter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GunDebugCommands implements CommandExecutor {
    private final GunImporter gunImporter;

    public GunDebugCommands() {
        this.gunImporter = ZombiesPlugin.instance.getConfigManager().getImporter("Gun");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(command.getName().equals("gunDebug") && commandSender instanceof Player) {
            Player playerSender = (Player)commandSender;

            if (strings.length > 0) {
                User user = ZombiesPlugin.instance.manager.getAssociatedUser(playerSender);
                GunObjectGroup gunUser = user.getGunGroup();
                int pSlot = playerSender.getInventory().getHeldItemSlot();
                if (user != null) {
                    if (strings[0].equals("ult")) {
                        Gun targetedGun = (Gun) user.getHotbar().getSelectedObject();
                        if(targetedGun != null) {
                            targetedGun.upgrade();
                        } else {
                            playerSender.sendMessage(ChatColor.RED + "Error: please hold your gun to ultimate");
                        }
                    } else if (strings[0].equals("getGun") && strings.length > 1) {
                        try {

                            if(pSlot != -1) {
                                Gun gun = gunImporter.createGun(strings[1]);
                                if(gun != null) {
                                    if(gunUser.isEquipableAt(pSlot))
                                        gunUser.addObject(gun, pSlot);
                                    else
                                        playerSender.sendMessage(ChatColor.RED + "Place select a gun slot to equip");
                                } else {
                                    playerSender.sendMessage(ChatColor.RED + "Get the the current gun id: " + strings[1]);
                                }

                            }
                        } catch (Exception e) {
                            playerSender.sendMessage(ChatColor.RED + "Error occurred when trying to create gun");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return false;
    }
}
