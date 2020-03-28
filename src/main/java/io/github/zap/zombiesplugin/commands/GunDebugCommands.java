package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunObjectGroup;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.perks.Perk;
import io.github.zap.zombiesplugin.perks.PerkObjectGroup;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.provider.GunImporter;
import io.github.zap.zombiesplugin.provider.PerkImporter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GunDebugCommands implements CommandExecutor {
    private final GunImporter gunImporter;
    private final PerkImporter perkImporter;

    public GunDebugCommands() {
        this.gunImporter = ZombiesPlugin.instance.getConfigManager().getImporter("Gun");
        this.perkImporter = ZombiesPlugin.instance.getConfigManager().getImporter("Perk");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;
            User user = ZombiesPlugin.instance.manager.getAssociatedUser(playerSender);

            if (user != null) {
                if (strings[0].equals("ult")) {
                    ultimateEquipment(user);
                } else if (strings[0].equals("get") && strings.length > 1) {

                }
            }
        }

       /* if(command.getName().equals("gunDebug") && commandSender instanceof Player) {
            Player playerSender = (Player)commandSender;

            if (strings.length > 0) {
                User user = ZombiesPlugin.instance.manager.getAssociatedUser(playerSender);
                GunObjectGroup gunUser = user.getGunGroup();
                int pSlot = playerSender.getInventory().getHeldItemSlot();
                if (user != null) {

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
                    } else if (strings[0].equals("getPerks") && strings.length > 1) {
                        try {
                            if (pSlot != -1) {
                                PerkObjectGroup perkUser = user.getPerkGroup();
                                Perk perk = perkImporter.getPerk(strings[1]);
                                if (perk != null) {
                                    if (perkUser.isEquipableAt(pSlot))
                                        perkUser.addObject(perk, pSlot);
                                    else
                                        playerSender.sendMessage(ChatColor.RED + "Place select a perk slot to equip");
                                } else {
                                    playerSender.sendMessage(ChatColor.RED + "Get the the current perk id: " + strings[1]);
                                }

                            }
                        }catch (Exception e){e.printStackTrace();}
                    }
                }
            }
        }*/

        return false;
    }

    private void ultimateEquipment(User sender) {
        HotbarObject obj = sender.getHotbar().getSelectedObject();
        if (obj instanceof UpgradeableEquipment) {
            ((UpgradeableEquipment) obj).upgrade();
        }
    }
}
