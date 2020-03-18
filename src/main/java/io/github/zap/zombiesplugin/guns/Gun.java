package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.guns.data.BulletStats;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.inventory.ItemStack;

public abstract class Gun {
    public final GunData gunStats;

    /**
     * Note: Please accessing player inventory through this field will consideration,
     *       This class is designed to maintain and update certain item slot
     *      If you need to access the current item slot, use gunSlot
     */
    protected User gunOwner;
    protected int ammo;
    protected int clipAmmo;
    protected int ultimateLevel;
    protected volatile boolean isVisible;

    // gunSlot in player hotbar
    protected ItemStack gunSlot;

    // previous data from drawGun()
    protected ItemStack preDraw;

    public Gun(GunData gunStats, User user) {
        this.gunStats = gunStats;
        this.gunOwner = user;
    }

    /**
      * Getting call when the gun is equipped by a player
      */
    public void initGun(ItemStack itemSlot, int level) {
        if (level < gunStats.stats.size() - 1 && level >= 0) {
            ultimateLevel = level;
        }

        gunSlot = gunStats.getDefaultVisual(ultimateLevel, itemSlot);

        ammo = getCurrentStats().baseAmmoSize;
        clipAmmo = getCurrentStats().baseClipAmmoSize;
        gunSlot.setAmount(getCurrentStats().baseAmmoSize);
    }

    public void reload() {
        // TODO: implement this methods
        clipAmmo = getCurrentStats().baseClipAmmoSize;
    }

    public void ultimate() {
        if(ultimateLevel < gunStats.stats.size() - 1) {
            ultimateLevel++;
            gunSlot = gunStats.getDefaultVisual(ultimateLevel, gunSlot);
        } else {
            // TODO: This weapon is maxed...
        }
    }

    public void refill () {
        gunSlot.setAmount(getCurrentStats().baseClipAmmoSize);
        ammo = getCurrentStats().baseAmmoSize;
    }

    // Use to hide gun when player knockdown or died
    public void setVisibility(boolean visibility) {
        if(visibility) {
            gunSlot = gunStats.getDefaultVisual(ultimateLevel, gunSlot);
            gunSlot.setAmount(clipAmmo);
        } else {
            gunSlot.setType(null); // or .setAmount(0)
        }

        isVisible = visibility;
    }

    // Perform a check for weapon firerate cooldown, reloading, out of ammo, etc
    protected boolean canShoot() {
        return true; // TODO: Placeholder value
    }

    // Subclass can call this method to update gun visual and animate gun cooldown
    protected void updateVisualAfterShoot() {
        ammo--;
        clipAmmo--;
        gunSlot.setAmount(clipAmmo);
        if (clipAmmo > 0) {
            // TODO: implement xp bar gun cooldown
        } else {
            if (ammo > 0) {
                reload();
            } else {
                // TODO: Message: This gun has ran out of ammo...
            }
        }
    }

    // Call this method when a gun is removed from the player
    public void releaseGunSlot() {
        // Prevent asynchronous operation to override new gun display
        gunSlot = new ItemStack(gunStats.displayItem, 0);
    }

    public int getUltimateLevel() {
        return ultimateLevel;
    }

    public BulletStats getCurrentStats() {
        return gunStats.stats.get(getUltimateLevel());
    }

    public GunData getGunStats() {
        return gunStats;
    }

    public abstract void shoot();
}
