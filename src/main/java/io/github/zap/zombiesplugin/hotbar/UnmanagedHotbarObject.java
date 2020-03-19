package io.github.zap.zombiesplugin.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This class will manages normal items in hotbar. You don't need to create this instance,
 * call to addObject(ItemStack item,
 */
public class UnmanagedHotbarObject extends HotbarObject {
    private ItemStack itemToRep;

    public UnmanagedHotbarObject(ItemStack itemToReproduce) {
        this.itemToRep = itemToReproduce;
    }

    @Override
    public void setVisibility(boolean isVisible) {
        super.setVisibility(isVisible);
        ItemStack item = getSlot();

        if (isVisible) {
            item.setType(itemToRep.getType());
            item.setItemMeta(itemToRep.getItemMeta());
            item.setAmount(itemToRep.getAmount());
        } else {
            item.setAmount(0);
        }
    }

    public ItemStack getItemToRep() {
        return itemToRep;
    }

    public void setItemToRep(ItemStack itemToRep) {
        this.itemToRep = itemToRep;
        setVisibility(this.isVisible());
    }
}
