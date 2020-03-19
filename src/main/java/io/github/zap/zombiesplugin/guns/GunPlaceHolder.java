package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GunPlaceHolder extends HotbarObject {
    private int slotNo;

    public GunPlaceHolder(int slotNo) {
        this.slotNo = slotNo;
    }

    @Override
    public void init(int slot, Player player) {
        super.init(slot, player);

        ItemMeta meta = getSlot().getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Gun #" + slotNo);
        meta.setLore(Arrays.asList(WeaponStatsLoreBuilder.getGunPlaceHolderLore()));
        getSlot().setItemMeta(meta);
        getSlot().setType(Material.LIGHT_GRAY_DYE);
    }
}
