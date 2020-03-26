package io.github.zap.zombiesplugin.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IDefaultVisual {
    ItemStack getDefaultVisual(String displayName, int level, ILeveling levels);
    Material getDisplayItem();
    ChatColor getDisplayColor();
    String[] getInstruction();
}
