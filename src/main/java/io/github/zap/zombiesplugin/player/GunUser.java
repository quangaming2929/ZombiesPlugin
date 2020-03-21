package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunPlaceHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Add slot parameter in this class refer to the slotCount like the first slot is the 2nd actual slot in the hotbar
 */
public class GunUser {
    public final  User user;

    private final List<Gun> guns;
    private final Hashtable<String, Integer> usedGun;

    // slot for gun
    private final List<Integer> preservedSlots;
    private int slotCount;

    public GunUser(User parent, int initialSlotCount, Integer... preservedSlots) {
        this.user = parent;
        this.slotCount = initialSlotCount;
        this.usedGun = new Hashtable<String, Integer>();
        this.preservedSlots = new ArrayList<Integer>();
        Collections.addAll(this.preservedSlots, preservedSlots);
        guns = new ArrayList<Gun>();

        setSlotCount(2);
    }

    public int getPreservedSlot(int actualSlot) {
        return preservedSlots.indexOf(actualSlot);
    }

    public int getActualSlot(int preservedSlot) {
        return preservedSlots.get(preservedSlot);
    }

    public Gun getGunBySlot(int id) {
        if(id >= 0 && id < guns.size()) {
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
            if(item.getType().equals(gun.gunData.displayItem))
                return gun;
        }

        return null;
    }

    public void equipGun(Gun gun) {
        if(guns.size() < getSlotCount()) {
            addGun(guns.size(), gun);
        }
    }

    public void replaceGunSlot(int slot, Gun gun) {
        if (guns.size() < getSlotCount() - 1) {
            equipGun(gun);
        }

        addGun(slot, gun);
    }

    private void addGun(int slot, Gun gun) {
        if(slot >= 0 && slot < getSlotCount()) {
            if (gun != null) {
                removeGun(slot);

                // Get the slot for our new gun
                user.getHotbar().addObject(gun, preservedSlots.get(slot));
                // Check for the previous gun ultimate level
                Integer ultimateLevel = usedGun.get(gun.gunData.name);
                if(ultimateLevel != null) {
                    gun.setLevel(ultimateLevel);
                } else {
                    gun.setLevel(0);
                }

                guns.add(gun);
            } else {
                // TODO: Log error message
            }
        }
    }

    private void removeGun(int slot) {
        // release old gun if any
        if(slot < guns.size()) {
            Gun oldGun =  guns.get(slot);
            if(oldGun != null) {
                if(usedGun.containsKey(oldGun.gunData.name)) {
                    usedGun.replace(oldGun.gunData.name, oldGun.getUltimateLevel());
                } else {
                    usedGun.put(oldGun.gunData.name, oldGun.getUltimateLevel());
                }

                guns.remove(oldGun);
            }
        }
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        if(slotCount > 0 && slotCount < preservedSlots.size()) {
            this.slotCount = slotCount;

            // remove gun to fit the player slot count
            for(int i = guns.size() - 1; i >= getSlotCount(); i--) {
                user.getHotbar().removeObject(guns.get(i));
                removeGun(getSlotIDByGun(guns.get(i)));
            }

            // add gun place holder
            for(int i = guns.size(); i < getSlotCount(); i++) {
                user.getHotbar().addObject(new GunPlaceHolder(i + 1), preservedSlots.get(i));
            }

        } else {
            // TODO: Log error -> the slot count count is larger than the preserved slots or < 0
        }
    }
}
