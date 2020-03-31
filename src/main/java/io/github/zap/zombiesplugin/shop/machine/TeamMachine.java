package io.github.zap.zombiesplugin.shop.machine;

import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMachine {

    public TeamMachine() {


    }

    static int last = 0;

    public static void access(User user, int num) {
        last = num;
        user.getPlayer().openInventory(prepareInv(num));
    }

    public static void next(User user) {
        last ++;
        access(user, last);
    }

    public static void prev(User user) {
        last--;
        access(user, last);
    }

    private static Inventory prepareInv(int num) {
        int width = (int) Math.ceil(Math.sqrt(num));
        int height = (int) Math.ceil(num / (float)width);
        int remainderLine = (int) Math.floor(Math.min(6, height) / 2);
        // this is the first line offset
        int offset = height + 2 <= 6 ? 1 : 0;
        // If the height go higher than 6 we need to change our calculation
        if(height > 6) {
            width = (int) Math.ceil(num / (float)6);
        }
        int finalLine = num % width;
        if (finalLine == 0) {
            finalLine = width;
        }


        Inventory gui = Bukkit.createInventory(null, 9 * Math.min(6, height + 2), "Team Machine (" + num + ")" );
        int i = 1;

        for (int h = 0; h < height; h++) {
            int lineCount = h == remainderLine ? finalLine : width;
            for (int w = 0; w < lineCount; w++) {
                if (i > num) {
                    return gui;
                }
                // If we are at the last line

                int slotID = alignItem(9, lineCount, w + 1);
                try{gui.setItem((h + offset) * 9 + slotID, createItem(i)); }
                catch (Exception e){}

                i++;
            }
        }

        // get not gonna go here anyway
        return gui;
    }

    private static ItemStack createItem (int index) {
        ItemStack is = new ItemStack(Material.BARRIER, index);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "This is test slot #" + index);
        is.setItemMeta(meta);

        return is;
    }

    /**
     * This method get the location of an item so that all items in a line will have an even spacing
     * Well Idk is there a better solution, but I gonna use my own algo.
     */
    private static int alignItem (float width, float count, float index) {
        return (int)Math.floor((2 * width * index - width) / (2 * count));
    }
}
