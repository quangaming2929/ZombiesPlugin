package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static io.github.zap.zombiesplugin.utils.Constants.GUN_PACKAGES;

public class GunUser {
    private final Player player;

    private final List<Gun> guns;
    private final Hashtable<String, Integer> usedGun;

    // slot for gun
    private final List<Integer> preservedSlot;
    private int slotCount;

    public GunUser(Player player, int initialSlotCount, Integer... preservedSlot) {
        this.player = player;
        this.slotCount = initialSlotCount;
        this.usedGun = new Hashtable<String, Integer>();
        this.preservedSlot = new ArrayList<Integer>();
        Collections.addAll(this.preservedSlot, preservedSlot);
        guns = new ArrayList<Gun>();
    }

    public Gun getGunBySlot(int id) {
        if(id > 0 && id < guns.size()) {
            return guns.get(id);
        } else {
            return null;
        }
    }

    public int getSlotIDByGun(Gun gun) {
        return guns.indexOf(gun);
    }

    public Gun getGunByItemStack(ItemStack item) {
        for (Gun gun : guns) {
            if(item.getType().equals(gun.gunStats.displayItem))
                return gun;
        }

        return null;
    }

    public void equipGun(String gunName) throws Exception {
        if(guns.size() < getSlotCount()) {
            addGun(guns.size(), gunName);
        }
    }

    public void replaceGunSlot(int slot, String gunName) throws Exception {
        if (guns.size() < getSlotCount() - 1) {
            equipGun(gunName);
        }

        addGun(slot, gunName);
    }

    private void addGun(int slot, String gunName) throws Exception {
        if(slot > 0 && slot < getSlotCount()) {
            Gun gun = getGunByName(gunName);
            if (gun != null) {
                releaseOldGun(slot);

                // Get the slot for our new gun
                ItemStack item = player.getPlayer().getInventory().getItem(slot);
                // Check for the previous gun ultimate level
                Integer ultimateLevel = usedGun.get(gunName);
                if(ultimateLevel != null) {
                    gun.initGun(item, ultimateLevel);
                } else {
                    gun.initGun(item, 0);
                }
            } else {
                // TODO: Log error message
            }
        }
    }

    private void releaseOldGun(int slot) {
        // release old gun if any
        if(slot != guns.size()) {
            Gun oldGun =  guns.get(slot);
            if(oldGun != null) {
                oldGun.releaseGunSlot();
                if(usedGun.containsKey(oldGun.gunStats.name)) {
                    usedGun.replace(oldGun.gunStats.name, oldGun.getUltimateLevel());
                } else {
                    usedGun.put(oldGun.gunStats.name, oldGun.getUltimateLevel());
                }
            }
        }
    }

    private Gun getGunByName(String name) throws Exception {
        GunData data = Zombie.getInstance().getGunProvider().getGun(name);
        return (Gun)Class.forName(GUN_PACKAGES + data.feature)
                .getConstructor(GunData.class, User.class).newInstance(data, this);
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        if(slotCount > 0 && slotCount < preservedSlot.size()) {
            this.slotCount = slotCount;

            // remove gun to fit the player slot count
            for(int i = guns.size() - 1; i >= getSlotCount(); i--) {
                releaseOldGun(getSlotIDByGun(guns.get(i)));
                player.getInventory().setItem(preservedSlot.get(i), null);
            }

            // add gun place holder
            for(int i = guns.size(); i < getSlotCount(); i++) {
                ItemStack item =  new ItemStack(Material.LIGHT_GRAY_DYE, 1);
                ItemMeta meta =  item.getItemMeta();

                meta.setDisplayName(ChatColor.GOLD + "Gun #" + i);
                meta.setLore(Arrays.asList(WeaponStatsLoreBuilder.getGunPlaceHolderLore()));

                player.getInventory().setItem(preservedSlot.get(i), item);
            }

        } else {
            // TODO: Log error
        }
    }
}
