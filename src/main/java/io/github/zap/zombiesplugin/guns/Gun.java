package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Gun extends UpgradeableEquipment {
    // Define value names
    public static final String GUN_AMMO = "ammo";
    public static final String GUN_CLIP_AMMO = "clipAmmo";
    public static final String GUN_FIRE_RATE = "fireRate";
    public static final String GUN_RELOAD_RATE = "reloadRate";

    private int ammo;
    private int clipAmmo;

    public Gun(EquipmentData equipmentData) {
        super(equipmentData);
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(level);
        refill();
    }

    private void refill() {
        setClipAmmo((int)tryGetValue(GUN_CLIP_AMMO));
        setAmmo((int)tryGetValue(GUN_AMMO));
    }

    private long lastReloadTime = 0;
    public void reload() {
        // If we have enough ammo to refill
        if(getClipAmmo() >= Math.min(tryGetValue(GUN_CLIP_AMMO), getAmmo()))
            return;

        // if we are reloading
        if(System.currentTimeMillis() - tryGetValue(GUN_RELOAD_RATE) * 1000 < lastReloadTime) {
            return;
        }

        lastReloadTime = System.currentTimeMillis();

        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_HORSE_GALLOP, 1 ,0.5f);

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZombiesPlugin.instance, new Runnable() {
            private  int step = 0;
            private  int maxVal = getEquipmentData().getDisplayItem().getMaxDurability();
            private  float stepVal = 1 / (float)tryGetValue(GUN_RELOAD_RATE) / 20 * maxVal;

            @Override
            public void run() {
                if(step < (int)(tryGetValue(GUN_RELOAD_RATE) * 20)) {
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
                setClipAmmo(Math.min((int)tryGetValue(GUN_CLIP_AMMO), getAmmo()));
            }
        }, (int)(tryGetValue(GUN_RELOAD_RATE) * 20));
    }

    /**
     * Check if the gun is ready to shoot, every subclass
     * of this class should call this before actual shoot
     */
    protected boolean canShoot() {
        return System.currentTimeMillis() - tryGetValue(GUN_FIRE_RATE) * 1000 > lastShotTime &&
                System.currentTimeMillis() - tryGetValue(GUN_RELOAD_RATE) * 1000 > lastReloadTime &&
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
                private float stepVal = 1 / tryGetValue(GUN_FIRE_RATE) / 20;

                @Override
                public void run() {
                    if(step < (int)(tryGetValue(GUN_FIRE_RATE) * 20) && isSelected()) {
                        getPlayer().setExp((step + 1) * stepVal);
                        step++;
                    }
                }
            }, 0, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(ZombiesPlugin.instance, () -> {
                if(isSelected()) {
                    getPlayer().setExp(1);
                }

                Bukkit.getScheduler().cancelTask(task);
            }, (int)(tryGetValue(GUN_FIRE_RATE) * 20));
        } else {
            if (getAmmo() > 0) {
                reload();
            } else {
                getPlayer().sendMessage(ChatColor.RED + "Tahmid took all of your ammo bro");
                getPlayer().sendMessage(ChatColor.GREEN + "Note: Tachibana Yui sells ammo for cheap :)");
            }
        }
    }

    public abstract void shoot();

    public int getClipAmmo() {
        return clipAmmo;
    }

    protected void setClipAmmo(int clipAmmo) {
        if(isVisible()) {
            this.clipAmmo = clipAmmo;

            if(clipAmmo > 0) {
                setItemDamage(0);
                getSlot().setAmount(clipAmmo);
            } else {
                setItemDamage(getEquipmentData().getDisplayItem().getMaxDurability());
                getSlot().setAmount(1);
            }

            // TODO: work around for item not updating meta twice in 1 server thread iteration
            getPlayer().updateInventory();
        }
    }

    public int getAmmo() {
        return ammo;
    }

    protected void setAmmo(int ammo) {
        if(isVisible()) {
            this.ammo = ammo;
            getPlayer().setLevel(ammo);
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
}
