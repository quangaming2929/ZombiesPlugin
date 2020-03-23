package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.ModifiableBulletStats;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public abstract class Gun extends HotbarObject {
    public final GunData gunData;
    private ModifiableBulletStats stats;

    private int ammo;
    private int clipAmmo;


    public Gun(GunData gunData) {
        this.gunData = gunData;
        setStats(attachStats(gunData));
    }


    /**
     * Set the current gun level, it will update the visual of the gun and
     * set the gun ammo to max. If you want to ultimate this gun, use the ultimate()
     * instead
     * @param level the level to set
     */
    public void setLevel(int level) {
        if (level < gunData.stats.size() && level >= 0) {
            setUltimateLevel(level);

            setSlot(gunData.getDefaultVisual(getUltimateLevel(), getSlot()));
            refill();
        }
    }

    /**
     * Ultimate this gun
     */
    public void ultimate() {
        if(getUltimateLevel() < gunData.stats.size() - 1) {
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            setLevel(getUltimateLevel() + 1);
        } else {
            getPlayer().sendMessage(ChatColor.RED + "This gun is too OP to ultimate z");
        }
    }

    private long lastReloadTime = 0;
    public void reload() {
        // If we have enough ammo to refill
        if(getClipAmmo() >= Math.min(getStats().getClipAmmoSize(), getAmmo()))
            return;

        // if we are reloading
        if(System.currentTimeMillis() - getStats().getReloadRate() * 1000 < lastReloadTime) {
            return;
        }

        lastReloadTime = System.currentTimeMillis();

        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_HORSE_GALLOP, 1 ,0.5f);

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombiesPlugin.instance, new Runnable() {
            private  int step = 0;
            private  int maxVal = gunData.displayItem.getMaxDurability();
            private  float stepVal = 1 / (float)getStats().getReloadRate() / 20 * maxVal;

            @Override
            public void run() {
                if(step < (int)(getStats().getReloadRate() * 20)) {
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
                setClipAmmo(Math.min(getStats().getClipAmmoSize(), getAmmo()));
            }
        }, (int)(getStats().getReloadRate() * 20));
    }

    public void refill () {
        // To prevent the game not updating the visual, we will schedule it
        Bukkit.getScheduler().scheduleSyncDelayedTask(ZombiesPlugin.instance, new Runnable() {
            @Override
            public void run() {
                setClipAmmo(getStats().getClipAmmoSize());
                setAmmo(getStats().getAmmoSize());
            }
        }, 1);
    }

    /**
     * Check if the gun is ready to shoot, every subclass
     * of this class should call this before actual shoot
     */
    protected boolean canShoot() {
         return System.currentTimeMillis() - getStats().getFireRate() * 1000 > lastShotTime &&
                System.currentTimeMillis() - getStats().getReloadRate() * 1000 > lastReloadTime &&
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
                private float stepVal = 1 / (float)getStats().getFireRate() / 20;

                @Override
                public void run() {
                   if(step < (int)(getStats().getFireRate() * 20) && isSelected()) {
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
            }, (int)(getStats().getFireRate() * 20));
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
     * Utility method to set the item durability
     * @param val the damage value to set
     */
    private void setItemDamage(int val) {
        Damageable dmg = (Damageable)getSlot().getItemMeta();
        dmg.setDamage(val);
        getSlot().setItemMeta((ItemMeta)dmg);
    }

    public abstract void shoot();

    /**
     * Create the stats contains all the gun values such as damage, fireRate,...
     */
    protected abstract ModifiableBulletStats attachStats(GunData gunStats);

    @Override
    public void setVisibility(boolean visibility) {
        super.setVisibility(visibility);

        if(visibility) {
            setSlot(gunData.getDefaultVisual(getUltimateLevel(), getSlot()));
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
            setItemDamage(gunData.displayItem.getMaxDurability() );
            getSlot().setAmount(1);
        }

        // TODO: work around for item not updating meta twice in 1 server thread iteration
        getPlayer().updateInventory();
    }

    public int getUltimateLevel() {
        return this.getStats().getLevel();
    }

    protected void setUltimateLevel(int ultimateLevel) {
        this.getStats().setLevel(ultimateLevel);
    }

    public ModifiableBulletStats getStats() {
        return stats;
    }

    public void setStats(ModifiableBulletStats stats) {
        this.stats = stats;
    }
}
