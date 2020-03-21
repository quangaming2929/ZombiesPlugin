package io.github.zap.zombiesplugin.guns.data;

import java.util.Hashtable;

public class BulletStats {

    public Hashtable<String, CustomValue> customValues;

    public double baseRange;
    public double baseDamage;
    public int baseAmmoSize;
    public int baseClipAmmoSize;
    public double baseFireRate;
    public double baseReloadRate;
    public int baseRewardGold;
    public float baseKnockback;

    public CustomValue getCustomValue(String name) {
        if(customValues.containsKey(name)) {
            return customValues.get(name);
        }

        return null;
    }

    public void setCustomValue(String name, CustomValue value) {
        if(customValues == null) {
            customValues = new Hashtable<>();
        }

        if(customValues.containsKey(name)) {
            customValues.replace(name, value);
        } else {
            customValues.put(name, value);
        }
    }

    /*
    public double getRange() {
        return baseRange;
    }

    public void setRange(double range) {
        this.baseRange = range;
    }

    public double getDamage() {
        return baseDamage;
    }

    public void setDamage(double damage) {
        this.baseDamage = damage;
    }

    public int getAmmoSize() {
        return baseAmmoSize;
    }

    public void setAmmoSize(int ammoSize) {
        this.baseAmmoSize = ammoSize;
    }

    public int getClipAmmoSize() {
        return baseClipAmmoSize;
    }

    public void setClipAmmoSize(int clipAmmoSize) {
        this.baseClipAmmoSize = clipAmmoSize;
    }

    public double getFireRate() {
        return baseFireRate;
    }

    public void setFireRate(double fireRate) {
        this.baseFireRate = fireRate;
    }

    public double getReloadRate() {
        return baseReloadRate;
    }

    public void setReloadRate(double reloadRate) {
        this.baseReloadRate = reloadRate;
    }

    public int getRewardGold() {
        return baseRewardGold;
    }

    public void setRewardGold(int rewardGold) {
        this.baseRewardGold = rewardGold;
    }

    public float getKnockback() {
        return baseKnockback;
    }

    public void setKnockback(float knockback) {
        this.baseKnockback = knockback;
    }*/
}
