package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GunPlaceHolder implements HotbarObject {
    private ItemStack slot;
    private int slotNo;

    public GunPlaceHolder(int slotNo) {
        this.slotNo = slotNo;
    }

    @Override
    public ItemStack getSlot() {
        return slot;
    }

    @Override
    public void update() {

    }

    @Override
    public void onRemoved() {
        slot = null;
    }

    @Override
    public void setVisibility(boolean isVisible) {

    }

    @Override
    public void init(ItemStack slot, Player player) {
        if(slot != null) {
            slot.setType(Material.WHITE_DYE);
            ItemMeta meta =  slot.getItemMeta();

            meta.setDisplayName(ChatColor.GOLD + "Gun #" + slotNo);
            meta.setLore(Arrays.asList(WeaponStatsLoreBuilder.getGunPlaceHolderLore()));
            slot.setItemMeta(meta);
            slot.setType(Material.LIGHT_GRAY_DYE);
        }
    }

    @Override
    public boolean onThrow() {
        return false;
    }
}
