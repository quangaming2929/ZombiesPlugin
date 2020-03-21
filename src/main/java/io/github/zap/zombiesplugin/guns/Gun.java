package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.data.BulletStats;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.player.GunUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Gun extends HotbarObject {
    public final GunData gunStats;

    protected GunUser gunOwner;

    private int ammo;
    private int clipAmmo;
    private int ultimateLevel;


    public Gun(GunData gunStats, GunUser user) {
        this.gunStats = gunStats;
        this.gunOwner = user;
    }

    /**
     * Set the current gun level, it will update the visual of the gun and
     * set the gun ammo to max. If you want to ultimate this gun, use the ultimate()
     * instead
     * @param level the level to set
     */
    public void setLevel(int level) {
        if (level < gunStats.stats.size() - 1 && level >= 0) {
            setUltimateLevel(level);

            setSlot(gunStats.getDefaultVisual(getUltimateLevel(), getSlot()));
            refill();
        }
    }

    /**
     * Ultimate this gun
     */
    public void ultimate() {
        if(getUltimateLevel() < gunStats.stats.size() - 1) {
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            setLevel(getUltimateLevel() + 1);
        } else {
            getPlayer().sendMessage(ChatColor.RED + "This gun is too OP to ultimate z");
        }
    }

    private long lastReloadTime = 0;
    public void reload() {
        // If we have enough ammo to refill
        if(getClipAmmo() >= Math.min(getCurrentStats().getClipAmmoSize(), getAmmo()))
            return;

        // if we are reloading
        if(System.currentTimeMillis() - getCurrentStats().getReloadRate() * 1000 < lastReloadTime) {
            return;
        }

        lastReloadTime = System.currentTimeMillis();

        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_HORSE_GALLOP, 1 ,0.5f);

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombiesPlugin.instance, new Runnable() {
            private  int step = 0;
            private  int maxVal = gunStats.displayItem.getMaxDurability();
            private  float stepVal = 1 / (float)getCurrentStats().getReloadRate() / 20 * maxVal;

            @Override
            public void run() {
                if(step < (int)(getCurrentStats().getReloadRate() * 20)) {
                    setItemDamage((int)(maxVal - (step + 1) * stepVal));
                    step++;
                }
            }
        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(ZombiesPlugin.instance, new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(taskID);

                // Make sure we don't have weird durability value
                setItemDamage(0);
                setClipAmmo(Math.min(getCurrentStats().getClipAmmoSize(), getAmmo()));
            }
        }, (int)(getCurrentStats().getReloadRate() * 20));
    }

    public void refill () {
        setClipAmmo(getCurrentStats().getClipAmmoSize());
        setAmmo(getCurrentStats().getAmmoSize());
    }

    /**
     * Check if the gun is ready to shoot, every subclass
     * of this class should call this before actual shoot
     */
    protected boolean canShoot() {
         return System.currentTimeMillis() - getCurrentStats().getFireRate() * 1000 > lastShotTime &&
                System.currentTimeMillis() - getCurrentStats().getReloadRate() * 1000 > lastReloadTime &&
                getAmmo() > 0 && getClipAmmo() > 0;

    }

    // Subclass can call this method to update gun visual and animate gun cooldown
    long lastShotTime = 0;

    /**
     * Update the guns visual such as animating the xp bar
     * and set the gun item amount, every subclass of this
     * class should call to this method after a successful
     * shot
     */
    protected void updateVisualAfterShoot() {
        lastShotTime = System.currentTimeMillis();

        setAmmo(getAmmo() - 1);
        setClipAmmo(getClipAmmo() - 1);

        if (getClipAmmo() > 0) {
            // Animate xp bar
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombiesPlugin.instance, new Runnable() {
                private int step = 0;
                private float stepVal = 1 / (float)getCurrentStats().getFireRate() / 20;

                @Override
                public void run() {
                   if(step < (int)(getCurrentStats().getFireRate() * 20) && isSelected()) {
                       getPlayer().setExp((step + 1) * stepVal);
                       step++;
                   }
                }
            }, 0, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(ZombiesPlugin.instance, new Runnable() {
                @Override
                public void run() {
                    if(isSelected()) {
                        getPlayer().setExp(1);
                    }

                    Bukkit.getScheduler().cancelTask(task);
                }
            }, (int)(getCurrentStats().getFireRate() * 20));
        } else {
            if (getAmmo() > 0) {
                reload();
            } else {
                getPlayer().sendMessage(ChatColor.RED + "Tahmid took all of your ammo bro");
                getPlayer().sendMessage(ChatColor.GREEN + "Note: Tachibana Yui sells ammo for cheap :)");
            }
        }
    }

    /**
     * Get the current stats for the current gun
     * ultimate level
     */
    public BulletStats getCurrentStats() {
        return gunStats.stats.get(getUltimateLevel());
    }

    /**
     * Utility method to set the item durability
     * @param val the damage value to set
     */
    private void setItemDamage(int val) {
        Damageable dmg = (Damageable)getSlot().getItemMeta();
        dmg.setDamage(val);
        getSlot().setItemMeta((ItemMeta)dmg);
    }

    public abstract void shoot();

    @Override
    public void setVisibility(boolean visibility) {
        super.setVisibility(visibility);

        if(visibility) {
            setSlot(gunStats.getDefaultVisual(getUltimateLevel(), getSlot()));
            // Set the clipAmmo to itself to update the visual
            setClipAmmo(getClipAmmo());
            setAmmo(getAmmo());
        } else {
            getSlot().setAmount(0);
        }
    }

    @Override
    public boolean onRightClick(Block clickedBlock, BlockFace clickedFace) {
        super.onRightClick(clickedBlock, clickedFace);
        shoot();
        return true;
    }

    @Override
    public boolean onLeftClick(Block clickedBlock, BlockFace clickedFace) {
        super.onLeftClick(clickedBlock, clickedFace);
        reload();
        return true;
    }

    @Override
    public boolean onSlotDeSelected() {
        super.onSlotDeSelected();
        getPlayer().setLevel(0);
        getPlayer().setExp(0);
        return false;
    }

    @Override
    public void onSlotSelected() {
        super.onSlotSelected();
        getPlayer().setLevel(getAmmo());
        getPlayer().setExp(1);
    }


    public int getAmmo() {
        return ammo;
    }

    protected void setAmmo(int ammo) {
        this.ammo = ammo;
        getPlayer().setLevel(ammo);
    }

    public int getClipAmmo() {
        return clipAmmo;
    }

    protected void setClipAmmo(int clipAmmo) {
        this.clipAmmo = clipAmmo;

        if(clipAmmo > 0) {
            setItemDamage(0);
            getSlot().setAmount(clipAmmo);
        } else {
            setItemDamage(gunStats.displayItem.getMaxDurability());
            getSlot().setAmount(1);
        }
    }

    public int getUltimateLevel() {
        return ultimateLevel;
    }

    protected void setUltimateLevel(int ultimateLevel) {
        this.ultimateLevel = ultimateLevel;
    }
}