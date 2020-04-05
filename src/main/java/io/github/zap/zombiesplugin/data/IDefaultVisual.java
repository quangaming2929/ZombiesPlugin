package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IDefaultVisual extends ICustomSerializerIdentity {
    ItemStack getDefaultVisual(String displayName, int level, ILeveling levels);
    Material getDisplayItem();
    ChatColor getDisplayColor();
    String[] getInstruction();

    void setDisplayItem(Material item);
    void setDisplayColor(ChatColor color);
    void setInstruction(String[] instruction);

    // Util methods
    String getDisplayNameWith(ChatColor color, String name, int level, ILeveling levels);
}
