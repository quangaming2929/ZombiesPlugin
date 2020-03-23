package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.provider.GunImporter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
                User user = ZombiesPlugin.instance.playerManager.getAssociatedUser(playerSender);
                if (user != null) {
                    if (strings[0].equals("ult")) {
                        ItemStack mainHandItem = playerSender.getInventory().getItemInMainHand();
                        Gun targetedGun = user.getGunUser().getGunByItemStack(mainHandItem);
                        if(targetedGun != null) {
                            targetedGun.ultimate();
                        } else {
                            playerSender.sendMessage(ChatColor.RED + "Error: please hold your gun to ultimate");
                        }
                    } else if (strings[0].equals("getGun") && strings.length > 1) {
                        try {
                            int pSlot = user.getGunUser().getPreservedSlot(playerSender.getInventory().getHeldItemSlot());
                            if(pSlot != -1) {
                                Gun gun = gunImporter.createGun(strings[1], user.getGunUser());
                                if(gun != null) {
                                    user.getGunUser().replaceGunSlot(pSlot, gun);
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
