package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.data.BulletStats;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.player.GunUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Gun implements HotbarObject {
    public final GunData gunStats;

    /**
     * Note: Please accessing player inventory through this field will consideration,
     *       This class is designed to maintain and update certain item slot
     *      If you need to access the current item slot, use gunSlot
     */
    protected GunUser gunOwner;
    protected int ammo;
    protected int clipAmmo;
    protected int ultimateLevel;
    protected volatile boolean isVisible;


    protected ItemStack gunSlot;

    // previous data from drawGun()
    protected ItemStack preDraw;

    public Gun(GunData gunStats, GunUser user) {
        this.gunStats = gunStats;
        this.gunOwner = user;
    }

    /**
      * Getting call when the gun is equipped by a player
      */
    public void setLevel(int level) {
        if (level < gunStats.stats.size() - 1 && level >= 0) {
            ultimateLevel = level;
        }

        gunSlot = gunStats.getDefaultVisual(ultimateLevel, gunSlot);

        ammo = getCurrentStats().baseAmmoSize;
        clipAmmo = getCurrentStats().baseClipAmmoSize;
        gunSlot.setAmount(getCurrentStats().baseClipAmmoSize);
    }


    private long lastReloadTime = 0;
    public void reload() {
        // If we have enough ammo to refill
        if(clipAmmo >= Math.min(getCurrentStats().baseClipAmmoSize, ammo))
            return;

        // if we are reloading
        if(System.currentTimeMillis() - getCurrentStats().reloadRate * 1000 < lastReloadTime) {
            return;
        }

        lastReloadTime = System.currentTimeMillis();

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombiesPlugin.instance, new Runnable() {
            private  int step = 0;
            private  int maxVal = gunStats.displayItem.getMaxDurability();
            private  float stepVal = 1 / (float)getCurrentStats().reloadRate / 20 * maxVal;

            @Override
            public void run() {
                if(step < (int)(getCurrentStats().reloadRate * 20)) {
                    Damageable dmg = (Damageable)gunSlot.getItemMeta();
                    dmg.setDamage((int)(maxVal - (step + 1) * stepVal));
                    gunSlot.setItemMeta((ItemMeta)dmg);

                    step++;
                }
            }
        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(ZombiesPlugin.instance, new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(taskID);

                // Make sure we don't have weird durability value
                Damageable dmg = (Damageable)gunSlot.getItemMeta();
                dmg.setDamage(0);
                gunSlot.setItemMeta((ItemMeta)dmg);

                clipAmmo = Math.min(getCurrentStats().baseClipAmmoSize, ammo);
                gunSlot.setAmount(clipAmmo);
            }
        }, (int)(getCurrentStats().reloadRate * 20));
    }

    public void ultimate() {
        if(ultimateLevel < gunStats.stats.size() - 1) {
            ultimateLevel++;
            gunSlot = gunStats.getDefaultVisual(ultimateLevel, gunSlot);
            ammo = getCurrentStats().baseAmmoSize;
            clipAmmo = getCurrentStats().baseClipAmmoSize;

            gunSlot.setAmount(clipAmmo);
            gunOwner.user.getPlayer().setLevel(ammo);
        } else {
            gunOwner.user.getPlayer().sendMessage(ChatColor.RED + "This gun is too OP to ultimate z");
        }
    }

    public void refill () {
        gunSlot.setAmount(getCurrentStats().baseClipAmmoSize);
        ammo = getCurrentStats().baseAmmoSize;
    }

    // Perform a check for weapon firerate cooldown, reloading, out of ammo, etc
    protected boolean canShoot() {
        return System.currentTimeMillis() - getCurrentStats().fireRate * 1000 > lastShotTime &&
                System.currentTimeMillis() - getCurrentStats().reloadRate * 1000 > lastReloadTime &&
                ammo > 0;

    }

    // Subclass can call this method to update gun visual and animate gun cooldown
    long lastShotTime = 0;
    protected void updateVisualAfterShoot() {
        lastShotTime = System.currentTimeMillis();

        ammo--;
        clipAmmo--;
        if(clipAmmo > 0) {
            gunSlot.setAmount(clipAmmo);
        }
        else {
            Damageable dmg = (Damageable)gunSlot.getItemMeta();
            dmg.setDamage(gunStats.displayItem.getMaxDurability());
            gunSlot.setItemMeta((ItemMeta)dmg);
        }

        gunOwner.user.getPlayer().setLevel(ammo);

        if (clipAmmo > 0) {
            // Animate xp bar
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombiesPlugin.instance, new Runnable() {
                private int step = 0;
                private float stepVal = 1 / (float)getCurrentStats().fireRate / 20;

                @Override
                public void run() {
                   if(step < (int)(getCurrentStats().fireRate * 20)) {
                       gunOwner.user.getPlayer().setExp((step + 1) * stepVal);
                       step++;
                   }
                }
            }, 0, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(ZombiesPlugin.instance, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask(task);
                }
            }, (int)(getCurrentStats().fireRate * 20));
        } else {
            if (ammo > 0) {
                reload();
            } else {
                gunOwner.user.getPlayer().sendMessage(ChatColor.RED + "Tahmid took all of your ammo bro");
                gunOwner.user.getPlayer().sendMessage(ChatColor.GREEN + "Note: Tachibana Yui sells ammo for cheap :)");
            }
        }
    }
    public int getUltimateLevel() {
        return ultimateLevel;
    }

    public BulletStats getCurrentStats() {
        return gunStats.stats.get(getUltimateLevel());
    }

    public abstract void shoot();


    @Override
    public ItemStack getSlot() {
        return gunSlot;
    }

    @Override
    public void update() {

    }

    @Override
    public void onRemoved() {
        gunSlot = new ItemStack(null, 0);
    }

    // Use to hide gun when player knockdown or died
    @Override
    public void setVisibility(boolean visibility) {
        if(visibility) {
            gunSlot = gunStats.getDefaultVisual(ultimateLevel, gunSlot);
            gunSlot.setAmount(clipAmmo);
        } else {
            gunSlot.setType(null); // or .setAmount(0)
        }

        isVisible = visibility;
    }

    @Override
    public void init(ItemStack slot, Player player) {
        gunSlot = slot;
    }

    @Override
    public boolean onThrow() {
        // we do nothing here just tell the hotbar manager that we don't want to be thrown
        return false;
    }



}
