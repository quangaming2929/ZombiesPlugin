package io.github.zap.zombiesplugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GameCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

            ItemStack[] newArmorStandArmor = {new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET)};


        }

        return false;
    }
}
