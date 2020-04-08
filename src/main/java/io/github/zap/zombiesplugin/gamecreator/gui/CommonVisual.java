package io.github.zap.zombiesplugin.gamecreator.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CommonVisual {
    public static String[] getRoomDetailLore (GameRoom room) {
        return new String[] {
                ChatColor.DARK_GRAY + "(Room id: " + room.getRoomID() + ")",
                "", // Blank line
                ChatColor.WHITE + "Map Details: ",
                ChatColor.GRAY + "Map name: " + ChatColor.GREEN + room.getMapName(),
                ChatColor.GRAY + "Mode/ Difficultly: " + ChatColor.GREEN + room.getSelectedDiff(),
                ChatColor.GRAY + "Public room: " + ChatColor.GREEN + room.isPublic()
        };
    }

    /**
     * Use to make glowing effect
     * @return
     */
    public static ItemStack glowItemStack (ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    // The api fillRect fills border of the rectangle z
    public static void fillRegion (int row, int column, int row2, int column2, ClickableItem item, InventoryContents contents) {
        for (int rowIndex = row; rowIndex < row2 + 1; rowIndex++) {
            for (int columnIndex = column; columnIndex < column2 + 1; columnIndex++) {
                contents.set(rowIndex, columnIndex, item);
            }
        }
    }

    public static ItemStack createVisualItem (Material displayItem, int amount, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(displayItem, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack previousPageVisual (int currentPage) {
        return getItemStack(currentPage - 1, "Previous page");
    }

    public static ItemStack nextPageVisual (int currentPage) {
        return getItemStack(currentPage + 1, "Next page");
    }


    public static ItemStack getItemStack(int page, String s) {
        ItemStack pre = new ItemStack(Material.ARROW, Math.min(page, 64));
        ItemMeta meta = pre.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + s);
        String[] lores = new String[] {
                ChatColor.GRAY + "Click to navigate to page " ,
                "" + ChatColor.GOLD + page
        };

        meta.setLore(Arrays.asList(lores));
        pre.setItemMeta(meta);

        return pre;
    }
}
