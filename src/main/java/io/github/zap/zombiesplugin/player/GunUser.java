package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunPlaceHolder;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

/**
 * Add slot parameter in this class refer to the slotCount like the first slot is the 2nd actual slot in the hotbar
 */
public class GunUser {
    public final  User user;

    private final Hashtable<String, Integer> usedGun;
    private final GunUserSlot[] gunContainer;
    // slot for gun
    private int slotCount;

    public GunUser(User parent, int initialSlotCount, Integer... preservedSlots) {
        this.user = parent;
        this.slotCount = initialSlotCount;
        this.usedGun = new Hashtable<String, Integer>();

        this.gunContainer = new GunUserSlot[preservedSlots.length];
        for (int i = 0; i < preservedSlots.length; i++) {
            this.gunContainer[i] = new GunUserSlot(preservedSlots[i]);
        }

        setSlotCount(2);
    }

    public int getPreservedSlot(int actualSlot) {
        return preservedSlots.indexOf(actualSlot);
    }

    public int getActualSlot(int preservedSlot) {
        return gunContainer
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

    public void equipGun(Gun gun) {
        if(guns.size() < getSlotCount()) {
            addGun(guns.size(), gun);
        }
    }

    public void replaceGunSlot(int slot, Gun gun) {
        if (guns.size() < getSlotCount()) {
            equipGun(gun);
        } else {
            addGun(slot, gun);
        }

    }

    private void addGun(int slot, Gun gun) {
        if(slot >= 0 && slot < getSlotCount()) {
            if (gun != null) {
                removeGun(slot, false);

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
                ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't add the current gun, player name: ", user.getPlayer().getDisplayName());
            }
        }
    }

    public void removeGun(int slot, boolean usePlaceHolder) {
        // release old gun if any
        if(slot >= 0 && slot < guns.size()) {
            Gun oldGun = getGunBySlot(slot);
            if(oldGun != null) {
                if(usedGun.containsKey(oldGun.gunData.name)) {
                    usedGun.replace(oldGun.gunData.name, oldGun.getUltimateLevel());
                } else {
                    usedGun.put(oldGun.gunData.name, oldGun.getUltimateLevel());
                }

                guns.remove(oldGun);
                user.getHotbar().removeObject(oldGun);
                if(usePlaceHolder) {
                    user.getHotbar().addObject(new GunPlaceHolder(slot), slot);
                }
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
                removeGun(getSlotIDByGun(guns.get(i)), false);
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
