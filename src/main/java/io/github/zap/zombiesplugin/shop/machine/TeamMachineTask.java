package io.github.zap.zombiesplugin.shop.machine;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.events.EventHandler;
import io.github.zap.zombiesplugin.events.ValueRequestedEventArgs;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.data.TMTaskData;
import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public abstract class TeamMachineTask {
    protected final GameManager manager;
    protected final TMTaskData data;
    protected int boughtCount = 0;

    public TeamMachineTask(TMTaskData data, GameManager manager) {
        this.data = data;
        this.manager = manager;
    }

    public void tryPurchase(User executor) {
        if(executor != null) {
            int cost = getCost();
            if(executor.getGold() >= cost) {
                if (execTask(executor)) {
                    data.purchaseFx.play(Arrays.asList(executor.getPlayer()), 100, SoundCategory.BLOCKS, null);
                    executor.addGold(-cost);
                    boughtCount++;
                }
            } else {
                executor.getPlayer().sendMessage(ChatColor.RED + "You don't have enough Gold!");
            }
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
        meta.setDisplayName((isPurchasable ? ChatColor.GREEN  : ChatColor.RED) + data.name);

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

    protected String tryGetCustomValue (String name) {
        if (data.customData.containsKey(name)) {
            String originalValue = data.customData.get(name);
            ValueRequestedEventArgs e = new ValueRequestedEventArgs(originalValue, name);
            onValueRequested(e);
            return (String) e.modifiedValue;
        }else {
            // Log this error to the console and the player
            String msg = ChatColor.RED + "The requested value: " + name + " is not existed! If you are a server operator," +
                    "please make sure that the config file is configured correctly. Value defaulted to empty";
            ZombiesPlugin.instance.getLogger().log(Level.SEVERE, msg);

            return "";
        }
    }

    public final EventHandler<ValueRequestedEventArgs> valueRequested = new EventHandler<>();
    protected void onValueRequested(ValueRequestedEventArgs e) {
        valueRequested.invoke(this, e);
    }
}
