package io.github.zap.zombiesplugin.commands.mapeditor;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapEditorCommands implements CommandExecutor, Listener {
    private Hashtable<UUID, ContextManager> sessions;

    public MapEditorCommands() {
        sessions = new Hashtable<>();
        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.isOp()) {
            Player player = (Player)sender;
            UUID uuid = player.getUniqueId();
            if(!sessions.containsKey(uuid)) {
                sessions.put(uuid, new ContextManager(player));
            }

            ContextManager context = sessions.get(uuid);
            String commandName = command.getName().toLowerCase();
            player.sendMessage(context.performCommand(commandName, args));
        }
        return true;
    }

    private void giveItem(Player target, String lore) {
        ItemStack tool = new ItemStack(Material.STICK);

        ArrayList<String> list = new ArrayList<>();
        list.add(lore);
        tool.setLore(list);
        tool.addEnchantment(Enchantment.KNOCKBACK, 1);

        target.getInventory().setItemInMainHand(tool);
    }
}