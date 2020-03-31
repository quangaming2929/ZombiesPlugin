package io.github.zap.zombiesplugin.shop.machine;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.data.ICost;
import io.github.zap.zombiesplugin.shop.machine.data.TMTaskData;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class TeamMachineTask {
    protected final GameManager manager;
    protected final TMTaskData data;
    protected int boughtCount = 0;

    public TeamMachineTask(TMTaskData data, GameManager manager) {
        this.data = data;
        this.manager = manager;
    }

    public void tryPurchase(User executor) {
        int cost = getCost();
        if(executor.getGold() >= cost) {
            if (execTask(executor)) {
                executor.addGold(-cost);
            }
        } else {
            executor.getPlayer().sendMessage(ChatColor.RED + "You don't have enough Gold!");
        }
    }

    public abstract boolean execTask(User user);

    public int getCost() {
        return data.cost.getCost(manager, boughtCount);
    }



    public ItemStack getVisual(User user) {
        boolean isPurchasable = user.getGold() > getCost();
        ItemStack item = new ItemStack(data.displayItem, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName((isPurchasable ? ChatColor.GREEN  : ChatColor.RED) + data.displayName);

        List<String> lore = new ArrayList<>();
        for (String line : data.description) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add("");
        lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + getCost() + " Gold");
        lore.add("");
        String actionLore = isPurchasable ?
                "" + ChatColor.YELLOW + "LEFT_CLICK " + ChatColor.GREEN + "to buy!" :
                "" + ChatColor.RED + "You don't have enough Gold!";
        lore.add(actionLore);

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
