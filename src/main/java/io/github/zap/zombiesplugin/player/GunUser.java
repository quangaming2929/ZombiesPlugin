package io.github.zap.zombiesplugin.player;

import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunPlaceHolder;
import io.github.zap.zombiesplugin.guns.LinearGun;
import io.github.zap.zombiesplugin.guns.data.BulletStats;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.gunattributes.LinearGunAttribute;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GunUser {
    public final  User user;

    private final List<Gun> guns;
    private final Hashtable<String, Integer> usedGun;

    // slot for gun
    private final List<Integer> preservedSlot;
    private int slotCount;

    public GunUser(User parent, int initialSlotCount, Integer... preservedSlot) {
        this.user = parent;
        this.slotCount = initialSlotCount;
        this.usedGun = new Hashtable<String, Integer>();
        this.preservedSlot = new ArrayList<Integer>();
        Collections.addAll(this.preservedSlot, preservedSlot);
        guns = new ArrayList<Gun>();

        setSlotCount(2);
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
            if(item.getType().equals(gun.gunStats.displayItem))
                return gun;
        }

        return null;
    }

    public void equipGun(String gunName) throws Exception {
        if(guns.size() < getSlotCount()) {
            addGun(preservedSlot.get(guns.size()), gunName);
        }
    }

    public void replaceGunSlot(int slot, String gunName) throws Exception {
        if (guns.size() < getSlotCount() - 1) {
            equipGun(gunName);
        }

        addGun(slot, gunName);
    }

    private void addGun(int slot, String gunName) throws Exception {
        if(slot >= 0 && slot < getSlotCount()) {
            Gun gun = getGunByName(gunName);
            if (gun != null) {
                saveOldGunValue(slot);

                // Get the slot for our new gun
                user.getHotbar().addObject(gun, slot);
                // Check for the previous gun ultimate level
                Integer ultimateLevel = usedGun.get(gunName);
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

    private void saveOldGunValue(int slot) {
        // release old gun if any
        if(slot < guns.size()) {
            Gun oldGun =  guns.get(slot);
            if(oldGun != null) {
                if(usedGun.containsKey(oldGun.gunStats.name)) {
                    usedGun.replace(oldGun.gunStats.name, oldGun.getUltimateLevel());
                } else {
                    usedGun.put(oldGun.gunStats.name, oldGun.getUltimateLevel());
                }
            }
        }
    }


    // Test code
    private Gun getGunByName(String name) throws Exception {
        GunData data = new GunData();
        data.description = new String[] { "TestGun" , "Test object will be remove later" };
        data.displayItem = Material.IRON_HOE;
        data.name = "Development gun";
        data.rewardGold = 10;
        // stats
        data.stats = new ArrayList<BulletStats>();
        for (int i = 0; i < 4; i++) {
            BulletStats s = new BulletStats();
            s.baseAmmoSize = (int)(20 + 20 * (float)i / 2);
            s.baseClipAmmoSize = (int)(10 + 20 * (float) i / 2);
            s.baseDamage = 10 + 20 * (float)i / 2;
            s.baseRange = 100 + 20 * (float)i / 2;
            s.fireRate = 0.25;
            s.reloadRate = 1;
            data.stats.add(s);
        }

        // data feature
        Hashtable<String, String> iv = new Hashtable<>();
        iv.put("maxHitEntities", "2");
        iv.put("particle", "CRIT");
        data.feature = new LinearGunAttribute(iv);

        LinearGun gun = new LinearGun(data, this);
        return  gun;
    }

    /*private Gun getGunByName(String name) throws Exception {
        GunData data = ZombiesPlugin.instance.getGunProvider().getGun(name);
        return (Gun)Class.forName(GUN_PACKAGES + data.feature)
                .getConstructor(GunData.class, GunUser.class).newInstance(data, this);
    }*/

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        if(slotCount > 0 && slotCount < preservedSlot.size()) {
            this.slotCount = slotCount;

            // remove gun to fit the player slot count
            for(int i = guns.size() - 1; i >= getSlotCount(); i--) {
                saveOldGunValue(getSlotIDByGun(guns.get(i)));
                user.getHotbar().removeObject(guns.get(i));
            }

            // add gun place holder
            for(int i = guns.size(); i < getSlotCount(); i++) {
                user.getHotbar().addObject(new GunPlaceHolder(i + 1), preservedSlot.get(i));
            }

        } else {
            // TODO: Log error -> the slot count count is larger than the preserved slots or < 0
        }
    }
}
