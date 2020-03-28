package io.github.zap.zombiesplugin.commands;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.Equipment;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.hotbar.ObjectGroup;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.provider.EquipmentImporter;
import io.github.zap.zombiesplugin.provider.Importer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EquipmentDebugCommands implements CommandExecutor {
    private final EquipmentImporter gunImporter;
    private final EquipmentImporter perkImporter;
    private final EquipmentImporter meleeImporter;

    public EquipmentDebugCommands() {
        this.gunImporter = ZombiesPlugin.instance.getConfigManager().getImporter("Gun");
        this.perkImporter = ZombiesPlugin.instance.getConfigManager().getImporter("Perk");
        this.meleeImporter = ZombiesPlugin.instance.getConfigManager().getImporter("Melee");
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
                    getEquipment(user, strings[1]);
                }
            }
        }
        return false;
    }

    private void getEquipment(User user, String id) {
        try {
            int activeSlot = user.getPlayer().getInventory().getHeldItemSlot();
            ObjectGroup activeGroup = user.getHotbar().overlap(activeSlot);
            if (activeGroup != null) {
                activeGroup.addObject(createEquipment(id), activeSlot);
            }
        } catch (Exception e) {
            user.getPlayer().sendMessage(ChatColor.RED + "Error occurred while creating equipment. See stack trace for more info! ");
            e.printStackTrace();
        }
    }

    private void ultimateEquipment(User sender) {
        HotbarObject obj = sender.getHotbar().getSelectedObject();
        if (obj instanceof UpgradeableEquipment) {
            ((UpgradeableEquipment) obj).upgrade();
        }
    }

    /**
     * Util method to query all equipments by id. DO NOT use this method / this command in production life cycle
     * @param id
     */
    private Equipment createEquipment(String id) throws Exception {
        for (Map.Entry<String, EquipmentData> item : gunImporter.getGunDatas()) {
            if (item.getKey().equals(id)) {
                return gunImporter.createEquipment(id);
            }
        }
        for (Map.Entry<String, EquipmentData> item : perkImporter.getGunDatas()) {
            if (item.getKey().equals(id)) {
                return perkImporter.createEquipment(id);
            }
        }
        for (Map.Entry<String, EquipmentData> item : meleeImporter.getGunDatas()) {
            if (item.getKey().equals(id)) {
                return meleeImporter.createEquipment(id);
            }
        }

        return null;
    }
}
